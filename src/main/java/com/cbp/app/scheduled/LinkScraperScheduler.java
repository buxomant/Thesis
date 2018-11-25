package com.cbp.app.scheduled;

import com.cbp.app.model.db.Website;
import com.cbp.app.repository.WebsiteRepository;
import com.cbp.app.service.ScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class LinkScraperScheduler {

    private final boolean jobEnabled;
    private final WebsiteRepository websiteRepository;
    private final ScraperService scraperService;

    @Autowired
    public LinkScraperScheduler(
        WebsiteRepository websiteRepository,
        ScraperService scraperService,
        @Value("${link-scraper-scheduler.enabled}") boolean jobEnabled
    ) {
        this.websiteRepository = websiteRepository;
        this.scraperService = scraperService;
        this.jobEnabled = jobEnabled;
    }

    private static final int ONE_SECOND_IN_MILLISECONDS = 1000;

    @Scheduled(fixedRate = ONE_SECOND_IN_MILLISECONDS)
    public void findNewLinks() throws IOException {
        if (!jobEnabled) {
            return;
        }

        Optional<Website> websiteOptional = websiteRepository.getNextUncheckedWebsite();
        if (!websiteOptional.isPresent()) {
            return;
        }

        scraperService.findLinksOnWebsite(websiteOptional.get());
    }
}
