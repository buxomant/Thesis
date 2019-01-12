package com.cbp.app.model.db;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class SubdomainOf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int subdomainRelationId;

    @Column
    @NotNull
    private int websiteIdParent;

    @Column
    @NotNull
    private int websiteIdChild;

    public SubdomainOf() { }

    public SubdomainOf(@NotNull int websiteIdParent, @NotNull int websiteIdChild) {
        this.websiteIdParent = websiteIdParent;
        this.websiteIdChild = websiteIdChild;
    }

    public int getSubdomainRelationId() {
        return subdomainRelationId;
    }

    public void setSubdomainRelationId(int subdomainRelationId) {
        this.subdomainRelationId = subdomainRelationId;
    }

    public int getWebsiteIdParent() {
        return websiteIdParent;
    }

    public void setWebsiteIdParent(int websiteIdParent) {
        this.websiteIdParent = websiteIdParent;
    }

    public int getWebsiteIdChild() {
        return websiteIdChild;
    }

    public void setWebsiteIdChild(int websiteIdChild) {
        this.websiteIdChild = websiteIdChild;
    }
}
