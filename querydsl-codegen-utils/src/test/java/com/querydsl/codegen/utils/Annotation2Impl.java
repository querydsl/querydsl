package com.querydsl.codegen.utils;

import java.lang.annotation.Annotation;

@SuppressWarnings("all")
public class Annotation2Impl implements Annotation2 {

    private final String value;

    public Annotation2Impl(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return Annotation2.class;
    }

}
