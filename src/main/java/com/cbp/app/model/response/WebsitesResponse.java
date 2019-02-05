package com.cbp.app.model.response;

import java.util.List;

public class WebsitesResponse {
    private List<WebsiteResponse> websites;

    public WebsitesResponse() { }

    public WebsitesResponse(List<WebsiteResponse> websites) {
        this.websites = websites;
    }

    public List<WebsiteResponse> getWebsites() {
        return websites;
    }

    public void setWebsites(List<WebsiteResponse> websites) {
        this.websites = websites;
    }
}
