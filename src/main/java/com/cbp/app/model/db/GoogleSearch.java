package com.cbp.app.model.db;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class GoogleSearch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int searchId;

    @Column
    @NotNull
    private int termId;

    @Column
    @NotNull
    private int startIndex;

    @Column
    private int nextStartIndex;

    @Column
    private LocalDateTime seenOn;

    public GoogleSearch() { }

    public GoogleSearch(
        @NotNull int startIndex,
        @NotNull int termId,
        int nextStartIndex,
        LocalDateTime seenOn
    ) {
        this.startIndex = startIndex;
        this.termId = termId;
        this.nextStartIndex = nextStartIndex;
        this.seenOn = seenOn;
    }

    public GoogleSearch(@NotNull int startIndex, @NotNull int termId, LocalDateTime seenOn) {
        this.startIndex = startIndex;
        this.termId = termId;
        this.seenOn = seenOn;
    }

    public int getSearchId() {
        return searchId;
    }

    public void setSearchId(int searchId) {
        this.searchId = searchId;
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getNextStartIndex() {
        return nextStartIndex;
    }

    public void setNextStartIndex(int nextStartIndex) {
        this.nextStartIndex = nextStartIndex;
    }

    public LocalDateTime getSeenOn() {
        return seenOn;
    }

    public void setSeenOn(LocalDateTime seenOn) {
        this.seenOn = seenOn;
    }
}
