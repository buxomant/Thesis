package com.cbp.app.model.db;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int pageId;

    @Column
    @NotBlank
    private String title;

    @Column
    @NotBlank
    private String url;

    @Column
    @NotNull
    private LocalDateTime discoveredOn;

    @Column
    private LocalDateTime lastCheckedOn;

    @Column
    private Integer lastResponseCode;

    @Column
    private int websiteId;

    public Page() { }

    public Page(
        @NotBlank String title,
        @NotBlank String url,
        @NotNull LocalDateTime discoveredOn,
        LocalDateTime lastCheckedOn,
        Integer lastResponseCode,
        int websiteId
    ) {
        this.title = title;
        this.url = url;
        this.discoveredOn = discoveredOn;
        this.lastCheckedOn = lastCheckedOn;
        this.lastResponseCode = lastResponseCode;
        this.websiteId = websiteId;
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getDiscoveredOn() {
        return discoveredOn;
    }

    public void setDiscoveredOn(LocalDateTime discoveredOn) {
        this.discoveredOn = discoveredOn;
    }

    public LocalDateTime getLastCheckedOn() {
        return lastCheckedOn;
    }

    public void setLastCheckedOn(LocalDateTime lastCheckedOn) {
        this.lastCheckedOn = lastCheckedOn;
    }

    public Integer getLastResponseCode() {
        return lastResponseCode;
    }

    public void setLastResponseCode(Integer lastResponseCode) {
        this.lastResponseCode = lastResponseCode;
    }

    public int getWebsiteId() {
        return websiteId;
    }

    public void setWebsiteId(int websiteId) {
        this.websiteId = websiteId;
    }
}
