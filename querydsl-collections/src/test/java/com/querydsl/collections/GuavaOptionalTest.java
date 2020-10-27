package com.querydsl.collections;

import static com.querydsl.collections.CollQueryFactory.from;
import static com.querydsl.core.alias.Alias.$;
import static com.querydsl.core.alias.Alias.alias;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import com.google.common.base.Optional;

public class GuavaOptionalTest {

    public static class Cat {

        private Optional<String> pedigree = Optional.absent();

        public Cat() {
        }

        public Cat(Optional<String> pedigree) {
            this.pedigree = pedigree;
        }

        public Optional<String> getPedigree() {
            return pedigree;
        }
    }

    @Test
    public void test() {
        Collection<Cat> cats = new ArrayList<>();
        cats.add(new Cat(Optional.<String>absent()));
        cats.add(new Cat(Optional.of("persian")));
        Cat c = alias(Cat.class);

        for (Cat cat : from(c, cats)
                .where($(c.getPedigree()).eq(Optional.of("persian")))
                .select($(c)).fetch()) {

            assertTrue(cat.getPedigree().isPresent());
            assertEquals("persian", cat.getPedigree().get());
        }
    }
}
