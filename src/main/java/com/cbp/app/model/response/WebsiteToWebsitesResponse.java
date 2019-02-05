package com.cbp.app.model.response;

import java.util.List;

public class WebsiteToWebsitesResponse {
    private List<WebsiteToWebsiteResponse> websiteToWebsites;

    public WebsiteToWebsitesResponse() { }

    public WebsiteToWebsitesResponse(List<WebsiteToWebsiteResponse> websiteToWebsites) {
        this.websiteToWebsites = websiteToWebsites;
    }

    public List<WebsiteToWebsiteResponse> getWebsiteToWebsites() {
        return websiteToWebsites;
    }

    public void setWebsiteToWebsites(List<WebsiteToWebsiteResponse> websiteToWebsites) {
        this.websiteToWebsites = websiteToWebsites;
    }
}
