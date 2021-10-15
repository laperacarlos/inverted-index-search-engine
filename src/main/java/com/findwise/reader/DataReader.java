package com.findwise.reader;

import com.findwise.SearchEngineApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataReader.class);

    public List<String> docsFromResources() {
        List<String> resultList = new ArrayList<>();
        try {
            List<String> fileNames = getFileNamesFromResourceJAR();
            for (String file : fileNames) {
                resultList.add(getResourceFileAsString("/documents/" + file));
            }
            return resultList;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    private String getResourceFileAsString(String fileName) throws IOException {
        try (InputStream is = SearchEngineApplication.class.getResourceAsStream(fileName)) {
            if (is == null) return null;
            try (InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                 BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }

    private List<String> getFileNamesFromResourceJAR() throws URISyntaxException, IOException {
        URI uri = SearchEngineApplication.class.getResource("/documents").toURI();
        System.out.println(uri.toString());
        Path dirPath = Paths.get(uri);
        return Files.list(dirPath)
                .map(p -> p.getFileName().toString())
                .collect(Collectors.toList());
    }
}
