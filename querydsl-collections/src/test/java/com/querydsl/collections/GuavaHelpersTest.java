package com.querydsl.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

public class GuavaHelpersTest {
    
    @Test
    public void Predicate() {
        Predicate<Cat> predicate = GuavaHelpers.wrap(QCat.cat.name.startsWith("Ann"));
        assertTrue(predicate.apply(new Cat("Ann")));
        assertFalse(predicate.apply(new Cat("Bob")));
    }
    
    @Test
    public void Function() {
        Function<Cat, String> function = GuavaHelpers.wrap(QCat.cat.name);
        assertEquals("Ann", function.apply(new Cat("Ann")));
        assertEquals("Bob", function.apply(new Cat("Bob")));
    }

}
