package com.findwise.engine;

import com.findwise.entry.IndexEntry;

import java.util.List;

public interface SearchEngine {
    void indexDocument(String id, String content);
    List<IndexEntry> search(String term);
}
