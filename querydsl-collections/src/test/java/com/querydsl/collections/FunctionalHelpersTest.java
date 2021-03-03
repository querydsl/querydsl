package com.querydsl.collections;

import org.junit.Test;

import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FunctionalHelpersTest {

    @Test
    public void predicate() {
        Predicate<Cat> predicate = FunctionalHelpers.wrap(QCat.cat.name.startsWith("Ann"));
        assertTrue(predicate.test(new Cat("Ann")));
        assertFalse(predicate.test(new Cat("Bob")));
    }

    @Test
    public void function() {
        Function<Cat, String> function = FunctionalHelpers.wrap(QCat.cat.name);
        assertEquals("Ann", function.apply(new Cat("Ann")));
        assertEquals("Bob", function.apply(new Cat("Bob")));
    }

}
