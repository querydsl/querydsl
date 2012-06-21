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

}
