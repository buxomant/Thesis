package com.cbp.app.model.response.GoogleSearch;

public class Item {
    private String title;
    private String link;
    private String displayLink;

    public Item() { }

    public Item(String title, String link, String displayLink) {
        this.title = title;
        this.link = link;
        this.displayLink = displayLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDisplayLink() {
        return displayLink;
    }

    public void setDisplayLink(String displayLink) {
        this.displayLink = displayLink;
    }
}
