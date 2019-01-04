package com.cbp.app.scheduled;

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
import java.util.Optional;

@Component
public class WebsiteScraperScheduler {

    private final WebsiteRepository websiteRepository;
    private final ScraperService scraperService;
    private final boolean fetchWebsitesJobEnabled;
    private final boolean processWebsitesJobEnabled;
    private final boolean fixDuplicateWebsitesJobEnabled;

    @Autowired
    public WebsiteScraperScheduler(
        WebsiteRepository websiteRepository,
        ScraperService scraperService,
        @Value("${fetch-websites-scheduler.enabled}") boolean fetchWebsitesJobEnabled,
        @Value("${process-websites-scheduler.enabled}") boolean processWebsitesJobEnabled,
        @Value("${fix-duplicate-websites-scheduler.enabled}") boolean fixDuplicateWebsitesJobEnabled
    ) {
        this.websiteRepository = websiteRepository;
        this.scraperService = scraperService;
        this.fetchWebsitesJobEnabled = fetchWebsitesJobEnabled;
        this.processWebsitesJobEnabled = processWebsitesJobEnabled;
        this.fixDuplicateWebsitesJobEnabled = fixDuplicateWebsitesJobEnabled;
    }

    @Scheduled(fixedRate = 100)
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void fetchWebsitesContent() throws IOException {
        if (fetchWebsitesJobEnabled) {
            Optional<Website> nextUncheckedWebsite = websiteRepository.getNextUncheckedWebsite();
            nextUncheckedWebsite.ifPresent(scraperService::fetchWebsiteContent);
        }
    }

    @Scheduled(fixedRate = 100)
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void processWebsites() {
        if (processWebsitesJobEnabled) {
            Optional<Website> nextUnprocessedWebsite = websiteRepository.getNextUnprocessedWebsite();
            nextUnprocessedWebsite.ifPresent(scraperService::processWebsite);
        }
    }

    @Scheduled(fixedRate = 100)
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void fixDuplicateWebsites() {
        if (fixDuplicateWebsitesJobEnabled) {
            Optional<String> nextDuplicateWebsiteUrl = websiteRepository.getNextDuplicateWebsiteUrl();
            nextDuplicateWebsiteUrl.ifPresent(scraperService::fixDuplicateWebsite);
        }
    }
}
