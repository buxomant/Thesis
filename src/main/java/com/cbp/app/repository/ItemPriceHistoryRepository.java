package com.cbp.app.repository;

import com.cbp.app.model.db.ItemPriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemPriceHistoryRepository extends JpaRepository<ItemPriceHistory, String> {
}
