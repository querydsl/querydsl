/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql;

import com.mysema.query.serialization.JavaPatterns;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.operation.Ops.MathOps;
import com.mysema.query.types.path.PathType;

/**
 * Operation patterns for JDOQL serialization
 * 
 * @author tiwe
 * 
 */
public class JDOQLPatterns extends JavaPatterns {

    public static final JDOQLPatterns DEFAULT = new JDOQLPatterns();

    public JDOQLPatterns() {
        // String
        add(Ops.STRING_CONTAINS, "%s.indexOf(%s) > -1");
        add(Ops.EQ_IGNORECASE, "%s.toLowerCase().equals(%s.toLowerCase())");

        // FIXME
        add(Ops.LAST_INDEX, "%s.indexOf(%s)");
        // FIXME
        add(Ops.LAST_INDEX_2ARGS, "%s.indexOf(%s,%s)");
        // FIXME
        add(Ops.LIKE, "%s.matches(%s)");
        
     // FIXME
        add(MathOps.MAX, "max(%s, %s)");
     // FIXME
        add(MathOps.MIN, "min(%s, %s)");
        
        // path types
        add(PathType.VARIABLE, "%s");
        for (PathType type : new PathType[] { 
                PathType.LISTVALUE,
                PathType.LISTVALUE_CONSTANT, 
                PathType.MAPVALUE,
                PathType.MAPVALUE_CONSTANT }) {
            add(type, "%s.get(%s)");
        }
        add(PathType.ARRAYVALUE, "%s[%s]");
        add(PathType.ARRAYVALUE_CONSTANT, "%s[%s]");
    }

}
