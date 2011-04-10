package com.mysema.query.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.annotations.QueryEmbeddable;
import com.mysema.query.annotations.QueryEntity;

public class ComparableTest {
    
    @QueryEntity
    public static class CustomComparable implements Comparable<CustomComparable>{

        @Override
        public int compareTo(CustomComparable o) {
            return 0;
        }
        
    }
    
    @QueryEmbeddable
    public static class CustomComparable2 implements Comparable<CustomComparable2>{

        @Override
        public int compareTo(CustomComparable2 o) {
            return 0;
        }
        
    }
    
    @Test
    public void CustomComparable_is_Properly_Handled(){
        assertNotNull(QComparableTest_CustomComparable.customComparable.asc());
    }

}
