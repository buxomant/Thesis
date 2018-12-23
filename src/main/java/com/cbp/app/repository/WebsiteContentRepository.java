package com.cbp.app.repository;

import com.cbp.app.model.db.WebsiteContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebsiteContentRepository extends JpaRepository<WebsiteContent, Integer> {
}
