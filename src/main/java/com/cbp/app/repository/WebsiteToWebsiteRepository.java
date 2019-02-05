package com.cbp.app.repository;

import com.cbp.app.model.db.WebsiteToWebsite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WebsiteToWebsiteRepository extends JpaRepository<WebsiteToWebsite, Integer> {
    Optional<WebsiteToWebsite> findByWebsiteIdFromAndWebsiteIdToAndContentId(int websiteIdFrom, int websiteIdTo, int contentId);

    void deleteAllByWebsiteIdFrom(int websiteIdFrom);

    List<WebsiteToWebsite> findAllByWebsiteIdTo(int websiteIdTo);

    @Query(value = "SELECT DISTINCT * FROM website_to_website wtw " +
        " WHERE website_id_from IN (SELECT website_id FROM website WHERE type = :websiteType AND content_type = :websiteContentType)" +
        " AND content_id IN (SELECT MAX(content_id) FROM website_to_website GROUP BY website_id_from)", nativeQuery = true)
    List<WebsiteToWebsite> findAllByWebsiteTypeAndContentTypeForLatestContentId(
        @Param("websiteType") String websiteType,
        @Param("websiteContentType") String websiteContentType
    );

    @Query(value = "SELECT COUNT(*) FROM website_to_website", nativeQuery = true)
    Integer getNumberOfWebsiteToWebsiteLinks();
}
