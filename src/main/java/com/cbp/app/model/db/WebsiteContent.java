package com.cbp.app.model.db;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class WebsiteContent {
    private static final String ID_SEQUENCE = "website_content_content_id_seq";

    @Id
    @SequenceGenerator(name = ID_SEQUENCE, sequenceName = ID_SEQUENCE, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ID_SEQUENCE)
    @Column
    private Integer contentId;

    @Column
    @NotNull
    private Integer websiteId;

    @Column
    private String content;

    @Column
    private LocalDateTime timeFetched;

    @Column
    private LocalDateTime timeProcessed;

    public WebsiteContent() { }

    public WebsiteContent(Integer websiteId, String content) {
        this.websiteId = websiteId;
        this.content = content;
        this.timeFetched = LocalDateTime.now();
    }

    public Integer getContentId() {
        return contentId;
    }

    public void setContentId(Integer contentId) {
        this.contentId = contentId;
    }

    public Integer getWebsiteId() {
        return websiteId;
    }

    public void setWebsiteId(Integer websiteId) {
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
