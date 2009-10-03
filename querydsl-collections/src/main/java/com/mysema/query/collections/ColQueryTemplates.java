/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import com.mysema.query.serialization.JavaTemplates;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.path.PathType;

/**
 * JavaOps extends OperationPatterns to add Java syntax specific operation
 * templates.
 * 
 * @author tiwe
 * @version $Id$
 */
public class ColQueryTemplates extends JavaTemplates {

    public ColQueryTemplates() {
        String functions = ColQueryTemplates.class.getName();
        add(Ops.EQ_OBJECT, "{0}.equals({1})");
        add(Ops.NE_OBJECT, "!{0}.equals({1})");
        add(Ops.INSTANCE_OF, "{1}.isInstance({0})");
        
        // Comparable
        add(Ops.AFTER, "{0}.compareTo({1}) > 0");
        add(Ops.BEFORE, "{0}.compareTo({1}) < 0");
        add(Ops.AOE, "{0}.compareTo({1}) >= 0");
        add(Ops.BOE, "{0}.compareTo({1}) <= 0");
        add(Ops.BETWEEN, functions + ".between({0},{1},{2})");
//        add(Ops.BETWEEN, "{0}.compareTo({1}) > 0 && {0}.compareTo({2}) < 0");
        add(Ops.STRING_CAST, "String.valueOf({0})");
        
        // Date and Time
        add(Ops.DateTimeOps.DAY_OF_MONTH, "{0}.getDate()");  // java.util.Date
        add(Ops.DateTimeOps.DAY_OF_WEEK, "{0}.getDay()");    // java.util.Date
        add(Ops.DateTimeOps.HOUR, "{0}.getHours()");         // java.util.Date
        add(Ops.DateTimeOps.MINUTE, "{0}.getMinutes()");     // java.util.Date
        add(Ops.DateTimeOps.SECOND, "{0}.getSeconds()");     // java.util.Date
        
        // String
        add(Ops.LIKE, functions + ".like({0},{1})");
        
        // Path types
        for (PathType type : new PathType[] { 
                PathType.LISTVALUE,
                PathType.MAPVALUE,
                PathType.MAPVALUE_CONSTANT }) {
            add(type, "{0}.get({1})");
        }
        add(PathType.LISTVALUE_CONSTANT, "{0}.get({1s})");
        add(PathType.ARRAYVALUE, "{0}[{1}]");
        add(PathType.ARRAYVALUE_CONSTANT, "{0}[{1s}]");
        
        // TEMPORARY FIXES
        
        add(Ops.DIV, "((double){0}) / ((double){1})");
    }
    
    public static boolean like(String str, String like){
        return str.matches(like.replace("%", ".*").replace('_', '.'));
    }

    public static <A extends Comparable<? super A>> boolean between(A a, A b, A c) {
        return a.compareTo(b) > 0 && a.compareTo(c) < 0;
    }

}
