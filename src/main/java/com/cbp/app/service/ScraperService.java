package com.cbp.app.service;

import com.cbp.app.helper.LoggingHelper;
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
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

@Service
public class ScraperService {
    private final WebsiteRepository websiteRepository;
    private final WebsiteContentRepository websiteContentRepository;
    private final PageRepository pageRepository;
    private final WebsiteToWebsiteRepository websiteToWebsiteRepository;
    private final PageToPageRepository pageToPageRepository;
    private final SubdomainOfRepository subdomainOfRepository;
    private final RegexPatternService regexPatternService;

    @Autowired
    public ScraperService(
        WebsiteRepository websiteRepository,
        WebsiteContentRepository websiteContentRepository,
        PageRepository pageRepository,
        WebsiteToWebsiteRepository websiteToWebsiteRepository,
        PageToPageRepository pageToPageRepository,
        SubdomainOfRepository subdomainOfRepository,
        RegexPatternService regexPatternService
    ) {
        this.websiteRepository = websiteRepository;
        this.websiteContentRepository = websiteContentRepository;
        this.pageRepository = pageRepository;
        this.websiteToWebsiteRepository = websiteToWebsiteRepository;
        this.pageToPageRepository = pageToPageRepository;
        this.subdomainOfRepository = subdomainOfRepository;
        this.regexPatternService = regexPatternService;
    }

    public void fetchWebsiteContent(Website currentWebsite) {
        LocalTime startTime = LoggingHelper.logStartOfMethod("fetchWebsiteContent");

        Connection connection = null;
        Document webPage = null;
        String urlIncludingWwwAndProtocol;
        String url = currentWebsite.getUrl();
        String urlIncludingWww = regexPatternService.getUrlMissingWwwPattern().matcher(url).matches() ? "www." + url : url;
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

        WebsiteType websiteType = getWebsiteType(url, webPage.baseUri());
        WebsiteContentType websiteContentType = getWebsiteContentType(webPage.baseUri());
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

        List<String> links = linkElements.stream()
            .map(link -> link.attr("href"))
            .map(String::trim)
            .map(String::toLowerCase)
            .map(this::stripProtocolPrefix)
            .map(this::stripWwwPrefix)
            .map(this::trimNonAlphanumericContent)
            .map(this::stripAnchorString)
            .map(this::stripQueryString)
            .map(link -> convertLocalLinksAndGlobalLinks(link, url))
            .filter(this::isValidWebUrl)
            .filter(this::isNotEmptyOrUseless)
            .distinct()
            .collect(Collectors.toList());

        processLinks(links, currentWebsite, websiteContent);

        currentWebsite.setLastProcessedOn(LocalDateTime.now());
        websiteRepository.save(currentWebsite);

        websiteContent.setTimeProcessed(LocalDateTime.now());
        websiteContentRepository.save(websiteContent);

        LoggingHelper.logMessage("Processed website: " + currentWebsite.getUrl());
        LoggingHelper.logEndOfMethod("processWebsite", startTime);
    }

    private void processLinks(List<String> links, Website currentWebsite, WebsiteContent websiteContent) {
        List<Website> allWebsites = links.stream()
            .map(this::stripSubPage)
            .distinct()
            .map(this::createNewWebsite)
            .collect(Collectors.toList());
        List<String> websiteUrls = allWebsites.stream().map(Website::getUrl).collect(Collectors.toList());
        List<Website> existingWebsites = websiteRepository.findAllByUrlIn(websiteUrls);
        List<String> existingWebsiteUrls = existingWebsites.stream().map(Website::getUrl).collect(Collectors.toList());
        List<Website> newWebsites = allWebsites.stream().filter(website -> !existingWebsiteUrls.contains(website.getUrl())).collect(Collectors.toList());

        List<Website> savedNewWebsites = websiteRepository.saveAll(newWebsites);
        existingWebsites.addAll(savedNewWebsites);

        List<WebsiteToWebsite> websiteToWebsites = existingWebsites.stream()
            .map(website -> new WebsiteToWebsite(websiteContent.getWebsiteId(), website.getWebsiteId(), websiteContent.getContentId()))
            .collect(Collectors.toList());
        websiteToWebsiteRepository.saveAll(websiteToWebsites);

        List<Page> pages = allWebsites.stream()
            .map(this::createNewPageForWebsiteHomePage)
            .collect(Collectors.toList());
        List<String> pageUrls = pages.stream().map(Page::getUrl).collect(Collectors.toList());
        List<Page> existingPages = pageRepository.findAllByUrlIn(pageUrls);
        List<String> existingPagesUrls = existingPages.stream().map(Page::getUrl).collect(Collectors.toList());
        List<Page> newPages = pages.stream().filter(page -> !existingPagesUrls.contains(page.getUrl())).collect(Collectors.toList());

        List<Page> savedNewPages = pageRepository.saveAll(newPages);
        existingPages.addAll(savedNewPages);

        List<Page> pagesMatchingUrl = pageRepository.findAllByUrlOrderByPageId(currentWebsite.getUrl());
        Page currentPage = pagesMatchingUrl.size() > 0
            ? fixDuplicatePage(pagesMatchingUrl)
            : createNewPageForWebsiteHomePage(websiteRepository.getOne(websiteContent.getWebsiteId()));

        List<PageToPage> pageToPages = existingPages.stream()
            .map(page -> new PageToPage(currentPage.getPageId(), page.getPageId(), websiteContent.getContentId()))
            .collect(Collectors.toList());

        pageToPageRepository.saveAll(pageToPages);
    }

    private Website createNewWebsite(String websiteUrl) {
        Website website = new Website(null, websiteUrl, LocalDateTime.now());
        WebsiteType websiteType = getWebsiteType(websiteUrl, websiteUrl);
        WebsiteContentType websiteContentType = getWebsiteContentType(websiteUrl);
        website.setType(websiteType);
        website.setContentType(websiteContentType);
        return website;
    }

    private Page createNewPageForWebsiteHomePage(Website website) {
        return new Page(website.getUrl(), LocalDateTime.now(), website.getWebsiteId());
    }

    private String stripProtocolPrefix(String link) {
        return link
            .replace("https//", "")
            .replace("https://", "")
            .replace("https:/", "")
            .replace("https:\\\\", "")
            .replace("https:\\", "")
            .replace("http//", "")
            .replace("http://", "")
            .replace("http:/", "")
            .replace("http:\\\\", "")
            .replace("http:\\", "");
    }

    private String stripWwwPrefix(String link) {
        if (link.startsWith("www.")) {
            return link.substring(4);
        } else {
            return link;
        }
    }

    private String convertLocalLinksAndGlobalLinks(String link, String baseUrl) {
        if (regexPatternService.getLocalPageLinkPattern().matcher(link).matches()
            || regexPatternService.getLocalLinkPattern().matcher(link).matches()
            || !link.contains(".")
        ) {
            return baseUrl + "/" + link;
        } else {
            return link;
        }
    }

    private String stripAnchorString(String link) {
        Matcher matcher = regexPatternService.getAnchorStringPattern().matcher(link);
        if (matcher.matches()) {
            return matcher.group(1);
        } else {
            return link;
        }
    }

    private String stripQueryString(String link) {
        Matcher matcher = regexPatternService.getQueryStringPattern().matcher(link);
        if (matcher.matches()) {
            return matcher.group(1);
        } else {
            return link;
        }
    }

    private String stripSubPage(String link) {
        Matcher matcher = regexPatternService.getSubPagePattern().matcher(link);
        if (matcher.matches()) {
            return matcher.group(1);
        } else {
            return link;
        }
    }

    private String trimNonAlphanumericContent(String link) {
        Matcher matcher = regexPatternService.getAlphanumericContentPattern().matcher(link);
        return matcher.replaceAll("");
    }

    private boolean isNotEmptyOrUseless(String link) {
        return !link.equals("") && !link.equals("/") && !link.equals("#");
    }

    private boolean isValidWebUrl(String link) {
        return !link.contains("@")
            && !link.startsWith("#")
            && !regexPatternService.getNonWebProtocolPattern().matcher(link).matches()
            && !regexPatternService.getNonWebResourcePattern().matcher(link).matches();
    }

    private WebsiteType getWebsiteType(String storedUrl, String actualUrl) {
        if (regexPatternService.getIndexingServicePattern().matcher(actualUrl).matches()) {
            return WebsiteType.INDEXING_SERVICE;
        }
        if (isDomesticWebsite(storedUrl) && !isDomesticWebsite(actualUrl)) {
            return WebsiteType.REDIRECT_TO_FOREIGN;
        }
        if (isDomesticWebsite(actualUrl)) {
            return WebsiteType.DOMESTIC;
        } else {
            return WebsiteType.FOREIGN;
        }
    }

    private WebsiteContentType getWebsiteContentType(String url) {
        if (regexPatternService.getSocialMediaWebsitePattern().matcher(url).matches()) {
            return WebsiteContentType.SOCIAL_MEDIA;
        }
        if (regexPatternService.getDomesticNewsWebsitePattern().matcher(url).matches()) {
            return WebsiteContentType.NEWS;
        }
        return WebsiteContentType.UNCATEGORIZED;
    }

    private boolean isDomesticWebsite(String url) {
        return regexPatternService.getDomesticWebsitePattern().matcher(url).matches()
            || regexPatternService.getDomesticNewsWebsitePattern().matcher(url).matches();
    }

    public Website fixDuplicateWebsite(List<Website> websitesMatchingUrl) {
        Website earliestWebsite = websitesMatchingUrl.get(0);

        if (websitesMatchingUrl.size() > 1) {
            websitesMatchingUrl.forEach(website -> {
                if (website.getWebsiteId() != earliestWebsite.getWebsiteId()) {
                    LocalTime startTime = LoggingHelper.logStartOfMethod("fixDuplicateWebsite");

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
                    pageToPage.setPageIdTo(earliestPage.getWebsiteId());
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
