package com.mysema.query.support;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NormalizationTest {

    @Test
    public void Performance() {
        int iterations = 1000000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            Normalization.normalize("select name from companies where id = ?");
        }
        System.err.println(System.currentTimeMillis() - start);
    }

    @Test
    public void Variables() {
        assertEquals("var1 + 3", Normalization.normalize("var1 + 3"));
    }

    @Test
    public void Normalize_Addition() {
        assertEquals("3", Normalization.normalize("1+2"));
        assertEquals("where 3 = 3", Normalization.normalize("where 1+2 = 3"));
        assertEquals("where 3.3 = 3.3", Normalization.normalize("where 1.1+2.2 = 3.3"));
        assertEquals("where 3.3 = 3.3", Normalization.normalize("where 1.1 + 2.2 = 3.3"));
    }

    @Test
    public void Normalize_Subtraction() {
        assertEquals("3", Normalization.normalize("5-2"));
        assertEquals("where 3 = 3", Normalization.normalize("where 5-2 = 3"));
        assertEquals("where 3.3 = 3.3", Normalization.normalize("where 5.5-2.2 = 3.3"));
        assertEquals("where 3.3 = 3.3", Normalization.normalize("where 5.5 - 2.2 = 3.3"));
    }

    @Test
    public void Normalize_Multiplication() {
        assertEquals("10", Normalization.normalize("5*2"));
        assertEquals("where 10 = 10", Normalization.normalize("where 5*2 = 10"));
        assertEquals("where 11 = 11", Normalization.normalize("where 5.5*2 = 11"));
        assertEquals("where 10.8 = 10.8", Normalization.normalize("where 5.4 * 2 = 10.8"));
    }

    @Test
    public void Normalize_Division() {
        assertEquals("2.5", Normalization.normalize("5/2"));
        assertEquals("where 2.5 = 2.5", Normalization.normalize("where 5/2 = 2.5"));
        assertEquals("where 2.6 = 2.6", Normalization.normalize("where 5.2/2 = 2.6"));
        assertEquals("where 2.6 = 2.6", Normalization.normalize("where 5.2 / 2 = 2.6"));
    }

    @Test
    public void Mixed() {
        assertEquals("13", Normalization.normalize("2 * 5 + 3"));
        assertEquals("-2.5", Normalization.normalize("2.5 * -1"));
    }

    @Test
    public void PI() {
        assertEquals("0.1591549431", Normalization.normalize("0.5 / " + Math.PI));
    }

    @Test
    public void DateTimeLiterals() {
        assertEquals("'1980-10-10'", Normalization.normalize("'1980-10-10'"));
    }

}
