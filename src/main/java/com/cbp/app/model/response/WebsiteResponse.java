package com.cbp.app.model.response;

import com.cbp.app.model.db.Website;
import com.cbp.app.model.enumType.WebsiteContentType;
import com.cbp.app.model.enumType.WebsiteType;

public class WebsiteResponse {
    private Integer websiteId;
    private String url;
    private WebsiteType type;
    private WebsiteContentType contentType;
    private Integer fetchEveryNumberOfHours;

    public WebsiteResponse() { }

    public WebsiteResponse(Website website) {
        this.websiteId = website.getWebsiteId();
        this.url = website.getUrl();
        this.type = website.getType();
        this.contentType = website.getContentType();
        this.fetchEveryNumberOfHours = website.getFetchEveryNumberOfHours();
    }

    public Integer getWebsiteId() {
        return websiteId;
    }

    public void setWebsiteId(Integer websiteId) {
        this.websiteId = websiteId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public WebsiteType getType() {
        return type;
    }

    public void setType(WebsiteType type) {
        this.type = type;
    }

    public WebsiteContentType getContentType() {
        return contentType;
    }

    public void setContentType(WebsiteContentType contentType) {
        this.contentType = contentType;
    }

    public Integer getFetchEveryNumberOfHours() {
        return fetchEveryNumberOfHours;
    }

    public void setFetchEveryNumberOfHours(Integer fetchEveryNumberOfHours) {
        this.fetchEveryNumberOfHours = fetchEveryNumberOfHours;
    }
}
