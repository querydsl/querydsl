package com.mysema.query.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.codegen.Evaluator;

public class MultiComparatorTest {
    
    private final Evaluator<Object[]> evaluator = new Evaluator<Object[]>() {
        @Override
        public Object[] evaluate(Object... args) {
            return args;
        }
        @Override
        public Class<? extends Object[]> getType() {
            return Object[].class;
        }            
    };
    
    @Test
    public void test() {
        MultiComparator<Object[]> comparator = new MultiComparator<Object[]>(evaluator, new boolean[]{true, true});
        assertTrue(comparator.compare(new Object[]{"a", "b"}, new Object[]{"a","c"}) < 0);
        assertTrue(comparator.compare(new Object[]{"b", "a"}, new Object[]{"a","b"}) > 0);
        assertTrue(comparator.compare(new Object[]{"b", "b"}, new Object[]{"b","b"}) == 0);
    }

}
