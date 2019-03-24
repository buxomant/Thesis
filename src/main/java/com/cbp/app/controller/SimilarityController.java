package com.cbp.app.controller;

import com.cbp.app.model.response.PageSimilaritiesResponse;
import com.cbp.app.model.response.PageSimilarityResponse;
import com.cbp.app.repository.PageSimilarityRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static com.cbp.app.service.LinkService.urlToTopDomain;

@CrossOrigin
@RestController
public class SimilarityController {
    private final PageSimilarityRepository pageSimilarityRepository;

    public SimilarityController(PageSimilarityRepository pageSimilarityRepository) {
        this.pageSimilarityRepository = pageSimilarityRepository;
    }

    @RequestMapping(value = "/similar-pages", method = RequestMethod.GET)
    public PageSimilaritiesResponse getSimilarPages() {
        List<PageSimilarityResponse> pageSimilarities = pageSimilarityRepository.getLatestPageSimilarities();
        pageSimilarities.forEach(pageSimilarity -> {
            pageSimilarity.setFirstWebsiteUrl(urlToTopDomain(pageSimilarity.getFirstWebsiteUrl()));
            pageSimilarity.setSecondWebsiteUrl(urlToTopDomain(pageSimilarity.getSecondWebsiteUrl()));
        });

        List<PageSimilarityResponse> filteredPageSimilarities = pageSimilarities.stream()
            .filter(pageSimilarity -> pageSimilarities.stream()
                .filter(ps -> ps.getFirstPageId() == pageSimilarity.getFirstPageId()).count() < 10)
            .filter(pageSimilarity -> pageSimilarities.stream()
                .filter(ps -> ps.getSecondPageId() == pageSimilarity.getSecondPageId()).count() < 10)
            .collect(Collectors.toList());

        return new PageSimilaritiesResponse(filteredPageSimilarities);
    }
}
