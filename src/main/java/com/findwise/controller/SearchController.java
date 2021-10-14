package com.findwise.controller;

import com.findwise.engine.SearchEngineImpl;
import com.findwise.entry.IndexEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchEngineImpl searchEngine;

    @GetMapping(value = "search/{term}")
    public List<IndexEntry> searchTerm(@PathVariable String term) {
        addExampleDocuments();
        List<IndexEntry> resultList = searchEngine.search(term);
        displayExampleResult(resultList, term);
        return resultList;
    }

    private void addExampleDocuments() {
        searchEngine.indexListOfDocuments(List.of("the brown fox jumped over the brown dog", "the lazy brown dog sat in the corner", "the red fox bit the lazy dog"));
    }

    private void displayExampleResult(List<IndexEntry> resultList, String term) {
        System.out.println("Result list for query: " + term);
        for (IndexEntry indexEntry : resultList) {
            System.out.println("Document Id: " + indexEntry.getId() + ", TF-IDF score: " + indexEntry.getScore());
        }
    }
}
