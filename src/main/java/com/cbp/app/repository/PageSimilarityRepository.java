package com.cbp.app.repository;

import com.cbp.app.model.response.PageSimilarityResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageSimilarityRepository extends JpaRepository<PageSimilarityResponse, Integer> {
    @Query(value = "SELECT similarity_id, similarity_coefficient, " +
        "  p1.page_id AS \"first_page_id\", " +
        "  p2.page_id AS \"second_page_id\", " +
        "  p1.url AS \"first_page_url\", " +
        "  p2.url AS \"second_page_url\", " +
        "  w1.url AS \"first_website_url\", " +
        "  w2.url AS \"second_website_url\" " +
        "FROM text_similarity ts " +
        "  JOIN page p1 ON p1.page_id = first_id " +
        "  JOIN page p2 ON p2.page_id = second_id " +
        "  JOIN website w1 ON p1.website_id = w1.website_id " +
        "  JOIN website w2 ON p2.website_id = w2.website_id " +
        "WHERE first_type = 'page' AND second_type = 'page' " +
        "  AND p1.url LIKE '%/%' " +
        "  AND p2.url LIKE '%/%' " +
        "  AND time_frame = (SELECT MAX(time_frame) FROM text_similarity WHERE first_type = 'page' AND second_type = 'page') " +
        "  AND length(p1.url) / length(p2.url) BETWEEN 0.5 AND 1.5", nativeQuery = true)
    public List<PageSimilarityResponse> getLatestPageSimilarities();
}
