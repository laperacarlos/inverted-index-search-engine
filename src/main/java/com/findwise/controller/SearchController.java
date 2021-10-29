package com.findwise.controller;

import com.findwise.engine.SearchEngineImpl;
import com.findwise.entry.IndexEntry;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);

    private final SearchEngineImpl searchEngine;

    @GetMapping(value = "search")
    public List<IndexEntry> searchTerm(@RequestParam("q") String term) {
        List<IndexEntry> resultList = searchEngine.search(term);
        for (IndexEntry entry : resultList) {
            LOGGER.info("Entry for query: \"{}\": Id: {}, score: {}", term, entry.getId(), entry.getScore());
        }
        return resultList;
    }

    @PostConstruct
    private void addExampleDocuments() {
        searchEngine.indexListOfDocuments(List.of("the brown fox jumped over the brown dog", "the lazy brown dog sat in the corner", "the red fox bit the lazy dog"));
    }
}
