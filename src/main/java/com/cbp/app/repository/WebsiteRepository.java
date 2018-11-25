package com.cbp.app.repository;

import com.cbp.app.model.db.Website;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WebsiteRepository extends JpaRepository<Website, Integer> {
    Optional<Website> findByUrl(String url);

    @Query(value = "SELECT * FROM website " +
        "WHERE last_checked_on IS NULL " +
        "ORDER BY discovered_on ASC LIMIT 1", nativeQuery = true)
    Optional<Website> getNextUncheckedWebsite();
}
