package com.mysema.query.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NullSafeComparableComparatorTest {

    private final NullSafeComparableComparator<String> comparator = new NullSafeComparableComparator<String>();

    @Test
    public void Null_Before_Object() {
        assertTrue(comparator.compare(null, "X") < 0);
    }

    @Test
    public void Object_After_Null() {
        assertTrue(comparator.compare("X", null) > 0);
    }

    @Test
    public void Object_Eq_Object(){
        assertEquals(0, comparator.compare("X", "X"));
    }

    @Test
    public void Object_Lt_Object(){
        assertTrue(comparator.compare("X", "Y") < 0);
    }

    @Test
    public void Object_Gt_Object(){
        assertTrue(comparator.compare("Z", "Y") > 0);
    }

}
