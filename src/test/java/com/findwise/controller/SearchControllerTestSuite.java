package com.findwise.controller;

import com.findwise.engine.SearchEngineImpl;
import com.findwise.entry.Entry;
import com.findwise.entry.IndexEntry;
import com.findwise.reader.DataReader;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(SearchController.class)
public class SearchControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchEngineImpl searchEngine;

    @MockBean
    private DataReader dataReader;

    @Test
    void shouldGetListOfIndexEntries() throws Exception {
        //given
        List<IndexEntry> entries = List.of(new Entry("id1", 0.77));
        List<String> list = List.of("jaźń");


        when(searchEngine.search("jaźń")).thenReturn(entries);
        when(dataReader.docsFromResources()).thenReturn(list);

        //when@tehn
        mockMvc.perform(MockMvcRequestBuilders
                .get("/search/jaźń")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is("id1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].score", Matchers.is(0.0)));
    }

}
