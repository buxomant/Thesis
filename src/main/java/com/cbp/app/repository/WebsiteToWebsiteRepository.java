package com.cbp.app.repository;

import com.cbp.app.model.db.WebsiteToWebsite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WebsiteToWebsiteRepository extends JpaRepository<WebsiteToWebsite, Integer> {
    Optional<WebsiteToWebsite> findByWebsiteIdFromAndWebsiteIdToAndContentId(int websiteIdFrom, int websiteIdTo, int contentId);

    void deleteAllByWebsiteIdFrom(int websiteIdFrom);

    List<WebsiteToWebsite> findAllByWebsiteIdTo(int websiteIdTo);

    @Query(value = "SELECT COUNT(*) FROM website_to_website", nativeQuery = true)
    Integer getNumberOfWebsiteToWebsiteLinks();
}
