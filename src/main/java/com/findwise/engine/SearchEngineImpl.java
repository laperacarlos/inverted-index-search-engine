package com.findwise.engine;

import com.findwise.entry.Entry;
import com.findwise.entry.IndexEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchEngineImpl implements SearchEngine {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchEngineImpl.class);
    private final Map<String, Set<Entry>> indexMap = new HashMap<>();
    private final Map<String, String> docsMap = new HashMap<>();
    private int corpusCounter = 0;

    public void indexListOfDocuments(List<String> docs) {
        for (String doc : docs) {
            String id = UUID.randomUUID().toString();
            indexDocument(id, doc);
            docsMap.put(id, doc);
        }
    }

    public void indexDocument(String id, String content) {

        documentValidation(content, id);

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
                if (indexMap.get(token).stream().noneMatch(t -> id.equals(t.getId()))) {

                    Set<Entry> entriesToUpdate = indexMap.get(token);
                    entriesToUpdate.add(new Entry(id, calculateTf(tokens, token)));
                    indexMap.replace(token, entriesToUpdate);
                    scoreActualization();

                }
            }
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

    private void documentValidation(String id, String content) {
        if(content.isBlank()) {
            LOGGER.info("Document with blank content. Id: " + id);
        }
        corpusCounter++;
    }

    private void scoreActualization() {
        List<String> keyList = new ArrayList<>(indexMap.keySet());

        for (String key : keyList) {
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
            if (term.equalsIgnoreCase(token)) {
                result++;
            }
        }
        return (double)result / tokens.size();
    }

    private double calculateIdf(int corpusSize, int docsWithTerm) {
        return Math.log10((double)corpusSize / docsWithTerm);
    }
}
