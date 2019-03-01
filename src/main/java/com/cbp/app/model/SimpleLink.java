package com.cbp.app.model;

import com.cbp.app.service.LinkService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleLink {
    private String linkTitle;
    private String linkUrl;

    public SimpleLink(String linkTitle, String linkUrl) {
        this.linkTitle = linkTitle;

        List<String> link = Collections.singletonList(linkUrl);
        this.linkUrl = link.stream()
            .map(String::trim)
            .map(String::toLowerCase)
            .map(LinkService::trimNonAlphanumericContent)
            .map(LinkService::stripProtocolPrefix)
            .map(LinkService::stripWwwPrefix)
            .map(LinkService::stripAnchorString)
            .map(LinkService::stripQueryString)
            .map(LinkService::stripAsteriskString)
            .map(LinkService::trimNonAlphanumericContent)
            .collect(Collectors.toList())
            .get(0);
    }

    public String getLinkTitle() {
        return linkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
}
