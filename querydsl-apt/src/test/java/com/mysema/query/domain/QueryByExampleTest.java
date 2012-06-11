package com.mysema.query.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.annotations.QueryDelegate;
import com.mysema.query.types.Predicate;


public class QueryByExampleTest {
        
    @QueryDelegate(ExampleEntity.class)
    public static Predicate like(QExampleEntity qtype, ExampleEntity example) {
        return example.name != null ? qtype.name.eq(example.name) : null; 
    }
    
    @Test
    public void Name_Not_Set() {
        ExampleEntity entity = new ExampleEntity();
        Predicate qbe = QExampleEntity.exampleEntity.like(entity);
        assertNull(qbe);
    }
    
    @Test
    public void Name_Set() {
        ExampleEntity entity = new ExampleEntity();
        entity.name = "XXX";
        Predicate qbe = QExampleEntity.exampleEntity.like(entity);
        assertEquals("exampleEntity.name = XXX", qbe.toString());
    }

}
