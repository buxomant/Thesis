package com.cbp.app.scheduled;

import com.cbp.app.helper.LoggingHelper;
import com.cbp.app.model.db.Website;
import com.cbp.app.repository.WebsiteRepository;
import com.cbp.app.service.ScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class WebsiteScraperScheduler {

    private final WebsiteRepository websiteRepository;
    private final ScraperService scraperService;
    private final boolean fetchWebsitesJobEnabled;
    private final boolean processWebsitesJobEnabled;
    private final boolean fixDuplicateWebsitesJobEnabled;
    private final boolean establishSubdomainRelationshipsJobEnabled;

    @Autowired
    public WebsiteScraperScheduler(
        WebsiteRepository websiteRepository,
        ScraperService scraperService,
        @Value("${fetch-websites-scheduler.enabled}") boolean fetchWebsitesJobEnabled,
        @Value("${process-websites-scheduler.enabled}") boolean processWebsitesJobEnabled,
        @Value("${fix-duplicate-websites-scheduler.enabled}") boolean fixDuplicateWebsitesJobEnabled,
        @Value("${establish-subdomain-relationships.enabled}") boolean establishSubdomainRelationshipsJobEnabled
    ) {
        this.websiteRepository = websiteRepository;
        this.scraperService = scraperService;
        this.fetchWebsitesJobEnabled = fetchWebsitesJobEnabled;
        this.processWebsitesJobEnabled = processWebsitesJobEnabled;
        this.fixDuplicateWebsitesJobEnabled = fixDuplicateWebsitesJobEnabled;
        this.establishSubdomainRelationshipsJobEnabled = establishSubdomainRelationshipsJobEnabled;
    }

    @Scheduled(fixedRate = 100000) // qq change back
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void fetchWebsitesContent() throws IOException {
        if (fetchWebsitesJobEnabled) {
            Optional<Website> nextUncheckedWebsite = websiteRepository.getNextDomesticWebsiteThatNeedsFetching();
            nextUncheckedWebsite.ifPresent(scraperService::fetchWebsiteContent);
        }
    }

    @Scheduled(fixedRate = 100)
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void processWebsites() {
        if (processWebsitesJobEnabled) {
            Optional<Website> nextUnprocessedWebsite = websiteRepository.getNextDomesticWebsiteThatNeedsProcessing();
            nextUnprocessedWebsite.ifPresent(scraperService::processWebsite);
        }
    }

    @Scheduled(fixedRate = 1000)
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void fixDuplicateWebsites() {
        if (fixDuplicateWebsitesJobEnabled) {
            Optional<String> nextDuplicateWebsiteUrl = websiteRepository.getNextDuplicateWebsiteUrl();
            if (nextDuplicateWebsiteUrl.isPresent()) {
                List<Website> websitesMatchingUrl = websiteRepository.findAllByUrlOrderByWebsiteId(nextDuplicateWebsiteUrl.get());
                scraperService.fixDuplicateWebsite(websitesMatchingUrl);
            }
        }
    }

    @Scheduled(fixedRate = 10000)
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void establishSubdomainRelationships() {
        if (establishSubdomainRelationshipsJobEnabled) {
            Optional<Website> nextWebsite = websiteRepository.getNextWebsiteNotMarkedAsDomainOrSubdomain();
            nextWebsite.ifPresent(scraperService::establishSubdomainRelationshipsForWebsite);
        }
    }
}
