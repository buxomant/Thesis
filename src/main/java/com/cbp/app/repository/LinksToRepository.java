package com.cbp.app.repository;

import com.cbp.app.model.db.LinksTo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LinksToRepository extends JpaRepository<LinksTo, Integer> {
    Optional<LinksTo> findByWebsiteIdFromAndWebsiteIdTo(int websiteIdFrom, int websiteIdTo);

    @Query(value = "SELECT COUNT(*) FROM links_to", nativeQuery = true)
    Integer getNumberOfLinks();
}
