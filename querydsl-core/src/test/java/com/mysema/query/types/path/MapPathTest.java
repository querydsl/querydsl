package com.mysema.query.types.path;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.types.ConstantImpl;

public class MapPathTest {

    @Test
    public void Get() {
        MapPath<String,String,StringPath> mapPath = new MapPath<String,String,StringPath>(String.class, String.class, StringPath.class, "p");
        assertNotNull(mapPath.get("X"));
        assertNotNull(mapPath.get(ConstantImpl.create("X")));
    }

}
