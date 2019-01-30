package com.cbp.app.model.db;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class WebsiteToWebsite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int linkId;

    @Column
    @NotNull
    private int websiteIdFrom;

    @Column
    @NotNull
    private int websiteIdTo;

    @Column
    @NotNull
    private int contentId;

    public WebsiteToWebsite() { }

    public WebsiteToWebsite(@NotNull int websiteIdFrom, @NotNull int websiteIdTo, @NotNull int contentId) {
        this.websiteIdFrom = websiteIdFrom;
        this.websiteIdTo = websiteIdTo;
        this.contentId = contentId;
    }

    public int getLinkId() {
        return linkId;
    }

    public void setLinkId(int linkId) {
        this.linkId = linkId;
    }

    public int getWebsiteIdFrom() {
        return websiteIdFrom;
    }

    public void setWebsiteIdFrom(int websiteIdFrom) {
        this.websiteIdFrom = websiteIdFrom;
    }

    public int getWebsiteIdTo() {
        return websiteIdTo;
    }

    public void setWebsiteIdTo(int websiteIdTo) {
        this.websiteIdTo = websiteIdTo;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }
}
