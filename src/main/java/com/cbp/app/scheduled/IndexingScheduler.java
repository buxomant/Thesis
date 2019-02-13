package com.cbp.app.scheduled;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ro.RomanianAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.cbp.app.service.IndexService.DATE_AND_HOUR_PATTERN;
import static com.cbp.app.service.IndexService.WEBSITE_STORAGE_PATH;
import static com.cbp.app.service.IndexService.indexDocs;

@Component
public class IndexingScheduler {
    private static final int ONE_HOUR_IN_MILLIS = 60 * 60 * 1000;

    @Scheduled(fixedRate = ONE_HOUR_IN_MILLIS)
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void indexWebsites() throws IOException {
        String dateAndHour = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_AND_HOUR_PATTERN));
        String workingDirectory = WEBSITE_STORAGE_PATH + "/" + dateAndHour;
        Path docDir = Paths.get(workingDirectory);

        Directory dir = FSDirectory.open(Paths.get(workingDirectory));
        Analyzer analyzer = new RomanianAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);

        IndexWriter writer = new IndexWriter(dir, iwc);
        indexDocs(writer, docDir);
        writer.forceMerge(1);
        writer.close();
    }
}
