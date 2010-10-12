package com.mysema.query.types.path;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.PathMetadataFactory;

public class ListPathTest {
    
    @Test
    public void test(){
        ListPath<String,StringPath> stringPath = new ListPath<String,StringPath>(
                String.class, StringPath.class, 
                PathMetadataFactory.forVariable("stringPath"));
        assertEquals("stringPath", stringPath.toString());
        assertEquals("stringPath", stringPath.any().toString());
        assertEquals("eqIc(stringPath.get(0),X)", stringPath.get(0).equalsIgnoreCase("X").toString());
        assertEquals("eqIc(stringPath,X)", stringPath.any().equalsIgnoreCase("X").toString());
        assertEquals("stringPath.get(0)", stringPath.get(ConstantImpl.create(0)).toString());
        
    }

}
