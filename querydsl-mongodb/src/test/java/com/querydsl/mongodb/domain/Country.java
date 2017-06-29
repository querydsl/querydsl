package com.querydsl.mongodb.domain;

import java.util.Locale;

import org.mongodb.morphia.annotations.Converters;
import org.mongodb.morphia.annotations.Entity;

@Entity
@Converters(LocaleConverter.class)
public class Country extends AbstractEntity {
    private String name;
    private Locale defaultLocale;

    Country() { }

    public Country(String name, Locale defaultLocale) {
        this.name = name;
        this.defaultLocale = defaultLocale;
    }

    public String getName() {
        return name;
    }

    public Locale getDefaultLocale() {
        return defaultLocale;
    }

}
