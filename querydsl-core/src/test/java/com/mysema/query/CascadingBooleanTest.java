/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import org.junit.Test;

import com.mysema.query.alias.Alias;
import com.mysema.query.types.expr.EBoolean;


/**
 * CascadingBooleanTest provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class CascadingBooleanTest {

    @Test
    public void test(){
        EBoolean etrue = Alias.$(true);
        EBoolean efalse = Alias.$(false);
        new CascadingBoolean().and(etrue).or(efalse);
    }
}
