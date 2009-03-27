package com.mysema.query.collections.support;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.mysema.query.grammar.types.Path.PString;


/**
 * CustomQueryableTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class CustomQueryableTest {

    private List<String> strings = Arrays.asList("1","2","3");
    
    private PString str = new PString("str");
    
    private SimpleIteratorSource source;
    
    @Before 
    public void setUp(){
        source = new SimpleIteratorSource();
        source.add(str, strings);           
    }
    
    private CustomQueryable query(){
        return new CustomQueryable(source);
    }
    
    @Test
    public void test1(){        
        assertEquals(strings, query().from(str).list(str));
        assertEquals("1", query().from(str).where(str.eq("1")).uniqueResult(str));
        
    }
}
