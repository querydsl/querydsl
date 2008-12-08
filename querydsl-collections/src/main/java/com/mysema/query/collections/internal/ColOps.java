/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.mysema.query.grammar.Ops;
import com.mysema.query.grammar.Ops.Op;
import com.mysema.query.grammar.types.PathMetadata;
import com.mysema.query.grammar.types.PathMetadata.PathType;

/**
 * HqlOps provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class ColOps  {
    
    private static final Map<Op<?>,java.lang.String> patterns = new HashMap<Op<?>,java.lang.String>();
    
    
    static{       
        String functions = ColOps.class.getName();
        // boolean
        patterns.put(Ops.AND, "%s && %s");
        patterns.put(Ops.NOT, "!%s");
        patterns.put(Ops.OR, "%s || %s");
        patterns.put(Ops.XNOR, "!(%s ^ %s)");        
        patterns.put(Ops.XOR, "%s ^ %s");
        
        // comparison
        patterns.put(Ops.BETWEEN, functions+".between(%s,%s,%s)");
        patterns.put(Ops.NOTBETWEEN, "!"+functions+".between(%s,%s,%s)");
        patterns.put(Ops.GOE, "%s >= %s");
        patterns.put(Ops.GT, "%s > %s");
        patterns.put(Ops.LOE, "%s <= %s");
        patterns.put(Ops.LT, "%s < %s");
           
        patterns.put(Ops.AFTER, "%s > %s");
        patterns.put(Ops.BEFORE, "%s < %s");
        
        // numeric
        patterns.put(Ops.ADD, "%s + %s");        
        patterns.put(Ops.DIV, "%s / %s");
        patterns.put(Ops.MOD, "%s % %s");
        patterns.put(Ops.MULT,"%s * %s");
        patterns.put(Ops.SUB, "%s - %s");
        patterns.put(Ops.SQRT, "Math.sqrt(%s)");
        
        // numeric aggregates
//        patterns.put(OpNumberAgg.AVG, "avg(%s)");
//        patterns.put(OpNumberAgg.MAX, "max(%s)");
//        patterns.put(OpNumberAgg.MIN, "min(%s)");
        
        // various
        patterns.put(Ops.EQ, "%s.equals(%s)");
        patterns.put(Ops.ISTYPEOF, "%s.class.equals(%s)");
        patterns.put(Ops.NE, "%s != %s");
        patterns.put(Ops.IN, "%2$s.contains(%1$s)"); 
        patterns.put(Ops.NOTIN, "!%2$s.contains(%1$s)");        
        patterns.put(Ops.ISNULL, "%s == null");
        patterns.put(Ops.ISNOTNULL, "%s != null");
        
        // string
        patterns.put(Ops.CONCAT, "%s + %s");
        patterns.put(Ops.LIKE, functions+".like(%s,%s)");
        patterns.put(Ops.LOWER, "%s.toLowerCase()");        
        patterns.put(Ops.SUBSTR1ARG, "%s.substring(%s)");
        patterns.put(Ops.SUBSTR2ARGS, "%s.substring(%s,%s)");
        patterns.put(Ops.TRIM, "%s.trim()");
        patterns.put(Ops.UPPER, "%s.toUpperCase()");
                
        // path types
        for (PathType type : new PathType[]{PathMetadata.LISTVALUE, PathMetadata.LISTVALUE_CONSTANT, PathMetadata.MAPVALUE, PathMetadata.MAPVALUE_CONSTANT}){
            patterns.put(type,"%s.get(%s)");    
        }
        patterns.put(PathMetadata.PROPERTY,"%s.%s"); 
        patterns.put(PathMetadata.SIZE,"%s.size()");
        patterns.put(PathMetadata.VARIABLE,"%s"); 
        
    }

    public static java.lang.String getPattern(Op<?> op){
        return patterns.get(op);
    }

    public static <A extends Comparable<A>> boolean between(A a, A b, A c){
        return a.compareTo(b) > 0 && a.compareTo(c) < 0;
    }
        
    public static boolean like(String source, String pattern){
        return Pattern.compile(pattern.replace("%", ".*")).matcher(source).matches();
    }
    
}
