package com.mysema.query.domain;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;

public class AnnotatedGettersTest {
    
    @QueryEntity
    public interface Entity{
        String getName();
    }
    
    @Test
    public void annotated_getter_is_supported(){
        assertNotNull(QAnnotatedGettersTest_Entity.entity.name);
    }

}
