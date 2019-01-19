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
import org.springframework.dao.IncorrectResultSizeDataAccessException;
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
import java.util.stream.Stream;

@Service
public class ScraperService {
    private final WebsiteRepository websiteRepository;
    private final WebsiteContentRepository websiteContentRepository;
    private final PageRepository pageRepository;
    private final LinksToRepository linksToRepository;
    private final SubdomainOfRepository subdomainOfRepository;
    private final RegexPatternService regexPatternService;

    @Autowired
    public ScraperService(
        WebsiteRepository websiteRepository,
        WebsiteContentRepository websiteContentRepository,
        PageRepository pageRepository,
        LinksToRepository linksToRepository,
        SubdomainOfRepository subdomainOfRepository,
        RegexPatternService regexPatternService
    ) {
        this.websiteRepository = websiteRepository;
        this.websiteContentRepository = websiteContentRepository;
        this.pageRepository = pageRepository;
        this.linksToRepository = linksToRepository;
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

        WebsiteType websiteType = getWebsiteType(url, webPage.baseUri());
        WebsiteContentType websiteContentType = getWebsiteContentType(webPage.baseUri());
        currentWebsite.setType(websiteType);
        currentWebsite.setContentType(websiteContentType);
        currentWebsite.setLastCheckedOn(LocalDateTime.now());
        currentWebsite.setLastResponseCode(connection.response().statusCode());
        websiteRepository.save(currentWebsite);

        String cleanContent = webPage.toString().replaceAll("\u0000", "");
        if (cleanContent != null && !cleanContent.equals("")) {
            WebsiteContent websiteContent = new WebsiteContent(currentWebsite.getWebsiteId(), cleanContent);
            websiteContent.setTimeFetched(LocalDateTime.now());
            websiteContentRepository.save(websiteContent);
        }

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

        Stream<String> links = linkElements.stream()
            .map(link -> link.attr("href"))
            .distinct()
            .map(String::trim)
            .map(this::stripProtocolPrefix)
            .map(this::stripWwwPrefix)
            .map(link -> fixLocalLinksAndGlobalLinks(link, url))
            .filter(this::isValidWebUrl)
            .map(this::stripTrailingSlash)
            .map(this::stripUselessPrefix)
            .map(this::stripQueryString)
            .filter(this::isNotEmptyOrUseless);

        links.forEach(link -> handleLinks(link, url, currentWebsite));

        currentWebsite.setLastProcessedOn(LocalDateTime.now());
        websiteRepository.save(currentWebsite);

        websiteContent.setTimeProcessed(LocalDateTime.now());

        LoggingHelper.logMessage("Processed website: " + currentWebsite.getUrl());
        LoggingHelper.logEndOfMethod("processWebsite", startTime);
    }

    private void handleLinks(String link, String currentUrl, Website currentWebsite) {
        if (!link.equals(currentUrl)) {
            if (link.contains(currentUrl)) {
                handleNewPages(link, currentWebsite);
            } else {
                handleNewWebsites(link, currentWebsite);
            }
        }
    }

    private void handleNewPages(String link, Website website) {
        Optional<Page> existingPage = pageRepository.findByUrl(link);
        if (!existingPage.isPresent()) {
            Page linkedPage = new Page(link, LocalDateTime.now(), website.getWebsiteId());
            pageRepository.save(linkedPage);
        }
    }

    private void handleNewWebsites(String link, Website website) {
        String websiteUrl = stripSubPage(link).toLowerCase();
        try {
            Optional<Website> existingWebsite = websiteRepository.findByUrl(websiteUrl);
            Website linkedWebsite = existingWebsite.orElseGet(() -> saveNewWebsite(websiteUrl));
            handleLinksToAndFrom(website, linkedWebsite);
        } catch (IncorrectResultSizeDataAccessException e) {
            e.printStackTrace();
        }
    }

    private void handleLinksToAndFrom(Website websiteFrom, Website websiteTo) {
        if (websiteFrom.getWebsiteId() != websiteTo.getWebsiteId()) {
            Optional<LinksTo> existingLink = linksToRepository.findByWebsiteIdFromAndWebsiteIdTo(websiteFrom.getWebsiteId(), websiteTo.getWebsiteId());
            LinksTo linksTo = existingLink.orElseGet(() -> new LinksTo(websiteFrom.getWebsiteId(), websiteTo.getWebsiteId()));
            linksToRepository.save(linksTo);
        }
    }

    private Website saveNewWebsite(String websiteUrl) {
        Website linkedWebsite = new Website(null, websiteUrl, LocalDateTime.now());
        WebsiteType websiteType = getWebsiteType(websiteUrl, websiteUrl);
        WebsiteContentType websiteContentType = getWebsiteContentType(websiteUrl);
        linkedWebsite.setType(websiteType);
        linkedWebsite.setContentType(websiteContentType);

        return websiteRepository.save(linkedWebsite);
//        WebsiteContent websiteContent = new WebsiteContent(savedWebsite.getWebsiteId(), null);
//        websiteContentRepository.save(websiteContent);
//        return savedWebsite;
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

    private String fixLocalLinksAndGlobalLinks(String link, String baseUrl) {
        if (regexPatternService.getGlobalLinkPattern().matcher(link).matches()) {
            return link.substring(2);
        } else {
            return regexPatternService.getLocalLinkPattern().matcher(link).matches()
                ? baseUrl + link
                : link;
        }
    }

    private String stripTrailingSlash(String link) {
        return link.endsWith("/")
            ? link.substring(0, link.length() - 1)
            : link;
    }

    private String stripUselessPrefix(String link) {
        return link.startsWith("#")
            ? link.substring(1, link.length())
            : link;
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

    private boolean isNotEmptyOrUseless(String link) {
        return !link.equals("") && !link.equals("/") && !link.equals("#");
    }

    private boolean isValidWebUrl(String link) {
        return link.contains("@")
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

    public void fixDuplicateWebsite(String websiteUrl) {
        LocalTime startTime = LoggingHelper.logStartOfMethod("fixDuplicateWebsite");

        List<Website> duplicateWebsites = websiteRepository.findAllByUrlOrderByWebsiteId(websiteUrl);
        Website earliestWebsite = duplicateWebsites.get(0);
        duplicateWebsites.forEach(website -> {
            if (website.getWebsiteId() != earliestWebsite.getWebsiteId()) {
                pageRepository.deleteAllByWebsiteId(website.getWebsiteId());
                linksToRepository.deleteAllByWebsiteIdFrom(website.getWebsiteId());
                linksToRepository.findAllByWebsiteIdTo(website.getWebsiteId()).forEach(linksTo -> {
                    linksTo.setWebsiteIdTo(earliestWebsite.getWebsiteId());
                    linksToRepository.save(linksTo);
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
            }
        });

        LoggingHelper.logEndOfMethod("fixDuplicateWebsite", startTime);
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
