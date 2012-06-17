package com.mysema.query.support;

import static org.junit.Assert.*;

import org.junit.Test;

public class SerializerBaseTest {
    
    @Test
    public void Normalize() {
        assertEquals("3", SerializerBase.normalize("1+2"));
        assertEquals("where 3 = 3", SerializerBase.normalize("where 1+2 = 3"));
        assertEquals("where 3.3 = 3.3", SerializerBase.normalize("where 1.1+2.2 = 3.3"));
        assertEquals("where 3.3 = 3.3", SerializerBase.normalize("where 1.1 + 2.2 = 3.3"));
    }

}
