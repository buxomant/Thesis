package com.cbp.app.repository;

import com.cbp.app.model.db.WebsiteTextSimilarity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebsiteTextSimilarityRepository extends JpaRepository<WebsiteTextSimilarity, Integer> {
}
