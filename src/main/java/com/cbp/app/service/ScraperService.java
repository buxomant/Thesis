package com.cbp.app.service;

import com.cbp.app.model.db.LinksTo;
import com.cbp.app.model.db.Page;
import com.cbp.app.model.db.Website;
import com.cbp.app.repository.LinksToRepository;
import com.cbp.app.repository.PageRepository;
import com.cbp.app.repository.WebsiteRepository;
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
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
public class ScraperService {
    private final WebsiteRepository websiteRepository;
    private final PageRepository pageRepository;
    private final LinksToRepository linksToRepository;
    private final Pattern urlMissingWwwPattern;
    private final Pattern globalLinkPattern;
    private final Pattern localLinkPattern;
    private final Pattern queryStringPattern;
    private final Pattern subPagePattern;
    private final Pattern nonWebResourcePattern;
    private final Pattern websitePattern;

    @Autowired
    public ScraperService(
        WebsiteRepository websiteRepository,
        PageRepository pageRepository,
        LinksToRepository linksToRepository
    ) {
        this.websiteRepository = websiteRepository;
        this.pageRepository = pageRepository;
        this.linksToRepository = linksToRepository;
        this.urlMissingWwwPattern = Pattern.compile("^\\w+\\.ro");
        this.globalLinkPattern = Pattern.compile("^\\/{2}.+");
        this.localLinkPattern = Pattern.compile("^\\/.+");
        this.queryStringPattern = Pattern.compile("(.*)\\?.*");
        this.subPagePattern = Pattern.compile("(.*\\.ro)/.*");
        this.nonWebResourcePattern = Pattern.compile(".*\\.(?:bmp|jpg|jpeg|png|gif|svg|pdf|doc|docx|xls|xlsx|ppt|pptx|ashx|xml)$");
        this.websitePattern = Pattern.compile(".+\\.ro$");
    }

    public void findLinksOnWebsite(Website currentWebsite) throws IOException {
        Connection connection;
        Document webPage = null;
        String urlIncludingWwwAndProtocol;
        String url = currentWebsite.getUrl();
        String urlIncludingWww = urlMissingWwwPattern.matcher(url).matches() ? "www." + url : url;
        urlIncludingWwwAndProtocol = "https://" + urlIncludingWww;
        connection = Jsoup.connect(urlIncludingWwwAndProtocol);

        try {
            webPage = connection.get();
        } catch (HttpStatusException | SSLException | SocketException | SocketTimeoutException e) {
            urlIncludingWwwAndProtocol = "http://" + urlIncludingWww;
            connection = Jsoup.connect(urlIncludingWwwAndProtocol);
        } catch (UnknownHostException e) {
            currentWebsite.setLastCheckedOn(LocalDateTime.now());
            currentWebsite.setLastResponseCode(connection.response().statusCode());
            websiteRepository.save(currentWebsite);
            return;
        }

        if (webPage == null) {
            try {
                webPage = connection.get();
            } catch (IOException e) {
                currentWebsite.setError(e.getMessage());
                currentWebsite.setLastCheckedOn(LocalDateTime.now());
                currentWebsite.setLastResponseCode(connection.response().statusCode());
                websiteRepository.save(currentWebsite);
                return;
            }
        }

        if (!isValidUrl(webPage.baseUri())) {
            currentWebsite.setLastCheckedOn(LocalDateTime.now());
            currentWebsite.setLastResponseCode(connection.response().statusCode());
            currentWebsite.setContent(webPage.toString());
            currentWebsite.setRedirectsToExternal(true);
            websiteRepository.save(currentWebsite);
            return;
        }

        Elements linkElements = webPage.select("a");

        Stream<String> links = linkElements.stream()
            .map(link -> link.attr("href"))
            .distinct()
            .filter(this::isNotEmptyOrUseless)
            .map(String::trim)
            .map(this::stripProtocolPrefix)
            .map(link -> fixLocalLinksAndGlobalLinks(link, url))
            .filter(this::isValidUrl)
            .map(this::stripTrailingSlash)
            .map(this::stripQueryString);

        links.forEach(link -> handleLinks(link, url, currentWebsite));

        currentWebsite.setContent(webPage.toString());
        currentWebsite.setLastCheckedOn(LocalDateTime.now());
        currentWebsite.setLastResponseCode(connection.response().statusCode());
        websiteRepository.save(currentWebsite);
    }

    private void handleLinks(String link, String currentUrl, Website currentWebsite) {
        if (link.equals(currentUrl)) {
            return;
        }
        if (link.contains(currentUrl)) {
            handleNewPages(link, currentWebsite);
        } else if (isValidWebsite(link)) {
            handleNewWebsites(link, currentWebsite);
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
        Optional<Website> existingWebsite = websiteRepository.findByUrl(websiteUrl);
        Website linkedWebsite = existingWebsite.orElseGet(() -> saveNewWebsite(websiteUrl));
        handleLinksToAndFrom(website, linkedWebsite);
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
        return websiteRepository.save(linkedWebsite);
    }

    private String stripProtocolPrefix(String link) {
        return link
            .replace("https://", "")
            .replace("http://", "");
    }

    private String fixLocalLinksAndGlobalLinks(String link, String baseUrl) {
        if (globalLinkPattern.matcher(link).matches()) {
            return link.substring(2, link.length());
        } else {
            return localLinkPattern.matcher(link).matches()
                ? baseUrl + link
                : link;
        }
    }

    private String stripTrailingSlash(String link) {
        return link.endsWith("/")
            ? link.substring(0, link.length() - 1)
            : link;
    }

    private String stripQueryString(String link) {
        Matcher matcher = queryStringPattern.matcher(link);
        if (matcher.matches()) {
            return matcher.group(1);
        } else {
            return link;
        }
    }

    private String stripSubPage(String link) {
        Matcher matcher = subPagePattern.matcher(link);
        if (matcher.matches()) {
            return matcher.group(1);
        } else {
            return link;
        }
    }

    private boolean isNotEmptyOrUseless(String link) {
        return !link.equals("") && !link.equals("/") && !link.equals("#");
    }

    private boolean isValidUrl(String link) {
        return link.contains(".ro")
            && !link.contains("facebook.com")
            && !link.contains("instagram.com")
            && !link.contains("twitter.com")
            && !link.contains("mailto:")
            && !link.contains("whatsapp://")
            && !link.contains("javascript:")
            && !nonWebResourcePattern.matcher(link).matches();
    }

    private boolean isValidWebsite(String link) {
        return this.websitePattern.matcher(link).matches();
    }
}
