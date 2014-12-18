package com.querydsl.core.support;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.querydsl.core.testutil.Benchmark;
import com.querydsl.core.testutil.Performance;
import com.querydsl.core.testutil.Runner;

public class NormalizationTest {

    @Test
    @Category(Performance.class)
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
    @Category(Performance.class)
    public void Performance_Substring() throws Exception {
        Runner.run("NormalizationTest Performance_Substring", new Benchmark() {

            @Override
            public void run(int times) throws Exception {
                for (int i = 0; i < times; i++) {
                    Normalization.normalize("substring(cat.name,0+1,locate(?1,cat.name)-1-0)");
                }
            }
        });
    }

    @Test
    public void Variables() {
        assertEquals("var1 + 3", Normalization.normalize("var1 + 3"));
    }

    @Test
    public void Arithmetic() {
        assertEquals("3", Normalization.normalize("1 - 2 + 4"));
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
        assertEquals("2 + 3 * 0hours", Normalization.normalize("2 + 3 * 0hours"));
        assertEquals("a like '1 + 2 ' and b like '2 * 3'", Normalization.normalize("a like '1 + 2 ' and b like '2 * 3'"));
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

    @Test
    public void Parameters() {
        assertEquals("?1 + 1", Normalization.normalize("?1 + 1"));
    }

    @Test
    public void Literals() {
        assertEquals("'INPS-ISET-0000-12345678A'", Normalization.normalize("'INPS-ISET-0000-12345678A'"));
        assertEquals("'INPS-ISET-0000X00000000A'", Normalization.normalize("'INPS-ISET-0000X00000000A'"));
        assertEquals("'INPS-ISET-0000-00000000A'", Normalization.normalize("'INPS-ISET-0000-00000000A'"));

        assertEquals("column = 'INPS-ISET-0000-00000000A' limit 1", Normalization.normalize("column = 'INPS-ISET-0000-00000000A' limit 1"));
    }
}
