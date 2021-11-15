package com.findwise.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorImplTest {

    private CalculatorImpl calculatorImpl;

    @BeforeEach
    void createCalculator() {
        calculatorImpl = new CalculatorImpl();
    }

    @Test
    void shouldCalculateTF() {
        //given
        List<String> tokensList1 = List.of("zażółć");
        List<String> tokensList2 = List.of("zażółć", "gęślą", "jaźń");
        List<String> tokensList3 = List.of("the", "brown", "fox", "jumped", "over", "the", "brown", "dog");

        //when&then
        assertEquals(1, calculatorImpl.calculateTF(tokensList1, "zażółć"));
        assertEquals(0.33333, calculatorImpl.calculateTF(tokensList2, "zażółć"), 0.00001);
        assertEquals(0.25, calculatorImpl.calculateTF(tokensList3, "the"));
    }

    @Test
    void shouldCalculateTFIDF() {
        //when&then
        assertEquals(0.17474, calculatorImpl.calculateTFIDFScore(0.25, 1, 1), 0.00001);
        assertEquals(0.21876, calculatorImpl.calculateTFIDFScore(0.25, 3, 3), 0.00001);
        assertEquals(0.25, calculatorImpl.calculateTFIDFScore(0.25, 1, 0), 0.00001);
    }
}
