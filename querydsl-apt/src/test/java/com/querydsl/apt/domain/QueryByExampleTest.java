package com.querydsl.apt.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.querydsl.core.annotations.QueryDelegate;
import com.querydsl.core.types.Predicate;

public class QueryByExampleTest {

    @QueryDelegate(ExampleEntity.class)
    public static Predicate like(QExampleEntity qtype, ExampleEntity example) {
        return example.name != null ? qtype.name.eq(example.name) : null;
    }

    @Test
    public void name_not_set() {
        ExampleEntity entity = new ExampleEntity();
        Predicate qbe = QExampleEntity.exampleEntity.like(entity);
        assertNull(qbe);
    }

    @Test
    public void name_set() {
        ExampleEntity entity = new ExampleEntity();
        entity.name = "XXX";
        Predicate qbe = QExampleEntity.exampleEntity.like(entity);
        assertEquals("exampleEntity.name = XXX", qbe.toString());
    }

}
