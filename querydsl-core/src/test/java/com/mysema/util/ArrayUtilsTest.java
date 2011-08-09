package com.mysema.util;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class ArrayUtilsTest {
    
    @Test
    public void test() {
        Object[] array = ArrayUtils.combine(5, new Object[]{"a","b"}, new Object[]{"c","d","e"});
        assertEquals(Arrays.asList("a","b","c","d","e"), Arrays.asList(array));
    }

}
