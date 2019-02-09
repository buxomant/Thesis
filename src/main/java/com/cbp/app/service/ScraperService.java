package com.cbp.app.service;

import com.cbp.app.helper.LoggingHelper;
import com.cbp.app.model.SimpleLink;
import com.cbp.app.model.db.*;
import com.cbp.app.model.enumType.WebsiteContentType;
import com.cbp.app.model.enumType.WebsiteType;
import com.cbp.app.repository.*;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ScraperService {
    private final WebsiteRepository websiteRepository;
    private final WebsiteContentRepository websiteContentRepository;
    private final PageRepository pageRepository;
    private final WebsiteToWebsiteRepository websiteToWebsiteRepository;
    private final PageToPageRepository pageToPageRepository;
    private final SubdomainOfRepository subdomainOfRepository;

    @Autowired
    public ScraperService(
        WebsiteRepository websiteRepository,
        WebsiteContentRepository websiteContentRepository,
        PageRepository pageRepository,
        WebsiteToWebsiteRepository websiteToWebsiteRepository,
        PageToPageRepository pageToPageRepository,
        SubdomainOfRepository subdomainOfRepository
    ) {
        this.websiteRepository = websiteRepository;
        this.websiteContentRepository = websiteContentRepository;
        this.pageRepository = pageRepository;
        this.websiteToWebsiteRepository = websiteToWebsiteRepository;
        this.pageToPageRepository = pageToPageRepository;
        this.subdomainOfRepository = subdomainOfRepository;
    }

    public void fetchWebsiteContent(Website currentWebsite) {
        LocalTime startTime = LoggingHelper.logStartOfMethod("fetchWebsiteContent");

        Connection connection = null;
        Document webPage = null;
        String urlIncludingWwwAndProtocol;
        String url = currentWebsite.getUrl();
        String urlIncludingWww = RegexPatternService.urlMissingWwwPattern.matcher(url).matches() ? "www." + url : url;
        urlIncludingWwwAndProtocol = "https://" + urlIncludingWww;

        try {
            connection = Jsoup.connect(urlIncludingWwwAndProtocol);
            webPage = connection.get();
        } catch (HttpStatusException | SSLException | SocketException | SocketTimeoutException e) {
            urlIncludingWwwAndProtocol = "http://" + urlIncludingWww;
        } catch (IOException e) {
            saveWebsiteError(currentWebsite, connection, e.getMessage());
            return;
        }

        if (webPage == null) {
            try {
                connection = Jsoup.connect(urlIncludingWwwAndProtocol);
                webPage = connection.get();
            } catch (IOException e) {
                saveWebsiteError(currentWebsite, connection, e.getMessage());
                return;
            }
        }

        String cleanContent = webPage.toString().replaceAll("\u0000", "");
        if (cleanContent != null && !cleanContent.equals("")) {
            WebsiteContent websiteContent = new WebsiteContent(currentWebsite.getWebsiteId(), cleanContent);
            websiteContent.setTimeFetched(LocalDateTime.now());
            websiteContentRepository.save(websiteContent);
        }

        WebsiteType websiteType = WebsiteService.getWebsiteType(url, webPage.baseUri());
        WebsiteContentType websiteContentType = WebsiteService.getWebsiteContentType(webPage.baseUri());
        currentWebsite.setType(websiteType);
        currentWebsite.setContentType(websiteContentType);
        currentWebsite.setLastCheckedOn(LocalDateTime.now());
        currentWebsite.setLastResponseCode(connection.response().statusCode());
        websiteRepository.save(currentWebsite);

        LoggingHelper.logMessage("Fetched website: " + currentWebsite.getUrl());
        LoggingHelper.logEndOfMethod("fetchWebsiteContent", startTime);
    }

    private void saveWebsiteError(Website currentWebsite, Connection connection, String errorMessage) {
        currentWebsite.setError(errorMessage);
        currentWebsite.setLastCheckedOn(LocalDateTime.now());
        if (connection != null) {
            currentWebsite.setLastResponseCode(connection.response().statusCode());
        }
        websiteRepository.save(currentWebsite);
    }

    public void processWebsite(Website currentWebsite) {
        LocalTime startTime = LoggingHelper.logStartOfMethod("processWebsite");

        String url = currentWebsite.getUrl();
        WebsiteContent websiteContent = websiteContentRepository.getFirstByWebsiteIdAndTimeProcessedIsNull(currentWebsite.getWebsiteId());
        Document webPage = Jsoup.parse(websiteContent.getContent());
        Elements linkElements = webPage.select("a");

        List<SimpleLink> links = LinkService.domLinksToSimpleLinks(linkElements, url);
        processLinks(links, currentWebsite, websiteContent);

        currentWebsite.setLastProcessedOn(LocalDateTime.now());
        websiteRepository.save(currentWebsite);

        websiteContent.setTimeProcessed(LocalDateTime.now());
        websiteContentRepository.save(websiteContent);

        LoggingHelper.logMessage("Processed website: " + currentWebsite.getUrl());
        LoggingHelper.logEndOfMethod("processWebsite", startTime);
    }

    private void processLinks(List<SimpleLink> links, Website currentWebsite, WebsiteContent websiteContent) {
        Map<SimpleLink, Website> websitesByLinkTitle = linksToWebsitesByLinkTitle(links);

        List<String> websiteUrls = websitesByLinkTitle.values().stream().map(Website::getUrl).collect(Collectors.toList());
        List<Website> existingWebsites = websiteRepository.findAllByUrlIn(websiteUrls);
        List<String> existingWebsiteUrls = existingWebsites.stream().map(Website::getUrl).collect(Collectors.toList());
        List<Website> newWebsites = websitesByLinkTitle.values().stream()
            .filter(website -> !existingWebsiteUrls.contains(website.getUrl())).collect(Collectors.toList());
        List<Website> savedNewWebsites = websiteRepository.saveAll(newWebsites);

        websitesByLinkTitle.replaceAll((link, website) -> {
            Optional<Website> existingWebsite = existingWebsites.stream().filter(w -> w.getUrl().equals(website.getUrl())).findFirst();
            if (existingWebsite.isPresent()) {
                return existingWebsite.get();
            }
            Optional<Website> newWebsite = savedNewWebsites.stream().filter(w -> w.getUrl().equals(website.getUrl())).findFirst();
            return newWebsite.orElse(website);
        });

        List<WebsiteToWebsite> websiteToWebsites = createWebsiteToWebsiteLinks(websitesByLinkTitle, websiteContent);
        websiteToWebsiteRepository.saveAll(websiteToWebsites);

        Map<SimpleLink, Page> pagesByLinkTitle = websitesByLinkTitleToPagesByLinkTitle(websitesByLinkTitle);

        List<String> pageUrls = pagesByLinkTitle.values().stream().map(Page::getUrl).collect(Collectors.toList());
        List<Page> existingPages = pageRepository.findAllByUrlIn(pageUrls);
        List<String> existingPageUrls = existingPages.stream().map(Page::getUrl).collect(Collectors.toList());
        List<Page> newPages = pagesByLinkTitle.values().stream()
            .filter(page -> !existingPageUrls.contains(page.getUrl())).collect(Collectors.toList());
        List<Page> savedNewPages = pageRepository.saveAll(newPages);

        pagesByLinkTitle.replaceAll((link, page) -> {
            Optional<Page> existingPage = existingPages.stream().filter(p -> p.getUrl().equals(page.getUrl())).findFirst();
            if (existingPage.isPresent()) {
                return existingPage.get();
            }
            Optional<Page> newPage = savedNewPages.stream().filter(p -> p.getUrl().equals(page.getUrl())).findFirst();
            return newPage.orElse(page);
        });

        List<Page> pagesMatchingUrl = pageRepository.findAllByUrlOrderByPageId(currentWebsite.getUrl());
        Page currentPage = pagesMatchingUrl.size() > 0
            ? fixDuplicatePage(pagesMatchingUrl)
            : WebsiteService.createNewPageForWebsiteHomePage(websiteRepository.getOne(websiteContent.getWebsiteId()));
        Page savedCurrentPage = (currentPage.getPageId() == 0)
            ? pageRepository.save(currentPage)
            : currentPage;

        List<PageToPage> pageToPages = createPageToPageLinks(pagesByLinkTitle, savedCurrentPage, pagesMatchingUrl, websiteContent);
        pageToPageRepository.saveAll(pageToPages);
    }

    private Map<SimpleLink, Website> linksToWebsitesByLinkTitle(List<SimpleLink> links) {
        return links.stream()
            .map(LinkService::stripSubPage)
            .filter(distinctByKey(SimpleLink::getLinkUrl))
            .collect(Collectors.toMap(
                Function.identity(),
                link -> WebsiteService.createNewWebsite(link.getLinkUrl())
            ));
    }

    private Map<SimpleLink, Page> websitesByLinkTitleToPagesByLinkTitle(Map<SimpleLink, Website> websitesByLinkTitle) {
        return websitesByLinkTitle.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> WebsiteService.createNewPageForWebsiteHomePage(entry.getValue())
            ));
    }

    private List<WebsiteToWebsite> createWebsiteToWebsiteLinks(
        Map<SimpleLink, Website> websitesByLinkTitle,
        WebsiteContent websiteContent
    ) {
        return websitesByLinkTitle.entrySet().stream()
            .map(entry -> new WebsiteToWebsite(
                websiteContent.getWebsiteId(),
                entry.getValue().getWebsiteId(),
                websiteContent.getContentId(),
                entry.getKey().getLinkTitle()
            ))
            .collect(Collectors.toList());
    }

    private List<PageToPage> createPageToPageLinks(
        Map<SimpleLink, Page> pagesByLinkTitle,
        Page savedCurrentPage,
        List<Page> pagesMatchingUrl,
        WebsiteContent websiteContent
    ) {
        return pagesByLinkTitle.entrySet().stream()
            .filter(entry -> !(pagesMatchingUrl.contains(entry.getValue()) && entry.getValue().getPageId() != savedCurrentPage.getPageId()))
            .map(entry -> new PageToPage(
                savedCurrentPage.getPageId(),
                entry.getValue().getPageId(),
                websiteContent.getContentId(),
                entry.getKey().getLinkTitle()
            ))
            .collect(Collectors.toList());
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public Website fixDuplicateWebsite(List<Website> websitesMatchingUrl) {
        Website earliestWebsite = websitesMatchingUrl.get(0);

        if (websitesMatchingUrl.size() > 1) {
            websitesMatchingUrl.forEach(website -> {
                if (website.getWebsiteId() != earliestWebsite.getWebsiteId()) {
                    LocalTime startTime = LoggingHelper.logStartOfMethod("fixDuplicateWebsite");

                    List<Page> pages = pageRepository.findAllByWebsiteId(website.getWebsiteId());
                    pages.forEach(page -> {
                        pageToPageRepository.deleteAllByPageIdFrom(page.getPageId());
                        pageToPageRepository.deleteAllByPageIdTo(page.getPageId());
                    });

                    pageRepository.deleteAllByWebsiteId(website.getWebsiteId());
                    websiteToWebsiteRepository.deleteAllByWebsiteIdFrom(website.getWebsiteId());
                    websiteToWebsiteRepository.findAllByWebsiteIdTo(website.getWebsiteId()).forEach(websiteToWebsite -> {
                        websiteToWebsite.setWebsiteIdTo(earliestWebsite.getWebsiteId());
                        websiteToWebsiteRepository.save(websiteToWebsite);
                    });
                    subdomainOfRepository.findAllByWebsiteIdParent(website.getWebsiteId()).forEach(subdomainOf -> {
                        subdomainOf.setWebsiteIdParent(earliestWebsite.getWebsiteId());
                        subdomainOfRepository.save(subdomainOf);
                    });
                    subdomainOfRepository.findAllByWebsiteIdChild(website.getWebsiteId()).forEach(subdomainOf -> {
                        subdomainOf.setWebsiteIdChild(earliestWebsite.getWebsiteId());
                        subdomainOfRepository.save(subdomainOf);
                    });

                    websiteRepository.delete(website);

                    LoggingHelper.logEndOfMethod("fixDuplicateWebsite", startTime);
                }
            });
        }

        return earliestWebsite;
    }

    private Page fixDuplicatePage(List<Page> pagesMatchingUrl) {
        Page earliestPage = pagesMatchingUrl.get(0);
        pagesMatchingUrl.forEach(page -> {
            if (page.getPageId() != earliestPage.getPageId()) {
                LocalTime startTime = LoggingHelper.logStartOfMethod("fixDuplicatePage");

                pageToPageRepository.deleteAllByPageIdFrom(page.getPageId());
                pageToPageRepository.findAllByPageIdTo(page.getPageId()).forEach(pageToPage -> {
                    pageToPage.setPageIdTo(earliestPage.getPageId());
                    pageToPageRepository.save(pageToPage);
                });

                pageRepository.delete(page);

                LoggingHelper.logEndOfMethod("fixDuplicatePage", startTime);
            }
        });

        return earliestPage;
    }

    public void establishSubdomainRelationshipsForWebsite(Website website) {
        LocalTime startTime = LoggingHelper.logStartOfMethod("establishSubdomainRelationshipsForWebsite");

        List<Website> websiteSubdomains = websiteRepository.getSubdomainsForUrl(website.getUrl());
        websiteSubdomains.forEach(subdomain -> {
            SubdomainOf subdomainRelationship = new SubdomainOf(website.getWebsiteId(), subdomain.getWebsiteId());
            subdomainOfRepository.save(subdomainRelationship);
        });

        LoggingHelper.logEndOfMethod("establishSubdomainRelationshipsForWebsite", startTime);
    }
}
