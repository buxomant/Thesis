package com.cbp.app.repository;

import com.cbp.app.model.db.PageToPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PageToPageRepository extends JpaRepository<PageToPage, Integer> {
    Optional<PageToPage> findByPageIdFromAndPageIdToAndContentId(int pageIdFrom, int pageIdTo, int contentId);

    void deleteAllByPageIdFrom(int pageIdFrom);

    void deleteAllByPageIdTo(int pageIdTo);

    List<PageToPage> findAllByPageIdTo(int pageIdTo);

    @Query(value = "SELECT COUNT(*) FROM page_to_page", nativeQuery = true)
    Integer getNumberOfPageToPageLinks();
}
