package com.findwise.engine;

import com.findwise.calculator.Calculator;
import com.findwise.entry.Entry;
import com.findwise.entry.IndexEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SearchEngineImpl implements SearchEngine {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchEngineImpl.class);
    private final Map<String, Set<Entry>> indexMap = new HashMap<>();
    private final Map<String, String> docsMap = new HashMap<>();
    private int corpusSize = 0;
    private final Calculator calculator;

    @Autowired
    public SearchEngineImpl(Calculator calculator) {
        this.calculator = calculator;
    }

    public void indexListOfDocuments(List<String> docs) {
        for (String doc : docs) {
            String id = UUID.randomUUID().toString();
            indexDocument(id, doc);
            docsMap.put(id, doc);
        }
    }

    public void indexDocument(String id, String content) {
        if (isContentValid(content)) {
            List<String> tokens = Arrays.asList(content.split("[\\s!.?:;]"));
            parseTokens(id, tokens);
        } else {
            LOGGER.info("Document with blank content or \"null\" content, will not be taken for TF-IDF calculation. Document Id: " + id);
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
        return content != null && !content.isBlank();
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
            double tf = calculator.calculateTF(tokens, token);
            entriesToUpdate.add(new Entry(id, tf));
            indexMap.replace(token, entriesToUpdate);
            scoreActualization();
        }
    }

    private void insertToken(String id, List<String> tokens, String token) {
        Set<Entry> entrySet = new HashSet<>();
        double tf = calculator.calculateTF(tokens, token);
        entrySet.add(new Entry(id, tf));
        indexMap.put(token, entrySet);
        scoreActualization();
    }

    private void scoreActualization() {
        List<String> keyList = new ArrayList<>(indexMap.keySet());
        for (String key : keyList) {
            Set<Entry> entrySet = indexMap.get(key);
            int docsWithTerm = entrySet.size();
            entrySet.forEach(entry -> entry.setScore(calculator.calculateTFIDFScore(entry.getTfValue(), corpusSize, docsWithTerm)));
            this.indexMap.replace(key, entrySet);
        }
    }

    public int getCorpusSize() {
        return corpusSize;
    }
}
