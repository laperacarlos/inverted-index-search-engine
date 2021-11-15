package com.findwise.calculator;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CalculatorImpl implements Calculator {

    public double calculateTFIDFScore(double tf, int corpusSize, int docsWithTerm) {
        double idf = calculateIdf(corpusSize, docsWithTerm);
        return tf * idf;
    }

    public double calculateTF(List<String> tokens, String term) {
        int result = 0;
        for (String token : tokens) {
            if (term.equalsIgnoreCase(token)) {
                ++result;
            }
        }
        return (double) result / tokens.size();
    }

    private double calculateIdf(int corpusSize, int docsWithTerm) {
        return Math.log10((double) corpusSize / (1 + docsWithTerm)) + 1;
    }
}
