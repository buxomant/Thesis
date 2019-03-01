package com.cbp.app.repository;

import com.cbp.app.model.db.TextSimilarity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextSimilarityRepository extends JpaRepository<TextSimilarity, Integer> {
}
