package com.findwise.engine;

import com.findwise.entry.Entry;
import com.findwise.entry.IndexEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchEngineImpl implements SearchEngine {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchEngineImpl.class);
    private final Map<String, Set<Entry>> indexMap = new HashMap<>();
    private final Map<String, String> docsMap = new HashMap<>();
    private int corpusSize = 0;

    public void indexListOfDocuments(List<String> docs) {
        for (String doc : docs) {
            if (doc != null) {
                String id = UUID.randomUUID().toString();
                indexDocument(id, doc);
                docsMap.put(id, doc);
            }
        }
    }

    public void indexDocument(String id, String content) {
        if (isContentValid(content)) {
            List<String> tokens = Arrays.asList(content.split("[\\s!.?:;]"));
            parseTokens(id, tokens);
        } else {
            LOGGER.info("Document with blank content, will not be taken for TF-IDF calculation. Document Id: " + id);
        }
    }

    public List<IndexEntry> search(String term) {
        if (indexMap.containsKey(term)) {
            List<IndexEntry> entryList = new ArrayList<>(indexMap.get(term));
            entryList.sort(Comparator.comparingDouble(IndexEntry::getScore).reversed());
            return entryList;
        } else {
            LOGGER.info("Searched term: " + term + " was not found in indexed documents.");
            return Collections.emptyList();
        }
    }

    private boolean isContentValid(String content) {
        return !content.isBlank();
    }

    private void parseTokens(String id, List<String> tokens) {
        ++corpusSize;
        for (String token : tokens) {
            if (indexMap.containsKey(token)) {
                tryUpdateToken(id, tokens, token);
            } else {
                insertToken(id, tokens, token);
            }
        }
    }

    private void tryUpdateToken(String id, List<String> tokens, String token) {
        if (indexMap.get(token).stream().noneMatch(entry -> id.equals(entry.getId()))) {
            Set<Entry> entriesToUpdate = indexMap.get(token);
            double tf = calculateTf(tokens, token);
            entriesToUpdate.add(new Entry(id, tf));
            indexMap.replace(token, entriesToUpdate);
            scoreActualization();
        }
    }

    private void insertToken(String id, List<String> tokens, String token) {
        Set<Entry> entrySet = new HashSet<>();
        double tf = calculateTf(tokens, token);
        entrySet.add(new Entry(id, tf));
        indexMap.put(token, entrySet);
        scoreActualization();
    }

    private void scoreActualization() {
        List<String> keyList = new ArrayList<>(indexMap.keySet());
        for (String key : keyList) {
            Set<Entry> entrySet = indexMap.get(key);
            double idf = calculateIdf(corpusSize, entrySet.size());
            entrySet.forEach(entry -> entry.setScore(scoreCalculator(entry.getTfValue(), idf)));
            this.indexMap.replace(key, entrySet);
        }
    }
//TODO calculations in new service
    private double scoreCalculator(double tf, double idf) {
        return tf * idf;
    }

    private double calculateTf(List<String> tokens, String term) {
        int result = 0;
        for (String token : tokens) {
            if (term.equalsIgnoreCase(token)) {
                ++result;
            }
        }
        return (double) result / tokens.size();
    }

//TODO check sorting while docsWithTerm=0 & docsWithTerm=corpusSize
    private double calculateIdf(int corpusSize, int docsWithTerm) {
        return Math.log10((double) corpusSize / docsWithTerm);
    }

    public int getCorpusSize() {
        return corpusSize;
    }
}
