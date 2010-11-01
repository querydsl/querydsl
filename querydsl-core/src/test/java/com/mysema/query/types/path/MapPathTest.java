package com.mysema.query.types.path;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.types.ConstantImpl;

public class MapPathTest {

    private MapPath<String,String,StringPath> mapPath = new MapPath<String,String,StringPath>(String.class, String.class, StringPath.class, "p");
    
    @Test
    public void Get() {
        assertNotNull(mapPath.get("X"));
        assertNotNull(mapPath.get(ConstantImpl.create("X")));
    }
    
    @Test
    public void GetKeytType(){
        assertEquals(String.class, mapPath.getKeyType());
    }

    @Test
    public void GetValueType(){
        assertEquals(String.class, mapPath.getValueType());
    }
    
    @Test
    public void GetParameter(){
        assertEquals(String.class, mapPath.getParameter(0));
        assertEquals(String.class, mapPath.getParameter(1));
    }

}
