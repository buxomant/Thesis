package com.cbp.app.controller;

import com.cbp.app.model.response.StatisticsResponse;
import com.cbp.app.repository.LinksToRepository;
import com.cbp.app.repository.PageRepository;
import com.cbp.app.repository.WebsiteRepository;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class StatisticsController {
    private final WebsiteRepository websiteRepository;
    private final PageRepository pageRepository;
    private final LinksToRepository linksToRepository;

    public StatisticsController(
        WebsiteRepository websiteRepository,
        PageRepository pageRepository,
        LinksToRepository linksToRepository
    ) {
        this.websiteRepository = websiteRepository;
        this.pageRepository = pageRepository;
        this.linksToRepository = linksToRepository;
    }

    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    public StatisticsResponse getStatistics() {
        Integer numberOfWebsites = websiteRepository.getNumberOfWebsites();
        Integer numberOfCheckedWebsites = websiteRepository.getNumberOfCheckedWebsites();
        Integer numberOfProcessedWebsites = websiteRepository.getNumberOfProcessedWebsites();
        Integer numberOfWebsitesWithErrors = websiteRepository.getNumberOfWebsitesWithErrors();
        Integer numberOfPages = pageRepository.getNumberOfPages();
        Integer numberOfLinks = linksToRepository.getNumberOfLinks();

        return new StatisticsResponse(
            numberOfWebsites,
            numberOfCheckedWebsites,
            numberOfProcessedWebsites,
            numberOfWebsitesWithErrors,
            numberOfPages,
            numberOfLinks
        );
    }
}
