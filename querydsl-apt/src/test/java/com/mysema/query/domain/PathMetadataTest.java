package com.mysema.query.domain;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.EStringConst;

public class PathMetadataTest {
    
    @Before
    public void setUp(){
        assertNotNull(QAnimal.animal);
        assertNotNull(QCat.cat);
        assertNotNull(QCategory.category);
        assertNotNull(QSimpleTypes.simpleTypes);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void test() throws Exception{        
        Field field = EStringConst.class.getDeclaredField("cache");
        field.setAccessible(true);
        Map<String, EString> cache = (Map) field.get(null);
        System.out.println(cache.size() + " entries in EString cache");
        
        // numbers
        assertTrue(cache.containsKey("0"));
        assertTrue(cache.containsKey("10"));
        
        // variables
        assertTrue(cache.containsKey("animal"));
        assertTrue(cache.containsKey("cat"));
        assertTrue(cache.containsKey("category"));
        assertTrue(cache.containsKey("simpleTypes"));
        
        // properties
        assertTrue(cache.containsKey("mate"));
        
    }

}
