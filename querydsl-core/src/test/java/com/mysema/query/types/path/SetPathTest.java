package com.mysema.query.types.path;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.types.PathMetadataFactory;

public class SetPathTest {
    
    @Test
    public void test(){
        SetPath<String,StringPath> stringPath = new SetPath<String,StringPath>(
                String.class, StringPath.class, 
                PathMetadataFactory.forVariable("stringPath"));
        assertEquals("stringPath", stringPath.toString());
        assertEquals("stringPath", stringPath.any().toString());
        assertEquals("eqIc(stringPath,X)", stringPath.any().equalsIgnoreCase("X").toString());
    }

}
