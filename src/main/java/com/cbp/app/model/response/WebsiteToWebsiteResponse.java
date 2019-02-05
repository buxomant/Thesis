package com.cbp.app.model.response;

public class WebsiteToWebsiteResponse {
    private Integer from;
    private Integer to;

    public WebsiteToWebsiteResponse() { }

    public WebsiteToWebsiteResponse(Integer from, Integer to) {
        this.from = from;
        this.to = to;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }
}
