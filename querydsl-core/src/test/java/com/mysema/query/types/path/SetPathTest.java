package com.mysema.query.types.path;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.types.PathMetadataFactory;

public class SetPathTest {
    
    private SetPath<String,StringPath> stringPath = new SetPath<String,StringPath>(
            String.class, StringPath.class, 
            PathMetadataFactory.forVariable("stringPath"));
    
    @Test
    public void ToString(){        
        assertEquals("stringPath", stringPath.toString());
        assertEquals("any(stringPath)", stringPath.any().toString());
        assertEquals("eqIc(any(stringPath),X)", stringPath.any().equalsIgnoreCase("X").toString());
    }
    
    @Test
    public void GetElementType(){
        assertEquals(String.class, stringPath.getElementType());
    }
    
    @Test
    public void GetParameter(){        
        assertEquals(String.class, stringPath.getParameter(0));
    }

}
