package com.cbp.app.model.response;

import java.util.List;

public class PageSimilaritiesResponse {
    private List<PageSimilarityResponse> pageSimilarities;

    public PageSimilaritiesResponse() { }

    public PageSimilaritiesResponse(List<PageSimilarityResponse> pageSimilarities) {
        this.pageSimilarities = pageSimilarities;
    }

    public List<PageSimilarityResponse> getPageSimilarities() {
        return pageSimilarities;
    }

    public void setPageSimilarities(List<PageSimilarityResponse> pageSimilarities) {
        this.pageSimilarities = pageSimilarities;
    }
}
