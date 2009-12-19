/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql;

import com.mysema.query.serialization.JavaTemplates;
import com.mysema.query.types.operation.Ops;

/**
 * JDOQLTemplates provides patterns for JDOQL serialization
 * 
 * @author tiwe
 * 
 */
public class JDOQLTemplates extends JavaTemplates {

    public static final JDOQLTemplates DEFAULT = new JDOQLTemplates();
    
    protected JDOQLTemplates() {
        // String
        add(Ops.STRING_CONTAINS, "{0}.indexOf({1}) > -1", 25);
        add(Ops.STRING_CONTAINS_IC, "{0l}.indexOf({1l}) > -1", 25);
        add(Ops.EQ_IGNORE_CASE, "{0l}.equals({1l})");
        add(Ops.STRING_IS_EMPTY, "{0} == \"\"", 25);
        add(Ops.LIKE, "{0}.like({1})");
        
        add(Ops.STRING_CAST, "(String){0}");
        
        // Date
        add(Ops.DateTimeOps.MONTH, "({0}.getMonth() + 1)"); // getMonth() in JDO returns a range from 0-11
        add(Ops.DateTimeOps.YEAR_MONTH, "({0}.getYear() * 100 + {0}.getMonth() + 1)"); 
        add(Ops.DateTimeOps.DAY_OF_MONTH, "{0}.getDay()");
        add(Ops.DateTimeOps.MILLISECOND, "0"); // NOT supported in JDOQL
        
        // other
        add(Ops.ALIAS, "{0} {1}");
    }

}
