package com.mysema.query.types.path;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.types.ConstantImpl;


public class ArrayPathTest {

    @Test
    public void Get(){
        ArrayPath<String> arrayPath = new ArrayPath<String>(String[].class, "p");
        assertNotNull(arrayPath.get(ConstantImpl.create(0)));
        
    }
    
}
