package com.querydsl.core.support;

import com.querydsl.core.testutil.Benchmark;
import com.querydsl.core.testutil.Runner;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NormalizationTest {

    @Test
    public void Performance() throws Exception {
        Runner.run("NormalizationTest Performance", new Benchmark() {

            @Override
            public void run(int times) throws Exception {
                for (int i = 0; i < times; i++) {
                    Normalization.normalize("select name from companies where id = ?");
                }
            }
        });
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
        assertEquals("where 9 = 9 and 13 = 13", Normalization.normalize("where 2 * 3 + 3 = 9 and 5 + 4 * 2 = 13"));
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
        assertEquals("17", Normalization.normalize("2 + 5 * 3"));
        assertEquals("-2.5", Normalization.normalize("2.5 * -1"));
        assertEquals("hours * 2 + 3", Normalization.normalize("hours * 2 + 3"));
        assertEquals("2 + 3 * hours", Normalization.normalize("2 + 3 * hours"));
    }

    @Test
    public void PI() {
        assertEquals("0.1591549431", Normalization.normalize("0.5 / " + Math.PI));
    }

    @Test
    public void DateTimeLiterals() {
        assertEquals("'1980-10-10'", Normalization.normalize("'1980-10-10'"));
    }

    @Test
    public void DateTimeLiterals2() {
        assertEquals("\"1980-10-10\"", Normalization.normalize("\"1980-10-10\""));
    }

    @Test
    public void Math1() {
        assertEquals("fn(1)", Normalization.normalize("fn(-1+2)"));
        assertEquals("fn(3)", Normalization.normalize("fn(1--2)"));
    }

    @Test
    public void Substring() {
        assertEquals("substring(cat.name,1,locate(?1,cat.name)-1)",
                Normalization.normalize("substring(cat.name,0+1,locate(?1,cat.name)-1-0)"));
    }
}
