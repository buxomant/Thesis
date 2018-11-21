package com.cbp.app.model.response.GoogleSearch;

import java.util.List;

public class GoogleSearchResponse {
    private Queries queries;
    private List<Item> items;

    public GoogleSearchResponse() { }

    public GoogleSearchResponse(Queries queries, List<Item> items) {
        this.queries = queries;
        this.items = items;
    }

    public Queries getQueries() {
        return queries;
    }

    public void setQueries(Queries queries) {
        this.queries = queries;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
