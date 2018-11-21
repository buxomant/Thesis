package com.cbp.app.model.response.GoogleSearch;

import java.util.List;

public class Queries {
    private List<Query> previousPage;
    private List<Query> request;
    private List<Query> nextPage;

    public Queries() { }

    public Queries(
        List<Query> previousPage,
        List<Query> request,
        List<Query> nextPage
    ) {
        this.previousPage = previousPage;
        this.request = request;
        this.nextPage = nextPage;
    }

    public List<Query> getPreviousPage() {
        return previousPage;
    }

    public void setPreviousPage(List<Query> previousPage) {
        this.previousPage = previousPage;
    }

    public List<Query> getRequest() {
        return request;
    }

    public void setRequest(List<Query> request) {
        this.request = request;
    }

    public List<Query> getNextPage() {
        return nextPage;
    }

    public void setNextPage(List<Query> nextPage) {
        this.nextPage = nextPage;
    }
}
