package com.mysema.query.support;

import static org.junit.Assert.*;

import org.junit.Test;

public class SerializerBaseTest {
        
    @Test
    public void Normalize_Addition() {
        assertEquals("3", SerializerBase.normalize("1+2"));
        assertEquals("where 3 = 3", SerializerBase.normalize("where 1+2 = 3"));
        assertEquals("where 3.3 = 3.3", SerializerBase.normalize("where 1.1+2.2 = 3.3"));
        assertEquals("where 3.3 = 3.3", SerializerBase.normalize("where 1.1 + 2.2 = 3.3"));
    }
    
    @Test
    public void Normalize_Subtraction() {
        assertEquals("3", SerializerBase.normalize("5-2"));
        assertEquals("where 3 = 3", SerializerBase.normalize("where 5-2 = 3"));
        assertEquals("where 3.3 = 3.3", SerializerBase.normalize("where 5.5-2.2 = 3.3"));
        assertEquals("where 3.3 = 3.3", SerializerBase.normalize("where 5.5 - 2.2 = 3.3"));
    }

    @Test
    public void Normalize_Multiplication() {
        assertEquals("10", SerializerBase.normalize("5*2"));
        assertEquals("where 10 = 10", SerializerBase.normalize("where 5*2 = 10"));
        assertEquals("where 11 = 11", SerializerBase.normalize("where 5.5*2 = 11"));
        assertEquals("where 10.8 = 10.8", SerializerBase.normalize("where 5.4 * 2 = 10.8"));
    } 

    @Test
    public void Normalize_Division() {
        assertEquals("2.5", SerializerBase.normalize("5/2"));
        assertEquals("where 2.5 = 2.5", SerializerBase.normalize("where 5/2 = 2.5"));
        assertEquals("where 2.6 = 2.6", SerializerBase.normalize("where 5.2/2 = 2.6"));
        assertEquals("where 2.6 = 2.6", SerializerBase.normalize("where 5.2 / 2 = 2.6"));
    } 
    
    @Test
    public void Mixed() {
        assertEquals("13", SerializerBase.normalize("2 * 5 + 3"));
        assertEquals("-2.5", SerializerBase.normalize("2.5 * -1"));
    }
    
    @Test
    public void PI() {
        assertEquals("0.15915", SerializerBase.normalize("0.5 / " + Math.PI));
    }
    
}
