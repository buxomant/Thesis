package com.cbp.app.repository;

import com.cbp.app.model.db.ItemPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemPriceRepository extends JpaRepository<ItemPrice, Integer> {
    List<ItemPrice> findAllByItemId(int itemId);

    Optional<ItemPrice> findFirstByItemIdOrderByTimeCheckedDesc(int itemId);

    void deleteAllByItemId(int itemId);
}
