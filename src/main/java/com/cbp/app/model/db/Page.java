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
    private String title;

    @Column
    private String content;

    @Column
    private String error;

    @Column
    @NotBlank
    private String url;

    @Column
    @NotNull
    private LocalDateTime discoveredOn;

    @Column
    private LocalDateTime lastCheckedOn;

    @Column
    private LocalDateTime lastProcessedOn;

    @Column
    private Integer lastResponseCode;

    @Column
    private int websiteId;

    public Page() { }

    public Page(@NotBlank String url, @NotNull LocalDateTime discoveredOn, int websiteId) {
        this.url = url;
        this.discoveredOn = discoveredOn;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
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

    public LocalDateTime getLastProcessedOn() {
        return lastProcessedOn;
    }

    public void setLastProcessedOn(LocalDateTime lastProcessedOn) {
        this.lastProcessedOn = lastProcessedOn;
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
