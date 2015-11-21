package com.querydsl.sql.types;

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.junit.Test;

public class LocaleTypeTest {

    @Test
    public void lang() {
        Locale l = new Locale("en");
        assertEquals(l, LocaleType.toLocale(l.toString()));
    }

    @Test
    public void lang_country() {
        Locale l = new Locale("en", "US");
        assertEquals(l, LocaleType.toLocale(l.toString()));
    }

    @Test
    public void lang_country_variant() {
        Locale l = new Locale("en", "US", "X");
        assertEquals(l, LocaleType.toLocale(l.toString()));
    }

}
