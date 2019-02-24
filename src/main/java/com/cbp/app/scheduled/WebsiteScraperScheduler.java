package com.cbp.app.scheduled;

import com.cbp.app.helper.LoggingHelper;
import com.cbp.app.helper.TimeLimitedRepeater;
import com.cbp.app.model.db.Website;
import com.cbp.app.repository.WebsiteRepository;
import com.cbp.app.service.ComparisonService;
import com.cbp.app.service.ScraperService;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ro.RomanianAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.cbp.app.service.IndexService.DATE_AND_HOUR_PATTERN;
import static com.cbp.app.service.IndexService.WEBSITE_STORAGE_PATH;
import static com.cbp.app.service.IndexService.indexDocs;

@Component
public class WebsiteScraperScheduler {

    private final WebsiteRepository websiteRepository;
    private final ScraperService scraperService;
    private final ComparisonService comparisonService;
    private final boolean fetchWebsitesJobEnabled;
    private final boolean processWebsitesJobEnabled;
    private final boolean fixDuplicateWebsitesJobEnabled;
    private final boolean establishSubdomainRelationshipsJobEnabled;

    @Autowired
    public WebsiteScraperScheduler(
        WebsiteRepository websiteRepository,
        ScraperService scraperService,
        ComparisonService comparisonService,
        @Value("${fetch-websites-scheduler.enabled}") boolean fetchWebsitesJobEnabled,
        @Value("${process-websites-scheduler.enabled}") boolean processWebsitesJobEnabled,
        @Value("${fix-duplicate-websites-scheduler.enabled}") boolean fixDuplicateWebsitesJobEnabled,
        @Value("${establish-subdomain-relationships.enabled}") boolean establishSubdomainRelationshipsJobEnabled
    ) {
        this.websiteRepository = websiteRepository;
        this.scraperService = scraperService;
        this.comparisonService = comparisonService;
        this.fetchWebsitesJobEnabled = fetchWebsitesJobEnabled;
        this.processWebsitesJobEnabled = processWebsitesJobEnabled;
        this.fixDuplicateWebsitesJobEnabled = fixDuplicateWebsitesJobEnabled;
        this.establishSubdomainRelationshipsJobEnabled = establishSubdomainRelationshipsJobEnabled;
    }

    @Scheduled(fixedRate = 60 * 1000)
    public void fetchWebsitesContent() throws IOException, ExecutionException, InterruptedException {
        if (fetchWebsitesJobEnabled) {
            LocalTime startTime = LoggingHelper.logStartOfMethod("fetchWebsitesContent (parallel)");

            List<Website> nextUncheckedWebsites = websiteRepository.getNextDomesticWebsitesThatNeedFetching();

            Map<Website, Optional<Document>> webPagesByWebsite = nextUncheckedWebsites.parallelStream()
                .collect(Collectors.toMap(
                    Function.identity(),
                    scraperService::getWebPageIfUrlReachable
                ));

            webPagesByWebsite.forEach((website, webPage) ->
                webPage.ifPresent(document -> scraperService.storeWebsiteContent(website, document))
            );

            LoggingHelper.logEndOfMethod(
                "fetchWebsitesContent (" + nextUncheckedWebsites.size() + " websites in parallel)",
                startTime
            );
        }
    }

    @Scheduled(fixedRate = 60 * 1000)
    public void processWebsites() throws IOException {
        if (processWebsitesJobEnabled) {
            Queue<Website> nextUnprocessedWebsites = new LinkedList<>(
                websiteRepository.getNextDomesticWebsitesThatNeedProcessing()
            );

            TimeLimitedRepeater
                .repeat(() -> scraperService.processWebsite(nextUnprocessedWebsites))
                .repeatWithDefaultTimeLimit();

            indexAndCompareWebsites();
        }
    }

    private void indexAndCompareWebsites() throws IOException {
        LocalTime startTime = LoggingHelper.logStartOfMethod("indexAndCompareWebsites");

        String dateAndHour = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_AND_HOUR_PATTERN));
        String workingDirectory = WEBSITE_STORAGE_PATH + "/" + dateAndHour;
        Path documentsPath = Paths.get(workingDirectory);

        Directory directory = FSDirectory.open(Paths.get(workingDirectory));

        Analyzer analyzer = new RomanianAnalyzer();
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);

        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
        indexWriter.deleteAll();

        indexDocs(indexWriter, documentsPath);
        indexWriter.forceMerge(1);
        indexWriter.close();

        comparisonService.compareDocuments();

        LoggingHelper.logEndOfMethod("indexAndCompareWebsites", startTime);
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

    @Scheduled(fixedRate = 1000)
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void establishSubdomainRelationships() {
        if (establishSubdomainRelationshipsJobEnabled) {
            Optional<Website> nextWebsite = websiteRepository.getNextWebsiteNotMarkedAsDomainOrSubdomain();
            nextWebsite.ifPresent(scraperService::establishSubdomainRelationshipsForWebsite);
        }
    }
}
