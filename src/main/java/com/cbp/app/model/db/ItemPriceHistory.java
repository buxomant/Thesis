package com.cbp.app.model.db;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class ItemPriceHistory {
    @Id
    private int id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "item_id")
    private Item item;

    @Column
    @NotNull
    private Float price;

    @Column
    private LocalDateTime timeChecked;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
