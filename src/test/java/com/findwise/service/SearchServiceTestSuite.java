package com.findwise.service;

import com.findwise.engine.SearchEngineImpl;
import com.findwise.entry.IndexEntry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SearchServiceTestSuite {

    @Autowired
    private SearchEngineImpl searchService;

    @Test
    void shouldIndexAndSearchDocuments() {
        //given
        searchService.indexDocument("id1", "the brown fox jumped over the brown dog");
        searchService.indexDocument("id2", "the lazy brown dog sat in the corner");
        searchService.indexDocument("id3", "the red fox bit the lazy dog");

        //when
        List<IndexEntry> entriesBrown = searchService.search("brown");
        List<IndexEntry> entriesFox = searchService.search("fox");

        //then
        assertEquals(2, entriesBrown.size());
        assertEquals(2, entriesFox.size());
        assertEquals(0.04402, entriesBrown.get(0).getScore(), 0.00001);
        assertEquals("id1", entriesBrown.get(0).getId());
        assertEquals(0.02201, entriesBrown.get(1).getScore(), 0.00001);
        assertEquals("id2", entriesBrown.get(1).getId());
        assertEquals(0.02515, entriesFox.get(0).getScore(), 0.00001);
        assertEquals("id3", entriesFox.get(0).getId());
        assertEquals(0.02201, entriesFox.get(1).getScore(), 0.00001);
        assertEquals("id1", entriesFox.get(1).getId());
    }
}
