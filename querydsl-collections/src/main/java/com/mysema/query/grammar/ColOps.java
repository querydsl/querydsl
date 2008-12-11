/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import java.util.regex.Pattern;

import com.mysema.query.grammar.Ops;
import com.mysema.query.grammar.types.PathMetadata;
import com.mysema.query.grammar.types.PathMetadata.PathType;
import com.mysema.query.serialization.BaseOps;

/**
 * ColOps extends BaseOps to add Java syntax specific operation templates.
 * 
 * @author tiwe
 * @version $Id$
 */
public class ColOps extends BaseOps {
    
    public ColOps(){       
        String functions = ColOps.class.getName();
        
        patterns.put(Ops.BETWEEN, functions+".between(%s,%s,%s)");
        patterns.put(Ops.NOTBETWEEN, "!"+functions+".between(%s,%s,%s)");
        patterns.put(Ops.SQRT, "Math.sqrt(%s)");
        
        patterns.put(Ops.EQ, "%s.equals(%s)");
        patterns.put(Ops.NE, "!%s.equals(%s)");  
        
        patterns.put(Ops.ISTYPEOF, "%s.class.equals(%s)");
        patterns.put(Ops.IN, "%2$s.contains(%1$s)"); 
        patterns.put(Ops.NOTIN, "!%2$s.contains(%1$s)");        
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
        
    }
    
    public static <A extends Comparable<A>> boolean between(A a, A b, A c){
        return a.compareTo(b) > 0 && a.compareTo(c) < 0;
    }
        
    public static boolean like(String source, String pattern){
        return Pattern.compile(pattern.replace("%", ".*")).matcher(source).matches();
    }
    
}
