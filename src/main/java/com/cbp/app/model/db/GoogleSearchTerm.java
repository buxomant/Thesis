package com.cbp.app.model.db;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class GoogleSearchTerm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int termId;

    @Column
    @NotBlank
    private String term;

    public GoogleSearchTerm() { }

    public GoogleSearchTerm(@NotBlank String term) {
        this.term = term;
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}
