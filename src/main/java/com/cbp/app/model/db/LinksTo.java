package com.cbp.app.model.db;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class LinksTo {
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

    public LinksTo() { }

    public LinksTo(@NotNull int websiteIdFrom, @NotNull int websiteIdTo) {
        this.websiteIdFrom = websiteIdFrom;
        this.websiteIdTo = websiteIdTo;
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
}
