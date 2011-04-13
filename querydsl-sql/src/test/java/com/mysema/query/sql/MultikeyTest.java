package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class MultikeyTest {

    Multikey multiKey1 = new Multikey();
    Multikey multiKey2 = new Multikey();
    
    @Test
    public void HashCode(){
        int hashCode = multiKey1.hashCode();
        multiKey1.setId(1);
        assertEquals(hashCode, multiKey1.hashCode());
        
        multiKey1.setId2("2");
        multiKey1.setId3(3);
        
        multiKey2.setId(1);
        multiKey2.setId2("2");
        multiKey2.setId3(3);
        
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());                
    }
    
    @Test
    public void Equals(){
        multiKey1.setId(1);
        multiKey1.setId2("2");
        multiKey1.setId3(3);
        
        assertFalse(multiKey1.equals(multiKey2));
        multiKey2.setId(1);        
        assertFalse(multiKey1.equals(multiKey2));
        
        multiKey2.setId2("2");
        multiKey2.setId3(3);
        
        assertEquals(multiKey1, multiKey2);
    }
    
    @Test
    public void ToString(){
        multiKey1.setId(1);
        multiKey1.setId2("2");
        multiKey1.setId3(3);
        assertEquals("Multikey#1;2;3", multiKey1.toString());
    }
    
}
