package com.cbp.app.model.db;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class WebsiteContent {
    @Id
    @Column
    private int websiteId;

    @Column
    private String content;

    @Column
    private LocalDateTime timeFetched;

    @Column
    private LocalDateTime timeProcessed;

    public WebsiteContent() { }

    public WebsiteContent(int websiteId, String content) {
        this.websiteId = websiteId;
        this.content = content;
        this.timeFetched = LocalDateTime.now();
    }

    public int getWebsiteId() {
        return websiteId;
    }

    public void setWebsiteId(int websiteId) {
        this.websiteId = websiteId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimeFetched() {
        return timeFetched;
    }

    public void setTimeFetched(LocalDateTime timeFetched) {
        this.timeFetched = timeFetched;
    }

    public LocalDateTime getTimeProcessed() {
        return timeProcessed;
    }

    public void setTimeProcessed(LocalDateTime timeProcessed) {
        this.timeProcessed = timeProcessed;
    }
}
