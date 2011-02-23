package com.mysema.query.util;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.Test;

import com.mysema.commons.lang.IteratorAdapter;
import com.mysema.query.QueryModifiers;


public class LimitingIteratorTest {

    @Test
    public void Limit(){
        LimitingIterator<String> it = new LimitingIterator<String>(Arrays.asList("1","2","3").iterator(), 2);
        assertEquals(Arrays.asList("1","2"), IteratorAdapter.asList(it));
    }

    @Test
    public void Offset(){
        Iterator<String> it = LimitingIterator.create(Arrays.asList("1","2","3").iterator(), QueryModifiers.offset(1));
        assertEquals(Arrays.asList("2","3"), IteratorAdapter.asList(it));
    }

}
