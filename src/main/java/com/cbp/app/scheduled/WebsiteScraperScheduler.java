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

    @Autowired
    public WebsiteScraperScheduler(
        WebsiteRepository websiteRepository,
        ScraperService scraperService,
        @Value("${fetch-websites-scheduler.enabled}") boolean fetchWebsitesJobEnabled,
        @Value("${process-websites-scheduler.enabled}") boolean processWebsitesJobEnabled
    ) {
        this.websiteRepository = websiteRepository;
        this.scraperService = scraperService;
        this.fetchWebsitesJobEnabled = fetchWebsitesJobEnabled;
        this.processWebsitesJobEnabled = processWebsitesJobEnabled;
    }

    private static final int ONE_SECOND_IN_MILLISECONDS = 100;

    @Scheduled(fixedRate = ONE_SECOND_IN_MILLISECONDS)
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void fetchWebsitesContent() throws IOException {
        System.out.println(">>> fetchWebsitesContent() >>>");
        if (fetchWebsitesJobEnabled) {
            Optional<Website> nextUncheckedWebsite = websiteRepository.getNextUncheckedWebsite();
            nextUncheckedWebsite.ifPresent(scraperService::fetchWebsiteContent);
        }
        System.out.println("<<< fetchWebsitesContent() <<<");
    }

    @Scheduled(fixedRate = ONE_SECOND_IN_MILLISECONDS)
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void processWebsites() {
        System.out.println(">>> processWebsites() >>>");
        if (processWebsitesJobEnabled) {
            Optional<Website> nextUnprocessedWebsite = websiteRepository.getNextUnprocessedWebsite();
            nextUnprocessedWebsite.ifPresent(scraperService::processWebsite);
        }
        System.out.println("<<< processWebsites() <<<");
    }
}
