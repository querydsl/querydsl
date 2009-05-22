/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import org.joda.time.LocalDate;
import org.junit.Test;

import com.mysema.query.alias.GrammarWithAlias;
import com.mysema.query.types.expr.EComparable;

/**
 * JodaTimeTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class JodaTimeTest {
    
    @Test
    public void test(){
        EComparable<LocalDate> expr = GrammarWithAlias.$(new LocalDate());
        expr.after(expr);
        expr.after(new LocalDate());
        expr.before(expr);
        expr.before(new LocalDate());
    }

}

