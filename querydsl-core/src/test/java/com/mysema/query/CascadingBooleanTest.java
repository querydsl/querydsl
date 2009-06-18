/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import org.junit.Test;

import com.mysema.query.alias.GrammarWithAlias;
import com.mysema.query.support.CascadingBoolean;
import com.mysema.query.types.expr.EBoolean;


/**
 * CascadingBooleanTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class CascadingBooleanTest {
    
    @Test
    public void test(){
        EBoolean etrue = GrammarWithAlias.$(true);
        EBoolean efalse = GrammarWithAlias.$(false);
        new CascadingBoolean().and(etrue).or(efalse).create();
    }

}
