package com.mysema.query.collections;

import static com.mysema.query.alias.Alias.$;
import static com.mysema.query.alias.Alias.alias;
import static com.mysema.query.collections.CollQueryFactory.from;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

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
    public void Test() {
        Collection<Cat> cats = Lists.newArrayList();
        cats.add(new Cat(Optional.<String>absent()));
        cats.add(new Cat(Optional.of("persian")));
        Cat c = alias(Cat.class);

        for (Cat cat : from(c, cats)
                .where($(c.getPedigree()).eq(Optional.of("persian")))
                .list($(c))) {

            assertTrue(cat.getPedigree().isPresent());
            assertEquals("persian", cat.getPedigree().get());
        }
    }
}
