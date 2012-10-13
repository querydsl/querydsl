package com.mysema.query.domain;

import static org.junit.Assert.*;

import org.junit.Test;

public class PathTest {
    
    @Test
    public void test() {
        assertEquals(Path.class, QPath.path.getType());
    }

}
