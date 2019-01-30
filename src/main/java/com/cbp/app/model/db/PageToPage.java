package com.cbp.app.model.db;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class PageToPage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int linkId;

    @Column
    @NotNull
    private int pageIdFrom;

    @Column
    @NotNull
    private int pageIdTo;

    @Column
    @NotNull
    private int contentId;

    public PageToPage() { }

    public PageToPage(@NotNull int pageIdFrom, @NotNull int pageIdTo, @NotNull int contentId) {
        this.pageIdFrom = pageIdFrom;
        this.pageIdTo = pageIdTo;
        this.contentId = contentId;
    }

    public int getLinkId() {
        return linkId;
    }

    public void setLinkId(int linkId) {
        this.linkId = linkId;
    }

    public int getPageIdFrom() {
        return pageIdFrom;
    }

    public void setPageIdFrom(int pageIdFrom) {
        this.pageIdFrom = pageIdFrom;
    }

    public int getPageIdTo() {
        return pageIdTo;
    }

    public void setPageIdTo(int pageIdTo) {
        this.pageIdTo = pageIdTo;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }
}
