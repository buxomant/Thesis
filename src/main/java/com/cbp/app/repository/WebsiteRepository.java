package com.cbp.app.repository;

import com.cbp.app.model.db.Website;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WebsiteRepository extends JpaRepository<Website, Integer> {
    Optional<Website> findByUrl(String url);

    List<Website> findAllByUrlOrderByWebsiteId(String url);

    @Query(value = "SELECT * FROM website " +
        "WHERE last_checked_on IS NULL " +
        "ORDER BY discovered_on ASC LIMIT 1", nativeQuery = true)
    Optional<Website> getNextUncheckedWebsite();

    @Query(value = "SELECT * FROM website " +
            "WHERE content IS NOT NULL AND last_processed_on IS NULL " +
            "ORDER BY discovered_on ASC LIMIT 1", nativeQuery = true)
    Optional<Website> getNextUnprocessedWebsite();

    @Query(value = "SELECT url FROM website" +
            " GROUP BY url" +
            " HAVING COUNT(url) > 1" +
            " ORDER BY COUNT(url) DESC LIMIT 1", nativeQuery = true)
    Optional<String> getNextDuplicateWebsiteUrl();

    @Query(value = "SELECT COUNT(*) FROM website", nativeQuery = true)
    Integer getNumberOfWebsites();

    @Query(value = "SELECT COUNT(*) FROM website WHERE last_checked_on IS NOT NULL", nativeQuery = true)
    Integer getNumberOfCheckedWebsites();

    @Query(value = "SELECT COUNT(*) FROM website WHERE last_processed_on IS NOT NULL", nativeQuery = true)
    Integer getNumberOfProcessedWebsites();

    @Query(value = "SELECT COUNT(*) FROM website WHERE error IS NOT NULL", nativeQuery = true)
    Integer getNumberOfWebsitesWithErrors();
}
