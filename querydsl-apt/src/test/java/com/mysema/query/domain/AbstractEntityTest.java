package com.mysema.query.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryInit;

public class AbstractEntityTest {
    
    
    @QueryEntity
    public static abstract class Category<T extends Category<T>> {
        
        public Category<T> defaultChild;
        
    }

    @QueryEntity
    public static class CategoryReference{
        
        @QueryInit("defaultChild")
        public Category<?> category;
                
    }

    
    @Test
    public void test(){
        QAbstractEntityTest_CategoryReference categoryReference = QAbstractEntityTest_CategoryReference.categoryReference;
        assertNotNull(categoryReference.category.defaultChild);
    }
    
}
