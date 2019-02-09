package com.cbp.app.service;

import com.cbp.app.model.db.Page;
import com.cbp.app.model.db.Website;
import com.cbp.app.model.enumType.WebsiteContentType;
import com.cbp.app.model.enumType.WebsiteType;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WebsiteService {
    public static Website createNewWebsite(String websiteUrl) {
        Website website = new Website(null, websiteUrl, LocalDateTime.now());
        WebsiteType websiteType = getWebsiteType(websiteUrl, websiteUrl);
        WebsiteContentType websiteContentType = getWebsiteContentType(websiteUrl);
        website.setType(websiteType);
        website.setContentType(websiteContentType);
        return website;
    }

    public static Page createNewPageForWebsiteHomePage(Website website) {
        return new Page(website.getUrl(), LocalDateTime.now(), website.getWebsiteId());
    }

    public static WebsiteType getWebsiteType(String storedUrl, String actualUrl) {
        if (RegexPatternService.indexingServicePattern.matcher(actualUrl).matches()) {
            return WebsiteType.INDEXING_SERVICE;
        }
        if (isDomesticWebsite(storedUrl) && !isDomesticWebsite(actualUrl)) {
            return WebsiteType.REDIRECT;
        }
        if (isDomesticWebsite(actualUrl)) {
            return WebsiteType.DOMESTIC;
        } else {
            return WebsiteType.FOREIGN;
        }
    }

    public static WebsiteContentType getWebsiteContentType(String url) {
        if (RegexPatternService.socialMediaWebsitePattern.matcher(url).matches()) {
            return WebsiteContentType.SOCIAL_MEDIA;
        }
        if (RegexPatternService.domesticNewsWebsitePattern.matcher(url).matches()) {
            return WebsiteContentType.NEWS;
        }
        return WebsiteContentType.UNCATEGORIZED;
    }

    private static boolean isDomesticWebsite(String url) {
        return RegexPatternService.domesticWebsitePattern.matcher(url).matches()
            || RegexPatternService.domesticNewsWebsitePattern.matcher(url).matches();
    }
}
