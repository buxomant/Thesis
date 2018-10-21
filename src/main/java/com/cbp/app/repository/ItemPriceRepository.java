package com.cbp.app.repository;

import com.cbp.app.model.db.Item;
import com.cbp.app.model.db.ItemPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPriceRepository extends JpaRepository<ItemPrice, Integer> {
    List<ItemPrice> findAllByItem(Item item);
}