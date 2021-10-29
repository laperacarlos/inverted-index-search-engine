package com.findwise.service;

import com.findwise.engine.SearchEngineImpl;
import com.findwise.entry.IndexEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//TODO add more unit tests (without Spring context)

public class SearchServiceTestSuite {

    private SearchEngineImpl searchEngine;

    @BeforeEach
    void createEngine() {
        searchEngine = new SearchEngineImpl();
    }

    @Test
    void shouldIndexAndSearchDocuments() {
        //given
        searchEngine.indexDocument("id1", "the brown fox jumped over the brown dog");
        searchEngine.indexDocument("id2", "the lazy brown dog sat in the corner");
        searchEngine.indexDocument("id3", "the red fox bit the lazy dog");

        //when
        List<IndexEntry> entriesBrown = searchEngine.search("brown");

        //then
        assertEquals(2, entriesBrown.size());
        assertEquals(0.04402, entriesBrown.get(0).getScore(), 0.00001);
        assertEquals("id1", entriesBrown.get(0).getId());
        assertEquals(0.02201, entriesBrown.get(1).getScore(), 0.00001);
        assertEquals("id2", entriesBrown.get(1).getId());
    }

    @Test
    void shouldNotIndexBlankOrEmptyContent() {
        //given
        searchEngine.indexDocument("id1", " ");
        searchEngine.indexDocument("id2", "");

        //when&then
        assertEquals(0, searchEngine.getCorpusSize());
    }

    @Test
    void shouldGenerateId() {
        //given
        searchEngine.indexListOfDocuments(List.of("zażółć gęślą jaźń"));

        //when
        List<IndexEntry> entries = searchEngine.search("zażółć");

        //then
        assertNotNull(entries.get(0).getId());
        assertFalse(entries.get(0).getId().isBlank());
    }
}
