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
    @NotBlank
    private String url;

    @Column
    @NotNull
    private LocalDateTime discoveredOn;

    @Column
    private LocalDateTime lastCheckedOn;

    @Column
    private Integer lastResponseCode;

    public Website() { }

    public Website(String title, @NotBlank String url, @NotBlank LocalDateTime discoveredOn) {
        this.title = title;
        this.url = url;
        this.discoveredOn = discoveredOn;
    }

    public Website(
        String title,
        @NotBlank String url,
        @NotBlank LocalDateTime discoveredOn,
        LocalDateTime lastCheckedOn,
        Integer lastResponseCode
    ) {
        this.title = title;
        this.url = url;
        this.discoveredOn = discoveredOn;
        this.lastCheckedOn = lastCheckedOn;
        this.lastResponseCode = lastResponseCode;
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
}
