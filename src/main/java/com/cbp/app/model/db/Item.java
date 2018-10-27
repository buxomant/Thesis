package com.cbp.app.model.db;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int itemId;

    @Column
    @NotBlank
    private String name;

    @Column
    @NotBlank
    private String url;

    public Item() { }

    public Item(@NotBlank String name, @NotBlank String url) {
        this.name = name;
        this.url = url;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
