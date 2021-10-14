package com.findwise.engine;

import com.findwise.entry.Entry;
import com.findwise.entry.IndexEntry;
import com.findwise.exception.InvalidDocumentContentException;
import com.findwise.exception.InvalidDocumentIdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchEngineImpl implements SearchEngine {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchEngineImpl.class);
    private final Map<String, Set<Entry>> indexMap = new HashMap<>();
    private int corpusCounter = 0;

    public void indexDocument(String id, String content) {
        try {
            documentValidation(id, content);

            List<String> tokens = Collections.list(new StringTokenizer((content))).stream()
                    .map(token -> (String) token)
                    .collect(Collectors.toList());

            for (String token : tokens) {

                if (!indexMap.containsKey(token)) {

                    Set<Entry> entrySet = new HashSet<>();
                    entrySet.add(new Entry(id, calculateTf(tokens, token)));
                    indexMap.put(token, entrySet);
                    scoreActualization();

                } else {
                    if(indexMap.get(token).stream().noneMatch(t -> id.equals(t.getId()))) {

                        Set<Entry> entriesToUpdate = indexMap.get(token);
                        entriesToUpdate.add(new Entry(id, calculateTf(tokens, token)));
                        indexMap.replace(token, entriesToUpdate);
                        scoreActualization();

                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public List<IndexEntry> search(String term) {
        if(indexMap.containsKey(term)) {
            List<IndexEntry> entryList = new ArrayList<>(indexMap.get(term));
            entryList.sort(Comparator.comparingDouble(IndexEntry::getScore).reversed());
            return entryList;
        } else {
            LOGGER.info("Searched term: " + term + " was not found in indexed documents.");
            return Collections.emptyList();
        }
    }

    private void documentValidation(String id, String content) throws Exception {
        if(id.isBlank()) {
            throw new InvalidDocumentIdException();
        }
        if(content.isBlank()) {
            throw new InvalidDocumentContentException(id);
        }
        corpusCounter++;
    }

    private void scoreActualization() {
        List<String> keyList = new ArrayList<>(indexMap.keySet());

        for(String key : keyList) {
            Set<Entry> entrySet = indexMap.get(key);
            double idf = calculateIdf(corpusCounter, entrySet.size());
            entrySet.forEach(entry -> entry.setScore(scoreCalculator(entry.getTfValue(), idf)));
            this.indexMap.replace(key, entrySet);
        }
    }

    private double scoreCalculator(double tf, double idf) {
        return tf * idf;
    }

    private double calculateTf(List<String> tokens, String term) {
        int result = 0;
        for (String token : tokens) {
            if(term.equalsIgnoreCase(token)) {
                result++;
            }
        }
        return (double)result / tokens.size();
    }

    private double calculateIdf(int corpusSize, int docsWithTerm) {
        return Math.log10((double)corpusSize / docsWithTerm);
    }
}
