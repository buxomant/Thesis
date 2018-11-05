package com.cbp.app.repository;

import com.cbp.app.model.db.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    @Query(value = "SELECT item.* FROM item " +
            "LEFT JOIN item_price USING (item_id) " +
            "GROUP BY (item_id) " +
            "ORDER BY coalesce(MAX(time_checked), '1900-01-01'), item_id ASC", nativeQuery = true)
    List<Item> findAllItemsThatNeedPriceCheckingInOrder();
}
