package com.cbp.app.model.db;

import javax.persistence.*;

@Entity
public class WebsiteContent {
    @Id
    @Column
    private int websiteId;

    @Column
    private String content;

    public WebsiteContent() { }

    public WebsiteContent(int websiteId, String content) {
        this.websiteId = websiteId;
        this.content = content;
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
}
