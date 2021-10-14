package com.findwise.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DataReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataReader.class);

    public List<String> docsFromResources() {
        try {
            String doc1 = getResourceFileAsString("documents/doc1.txt");
            String doc2 = getResourceFileAsString("documents/doc2.txt");
            String doc3 = getResourceFileAsString("documents/doc3.txt");
            return List.of(Objects.requireNonNull(doc1), Objects.requireNonNull(doc2), Objects.requireNonNull(doc3));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    private String getResourceFileAsString(String fileName) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(fileName)) {
            if (is == null) return null;
            try (InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                 BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }
}
