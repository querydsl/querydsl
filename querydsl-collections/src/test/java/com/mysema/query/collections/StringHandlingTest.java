/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import static com.mysema.query.grammar.GrammarWithAlias.$;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.Test;

/**
 * StringHandlingTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class StringHandlingTest extends AbstractQueryTest {

    @Test
    public void test(){
        Iterable<String> data1 = Arrays.asList("petER", "THomas", "joHAN");
        Iterable<String> data2 = Arrays.asList("PETer", "thOMAS", "JOhan");
        
        Iterator<String> res = Arrays.asList("petER - PETer","THomas - thOMAS", "joHAN - JOhan").iterator();
        for (Object[] arr : query().from($("a"), data1).from($("b"), data2).where($("a").equalsIgnoreCase($("b"))).list($("a"),$("b"))){
            assertEquals(res.next(), arr[0]+" - "+arr[1]);
        }
    }
    
}
