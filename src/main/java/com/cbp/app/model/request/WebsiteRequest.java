package com.cbp.app.model.request;

import com.cbp.app.model.enumType.WebsiteContentType;
import com.cbp.app.model.enumType.WebsiteType;

public class WebsiteRequest {
    private String url;
    private WebsiteType type;
    private WebsiteContentType contentType;
    private Integer fetchEveryNumberOfHours;

    public WebsiteRequest() { }

    public WebsiteRequest(String url, WebsiteType type, WebsiteContentType contentType, Integer fetchEveryNumberOfHours) {
        this.url = url;
        this.type = type;
        this.contentType = contentType;
        this.fetchEveryNumberOfHours = fetchEveryNumberOfHours;
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
