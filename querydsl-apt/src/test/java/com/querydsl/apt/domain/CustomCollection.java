package com.querydsl.apt.domain;

import java.util.AbstractSet;
import java.util.Iterator;

import javax.persistence.Entity;

public class CustomCollection {

    @Entity
    public static class MyCustomCollection<T> extends AbstractSet<T> {

        @Override
        public Iterator<T> iterator() {
            return null;
        }

        @Override
        public int size() {
            return 0;
        }

    }

    @Entity
    public static class MyCustomCollection2<T> extends AbstractSet<T> {

        @Override
        public Iterator<T> iterator() {
            return null;
        }

        @Override
        public int size() {
            return 0;
        }

    }

    @Entity
    public static class MyEntity {

        MyCustomCollection<String> strings;

        MyCustomCollection2 strings2;
    }

}
