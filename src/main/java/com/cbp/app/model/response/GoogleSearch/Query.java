package com.cbp.app.model.response.GoogleSearch;

public class Query {
    private int startIndex;

    public Query() { }

    public Query(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }
}
