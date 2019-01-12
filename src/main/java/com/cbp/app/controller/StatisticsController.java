package com.cbp.app.controller;

import com.cbp.app.model.response.CrawlerStatisticsResponse;
import com.cbp.app.model.response.WebsiteStatisticsResponse;
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

    @RequestMapping(value = "/crawler-statistics", method = RequestMethod.GET)
    public CrawlerStatisticsResponse getCrawlerStatistics() {
        Integer numberOfWebsites = websiteRepository.getNumberOfWebsites();
        Integer numberOfCheckedWebsites = websiteRepository.getNumberOfCheckedWebsites();
        Integer numberOfProcessedWebsites = websiteRepository.getNumberOfProcessedWebsites();
        Integer numberOfWebsitesWithErrors = websiteRepository.getNumberOfWebsitesWithErrors();
        Integer numberOfDuplicateWebsites = websiteRepository.getNumberOfDuplicateWebsites();
        Integer numberOfPages = pageRepository.getNumberOfPages();
        Integer numberOfLinks = linksToRepository.getNumberOfLinks();

        return new CrawlerStatisticsResponse(
            numberOfWebsites,
            numberOfCheckedWebsites,
            numberOfProcessedWebsites,
            numberOfWebsitesWithErrors,
            numberOfDuplicateWebsites,
            numberOfPages,
            numberOfLinks
        );
    }

    @RequestMapping(value = "/website-statistics", method = RequestMethod.GET)
    public WebsiteStatisticsResponse getWebsiteStatistics() {
        Integer numberOfDomesticWebsites = websiteRepository.getNumberOfDomesticWebsites();
        Integer numberOfForeignWebsites = websiteRepository.getNumberOfForeignWebsites();
        Integer numberOfRedirectToForeignWebsites = websiteRepository.getNumberOfRedirectToForeignWebsites();
        Integer numberOfIndexingServiceWebsites = websiteRepository.getNumberOfIndexingServiceWebsites();
        Integer numberOfNewsWebsites = websiteRepository.getNumberOfNewsWebsites();
        Integer numberOfSocialMediaWebsites = websiteRepository.getNumberOfSocialMediaWebsites();
        Integer numberOfUncategorizedWebsites = websiteRepository.getNumberOfUncategorizedWebsites();
        Integer numberOfTopDomains = websiteRepository.getNumberOfTopDomains();
        Integer numberOfSubDomains = websiteRepository.getNumberOfSubDomains();

        return new WebsiteStatisticsResponse(
            numberOfDomesticWebsites,
            numberOfForeignWebsites,
            numberOfRedirectToForeignWebsites,
            numberOfIndexingServiceWebsites,
            numberOfNewsWebsites,
            numberOfSocialMediaWebsites,
            numberOfUncategorizedWebsites,
            numberOfTopDomains,
            numberOfSubDomains
        );
    }
}
