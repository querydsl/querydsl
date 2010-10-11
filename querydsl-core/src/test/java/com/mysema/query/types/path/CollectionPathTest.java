package com.mysema.query.types.path;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.types.PathMetadataFactory;

public class CollectionPathTest {
    
    @Test
    public void test(){
        CollectionPath<String,StringPath> stringPath = new CollectionPath<String,StringPath>(
                String.class, StringPath.class, 
                PathMetadataFactory.forVariable("stringPath"));
        assertEquals("stringPath", stringPath.toString());
        assertEquals("stringPath", stringPath.any().toString());
        assertEquals("eqIc(stringPath,X)", stringPath.any().equalsIgnoreCase("X").toString());
    }

}
