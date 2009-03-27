package com.mysema.query.collections.support;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.mysema.query.collections.MiniApi;
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
    
    @Test
    public void test(){
        SimpleIteratorSource source = new SimpleIteratorSource();
        source.add(str, strings);
        
        CustomQueryable queryable = new CustomQueryable(source);
        
    }
}
