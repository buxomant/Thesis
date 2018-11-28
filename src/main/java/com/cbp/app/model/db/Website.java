package com.cbp.app.model.db;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Website {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int websiteId;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private String error;

    @Column
    private boolean redirectsToExternal;

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

    public Website() { }

    public Website(String title, @NotBlank String url, @NotBlank LocalDateTime discoveredOn) {
        this.title = title;
        this.url = url;
        this.discoveredOn = discoveredOn;
    }

    public int getWebsiteId() {
        return websiteId;
    }

    public void setWebsiteId(int websiteId) {
        this.websiteId = websiteId;
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

    public boolean getRedirectsToExternal() {
        return redirectsToExternal;
    }

    public void setRedirectsToExternal(boolean redirectsToExternal) {
        this.redirectsToExternal = redirectsToExternal;
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

    public LocalDateTime getLastProcessedOn() {
        return lastProcessedOn;
    }

    public void setLastProcessedOn(LocalDateTime lastProcessedOn) {
        this.lastProcessedOn = lastProcessedOn;
    }

    public Integer getLastResponseCode() {
        return lastResponseCode;
    }

    public void setLastResponseCode(Integer lastResponseCode) {
        this.lastResponseCode = lastResponseCode;
    }
}
