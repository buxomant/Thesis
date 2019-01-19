package com.cbp.app.model.db;

import com.cbp.app.model.enumType.WebsiteContentType;
import com.cbp.app.model.enumType.WebsiteType;

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
    @Enumerated(EnumType.STRING)
    private WebsiteType type;

    @Column
    @Enumerated(EnumType.STRING)
    private WebsiteContentType contentType;

    @Column
    private Integer fetchEveryNumberOfHours;

    private static final Integer DEFAULT_FETCH_EVERY_NUMBER_OF_HOURS = 8760;

    public Website() { }

    public Website(String title, @NotBlank String url, @NotBlank LocalDateTime discoveredOn) {
        this.title = title;
        this.url = url;
        this.discoveredOn = discoveredOn;
        this.fetchEveryNumberOfHours = DEFAULT_FETCH_EVERY_NUMBER_OF_HOURS;
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
