package com.mysema.query.types.template;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BooleanTemplateTest {
    
    @Test
    public void True(){
        assertEquals("true", BooleanTemplate.TRUE.toString());
    }
    
    @Test
    public void False(){
        assertEquals("false", BooleanTemplate.FALSE.toString());
    }

}
