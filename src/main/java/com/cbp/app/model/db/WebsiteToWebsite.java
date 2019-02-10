package com.cbp.app.model.db;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class WebsiteToWebsite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer linkId;

    @Column
    @NotNull
    private Integer websiteIdFrom;

    @Column
    @NotNull
    private Integer websiteIdTo;

    @Column
    @NotNull
    private Integer contentId;

    @Column
    private String title;

    public WebsiteToWebsite() { }

    public WebsiteToWebsite(@NotNull Integer websiteIdFrom, @NotNull Integer websiteIdTo, @NotNull Integer contentId, String title) {
        this.websiteIdFrom = websiteIdFrom;
        this.websiteIdTo = websiteIdTo;
        this.contentId = contentId;
        this.title = title;
    }

    public Integer getLinkId() {
        return linkId;
    }

    public void setLinkId(Integer linkId) {
        this.linkId = linkId;
    }

    public Integer getWebsiteIdFrom() {
        return websiteIdFrom;
    }

    public void setWebsiteIdFrom(Integer websiteIdFrom) {
        this.websiteIdFrom = websiteIdFrom;
    }

    public Integer getWebsiteIdTo() {
        return websiteIdTo;
    }

    public void setWebsiteIdTo(Integer websiteIdTo) {
        this.websiteIdTo = websiteIdTo;
    }

    public Integer getContentId() {
        return contentId;
    }

    public void setContentId(Integer contentId) {
        this.contentId = contentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
