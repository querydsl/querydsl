/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import org.joda.time.LocalDate;
import org.junit.Test;

import com.mysema.query.alias.Alias;
import com.mysema.query.types.expr.EComparable;

// TODO: Auto-generated Javadoc
/**
 * JodaTimeTest provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class JodaTimeTest {
    
    /**
     * Test.
     */
    @Test
    public void test(){
        EComparable<LocalDate> expr = Alias.$(new LocalDate());
        expr.after(expr);
        expr.after(new LocalDate());
        expr.before(expr);
        expr.before(new LocalDate());
    }

}

