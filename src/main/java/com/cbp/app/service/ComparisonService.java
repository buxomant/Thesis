package com.cbp.app.service;

import com.cbp.app.model.db.WebsiteTextSimilarity;
import com.cbp.app.repository.WebsiteTextSimilarityRepository;
import org.apache.lucene.analysis.ro.RomanianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


import static com.cbp.app.service.IndexService.DATE_AND_HOUR_PATTERN;
import static com.cbp.app.service.IndexService.WEBSITE_STORAGE_PATH;

@Service
public class ComparisonService {
    private final WebsiteTextSimilarityRepository websiteTextSimilarityRepository;

    private static int MAX_HITS = 100;

    @Autowired
    public ComparisonService(WebsiteTextSimilarityRepository websiteTextSimilarityRepository) {
        this.websiteTextSimilarityRepository = websiteTextSimilarityRepository;
    }

    public void compareDocuments() throws IOException {
        String dateAndHour = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_AND_HOUR_PATTERN));
        Path workingDirectoryPath = Paths.get(WEBSITE_STORAGE_PATH + "/" + dateAndHour);

        Directory workingDirectory = FSDirectory.open(workingDirectoryPath);
        IndexReader indexReader = DirectoryReader.open(workingDirectory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        List<WebsiteTextSimilarity> allSimilarities = new ArrayList<>();

        AtomicInteger docId = new AtomicInteger(0);
        for (int currentDocumentId = 0; currentDocumentId < indexReader.maxDoc(); currentDocumentId++) {
            int tempDocId = docId.getAndIncrement(); // qq refactor
            Document currentDocument = indexReader.document(currentDocumentId);
            int currentWebsiteId = Integer.parseInt(currentDocument.getField("websiteId").stringValue());

            MoreLikeThis moreLikeThis = new MoreLikeThis(indexReader);
            moreLikeThis.setFieldNames(new String[] { "websiteText" });
            moreLikeThis.setAnalyzer(new RomanianAnalyzer());

            Query query = moreLikeThis.like(currentDocumentId);

            TopDocs result = indexSearcher.search(query, MAX_HITS);
            List<WebsiteTextSimilarity> similarities = Arrays.stream(result.scoreDocs)
                .filter(scoreDoc -> scoreDoc.doc > tempDocId)
                .map(scoreDoc -> {
                    try {
                        Document otherDocument = indexReader.document(scoreDoc.doc);
                        int otherWebsiteId = Integer.parseInt(otherDocument.getField("websiteId").stringValue());
                        Float coefficient = scoreDoc.score / result.getMaxScore();
                        return new WebsiteTextSimilarity(currentWebsiteId, otherWebsiteId, dateAndHour, coefficient);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

            allSimilarities.addAll(similarities);
        }

        websiteTextSimilarityRepository.deleteAll();
        websiteTextSimilarityRepository.saveAll(allSimilarities);
    }
}
