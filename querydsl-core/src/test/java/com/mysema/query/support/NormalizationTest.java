package com.mysema.query.support;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.mysema.testutil.Benchmark;
import com.mysema.testutil.Performance;
import com.mysema.testutil.Runner;

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
    public void Variables() {
        assertUntouched("var1 + 3");
    }

    @Test
    public void Arithmetic() {
        assertNormalized("3", "1 - 2 + 4");
    }

    @Test
    public void Normalize_Addition() {
        assertNormalized("3", "1+2");
        assertNormalized("where 3 = 3", "where 1+2 = 3");
        assertNormalized("where 3.3 = 3.3", "where 1.1+2.2 = 3.3");
        assertNormalized("where 3.3 = 3.3", "where 1.1 + 2.2 = 3.3");
    }

    @Test
    public void Normalize_Subtraction() {
        assertNormalized("3", "5-2");
        assertNormalized("where 3 = 3", "where 5-2 = 3");
        assertNormalized("where 3.3 = 3.3", "where 5.5-2.2 = 3.3");
        assertNormalized("where 3.3 = 3.3", "where 5.5 - 2.2 = 3.3");
    }

    @Test
    public void Normalize_Multiplication() {
        assertNormalized("10", "5*2");
        assertNormalized("where 10 = 10", "where 5*2 = 10");
        assertNormalized("where 11 = 11", "where 5.5*2 = 11");
        assertNormalized("where 10.8 = 10.8", "where 5.4 * 2 = 10.8");
        assertNormalized("where 9 = 9 and 13 = 13", "where 2 * 3 + 3 = 9 and 5 + 4 * 2 = 13");
    }

    @Test
    public void Normalize_Division() {
        assertNormalized("2.5", "5/2");
        assertNormalized("where 2.5 = 2.5", "where 5/2 = 2.5");
        assertNormalized("where 2.6 = 2.6", "where 5.2/2 = 2.6");
        assertNormalized("where 2.6 = 2.6", "where 5.2 / 2 = 2.6");
    }

    @Test
    public void Normalize_Modulo() {
        assertNormalized("1", "4%3");
        assertNormalized("3", "2 + 4%3");
    }

    @Test
    public void Mixed() {
        assertNormalized("13", "2 * 5 + 3");
        assertNormalized("17", "2 + 5 * 3");
        assertNormalized("-2.5", "2.5 * -1");
        assertUntouched("hours * 2 + 3");
        assertUntouched("2 + 3 * hours");
        assertUntouched("2 + 3 * 0hours");
        assertUntouched("a like '1 + 2 ' and b like '2 * 3'");
        assertUntouched("xxx in ('ABC123-4567-3214-EDBD982')");
        assertUntouched("xxx in ('CFD9A467-439A-4033-8176-464D3AA0E430')");
    }

    @Test
    public void PI() {
        assertNormalized("0.1591549431", "0.5 / " + Math.PI);
    }

    @Test
    public void DateTimeLiterals() {
        assertUntouched("'1980-10-10'");
    }

    @Test
    public void DateTimeLiterals2() {
        assertUntouched("\"1980-10-10\"");
    }

    @Test
    public void Math1() {
        assertNormalized("fn(1)", "fn(-1+2)");
        assertNormalized("fn(3)", "fn(1--2)");
    }

    @Test
    public void Substring() {
        assertNormalized("substring(cat.name,1,locate(?1,cat.name)-1-2)",
                "substring(cat.name,0+1,locate(?1,cat.name)-1-2)");
    }

    @Test
    public void Literals() {
        assertUntouched("'INPS-ISET-0000-12345678A'");
        assertUntouched("'INPS-ISET-0000X00000000A'");
        assertUntouched("'INPS-ISET-0000-00000000A'");
        assertUntouched("'INPS-ISET-0000-0000.0000A'");
        assertUntouched("'INPS-ISET-0000-0000.0000'");

        assertUntouched("column = 'INPS-ISET-0000-00000000A' limit 1");
    }

    @Test
    public void Parameters() {
        assertUntouched("?1 + 1");
    }

    private static void assertUntouched(String untouched) {
        assertNormalized(untouched, untouched);
    }

    private static void assertNormalized(String expected, String input) {
        assertEquals(expected, Normalization.normalize(input));
    }
}
