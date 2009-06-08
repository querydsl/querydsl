/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import com.mysema.query.serialization.JavaPatterns;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.path.PathType;

/**
 * JavaOps extends OperationPatterns to add Java syntax specific operation
 * templates.
 * 
 * @author tiwe
 * @version $Id$
 */
public class ColQueryPatterns extends JavaPatterns {

    public static final ColQueryPatterns DEFAULT = new ColQueryPatterns();

    protected ColQueryPatterns() {
        String functions = ColQueryPatterns.class.getName();
        add(Ops.EQ_OBJECT, "%s.equals(%s)");
        add(Ops.NE_OBJECT, "!%s.equals(%s)");
        add(Ops.INSTANCEOF, "%2$s.isInstance(%1$s)");
        
        // comparable
        add(Ops.AFTER, "%s.compareTo(%s) > 0");
        add(Ops.BEFORE, "%s.compareTo(%s) < 0");
        add(Ops.AOE, "%s.compareTo(%s) >= 0");
        add(Ops.BOE, "%s.compareTo(%s) <= 0");
        add(Ops.BETWEEN, functions + ".between(%s,%s,%s)");
        add(Ops.NOTBETWEEN, "!" + functions + ".between(%s,%s,%s)");
        add(Ops.STRING_CAST, "String.valueOf(%s)");
        
        // Date and Time
        add(Ops.DateTimeOps.HOUR, "%s.getHours()"); 
        add(Ops.DateTimeOps.MINUTE, "%s.getMinutes()");
        add(Ops.DateTimeOps.SECOND, "%s.getSeconds()");
        
        // String
//        add(Ops.LIKE, functions + ".like(%s,%s)");
        
        // path types
        for (PathType type : new PathType[] { PathType.LISTVALUE_CONSTANT }) {
            add(type, "%s.get(%s.intValue())");
        }

        // path types
        for (PathType type : new PathType[] { PathType.LISTVALUE,
                PathType.LISTVALUE_CONSTANT, PathType.MAPVALUE,
                PathType.MAPVALUE_CONSTANT }) {
            add(type, "%s.get(%s)");
        }
        add(PathType.ARRAYVALUE, "%s[%s]");
        add(PathType.ARRAYVALUE_CONSTANT, "%s[%s.intValue()]");
        
        // TEMPORARY FIXES
        
        add(Ops.DIV, "((double)%s) / ((double)%s)");
    }

    public static <A extends Comparable<? super A>> boolean between(A a, A b, A c) {
        return a.compareTo(b) > 0 && a.compareTo(c) < 0;
    }

}
