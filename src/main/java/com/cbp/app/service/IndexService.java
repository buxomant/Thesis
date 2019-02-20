package com.cbp.app.service;

import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Collectors;

@Service
public class IndexService {
    public static final String WEBSITE_STORAGE_PATH = "./website-storage";
    public static final String DATE_AND_HOUR_PATTERN = "yyyy-MM-dd_HH";

    public static void indexDocs(final IndexWriter writer, Path path) throws IOException {
        if (Files.isDirectory(path)) {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    try {
                        indexDoc(writer, file);
                    } catch (IOException ignore) {
                        // don't index files that can't be read.
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            indexDoc(writer, path);
        }
    }

    private static void indexDoc(IndexWriter indexWriter, Path file) throws IOException {
        String fullFilePath = file.toString();

        if (!fullFilePath.endsWith(".txt")) {
            System.out.println("*** skipping non-text file");
            return;
        }

        String[] splitDirectoryPath = file.toString().replace(".txt", "").split("\\\\");
        String fileName = splitDirectoryPath[splitDirectoryPath.length - 1];
        String[] splitFileName = fileName.split("_");
        String websiteUrl = splitFileName[0];
        String websiteId = splitFileName[1];

        try (InputStream stream = Files.newInputStream(file)) {
            Document document = new Document();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            String websiteText = bufferedReader.lines().collect(Collectors.joining("\n"));

            Field websiteUrlField = new Field("websiteUrl", websiteUrl, getFieldType());
            Field websiteIdField = new Field("websiteId", websiteId, getFieldType());
            Field websiteTextField = new Field("websiteText", websiteText, getFieldType());
            document.add(websiteUrlField);
            document.add(websiteIdField);
            document.add(websiteTextField);

            System.out.println("*** adding to index " + file);
            indexWriter.addDocument(document);

//            if (indexWriter.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {
//                System.out.println("*** adding to index " + file);
//                indexWriter.addDocument(document);
//            } else {
//                System.out.println("*** updating in index " + file);
//                indexWriter.updateDocument(new Term("path", file.toString()), document);
//            }
        }
    }

    private static FieldType getFieldType() {
        FieldType fieldType = new FieldType();
        fieldType.setStored(true);
        fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        fieldType.setTokenized(true);
        fieldType.setStoreTermVectors(true);
        fieldType.setStoreTermVectorOffsets(true);
        fieldType.setStoreTermVectorPayloads(true);
        fieldType.setStoreTermVectorPositions(true);
        fieldType.setOmitNorms(true);

        return fieldType;
    }
}
