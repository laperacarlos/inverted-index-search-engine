package com.findwise.service;

import com.findwise.calculator.CalculatorImpl;
import com.findwise.engine.SearchEngineImpl;
import com.findwise.entry.IndexEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SearchServiceTest {

    private SearchEngineImpl searchEngine;

    @BeforeEach
    void createEngine() {
        searchEngine = new SearchEngineImpl(new CalculatorImpl());
    }

    @Test
    void shouldIndexAndSearchSortedDocuments() {
        //given
        searchEngine.indexDocument("id1", "the brown fox jumped over the brown dog");
        searchEngine.indexDocument("id2", "the lazy brown dog sat in the corner");
        searchEngine.indexDocument("id3", "the red fox bit the lazy dog");

        //when
        List<IndexEntry> entriesBrown = searchEngine.search("brown");
        List<IndexEntry> entriesThe = searchEngine.search("the");
        List<IndexEntry> entriesOver = searchEngine.search("over");

        //then
        assertEquals(2, entriesBrown.size());
        assertEquals(3, entriesThe.size());
        assertEquals(1, entriesOver.size());
        assertEquals(0.25, entriesBrown.get(0).getScore(), 0.00001);
        assertEquals("id1", entriesBrown.get(0).getId());
        assertEquals(0.125, entriesBrown.get(1).getScore(), 0.00001);
        assertEquals("id2", entriesBrown.get(1).getId());
        assertEquals(0.25001, entriesThe.get(0).getScore(), 0.00001);
        assertEquals("id3", entriesThe.get(0).getId());
        assertEquals(0.21876, entriesThe.get(1).getScore(), 0.00001);
        assertEquals(0.14701, entriesOver.get(0).getScore(), 0.00001);
        assertEquals("id1", entriesOver.get(0).getId());
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

    @Test
    void shouldReturnEmptyList() {
        //given
        searchEngine.indexDocument("id1", "the brown fox jumped over the brown dog");
        searchEngine.indexDocument("id2", "the lazy brown dog sat in the corner");
        searchEngine.indexDocument("id3", "the red fox bit the lazy dog");

        //when
        List<IndexEntry> entriesOrange = searchEngine.search("orange");

        //then
        assertEquals(0, entriesOrange.size());
    }

    @Test
    void shouldNotIndexNull() {
        //given
        searchEngine.indexDocument("id1", null);
        searchEngine.indexDocument("id2", "the red fox bit the lazy dog");

        //when&then
        assertEquals(1, searchEngine.getCorpusSize());
    }
}
