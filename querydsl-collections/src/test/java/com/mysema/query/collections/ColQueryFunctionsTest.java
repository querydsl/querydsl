package com.mysema.query.collections;

import static org.junit.Assert.*;

import org.junit.Test;


public class ColQueryFunctionsTest {
    
    @Test
    public void coalesce(){
        assertEquals("1", ColQueryFunctions.coalesce("1",null));
        assertEquals("1", ColQueryFunctions.coalesce(null,"1","2"));
        assertNull(ColQueryFunctions.coalesce(null,null));
    }

}
