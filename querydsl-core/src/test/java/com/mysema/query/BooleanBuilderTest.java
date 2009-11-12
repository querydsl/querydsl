/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import org.junit.Test;

import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EBooleanConst;


/**
 * CascadingBooleanTest provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class BooleanBuilderTest {

    private EBoolean first = EBooleanConst.TRUE;
    
    private EBoolean second = EBooleanConst.FALSE;
    
    @Test
    public void test(){
        new BooleanBuilder().and(first).or(second);
    }
    
    @Test
    public void advanced(){
        BooleanBuilder builder = new BooleanBuilder();
        builder.andAnyOf(first, second, first);
        builder.orAllOf(first, second, first);
        System.out.println(builder);
    }
}
