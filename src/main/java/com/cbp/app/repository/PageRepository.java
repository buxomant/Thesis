package com.cbp.app.repository;

import com.cbp.app.model.db.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PageRepository extends JpaRepository<Page, Integer> {
    Optional<Page> findByUrl(String url);

    List<Page> findAllByUrlIn(List<String> urls);

    List<Page> findAllByUrlOrderByPageId(String url);

    void deleteAllByWebsiteId(Integer websiteId);

    @Query(value = "SELECT COUNT(*) FROM page", nativeQuery = true)
    Integer getNumberOfPages();
}
