package com.findwise.calculator;

import java.util.List;

public interface Calculator {
    double calculateTFIDFScore(double tf, int corpusSize, int docsWithTerm);

    double calculateTF(List<String> tokens, String term);
}
