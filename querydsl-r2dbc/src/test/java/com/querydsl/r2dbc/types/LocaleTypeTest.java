package com.querydsl.r2dbc.types;

import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

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
