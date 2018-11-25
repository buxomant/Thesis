package com.cbp.app.repository;

import com.cbp.app.model.db.LinksTo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LinksToRepository extends JpaRepository<LinksTo, Integer> {
    Optional<LinksTo> findByWebsiteIdFromAndWebsiteIdTo(int websiteIdFrom, int websiteIdTo);
}
