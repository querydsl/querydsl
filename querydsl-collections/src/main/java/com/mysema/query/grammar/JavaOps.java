/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import java.util.regex.Pattern;

import com.mysema.query.grammar.types.PathMetadata;
import com.mysema.query.grammar.types.PathMetadata.PathType;
import com.mysema.query.serialization.OperationPatterns;

/**
 * JavaOps extends OperationPatterns to add Java syntax specific operation templates.
 * 
 * @author tiwe
 * @version $Id$
 */
public class JavaOps extends OperationPatterns {
    
    public JavaOps(){       
        String functions = JavaOps.class.getName();
        
        add(Ops.BETWEEN, functions+".between(%s,%s,%s)");
        add(Ops.NOTBETWEEN, "!"+functions+".between(%s,%s,%s)");
        add(Ops.SQRT, "Math.sqrt(%s)");
                
        add(Ops.EQ_PRIMITIVE, "%s == %s");
        add(Ops.EQ_OBJECT, "%s.equals(%s)");      
        add(Ops.NE_OBJECT, "!%s.equals(%s)");  
        
        add(Ops.ISNULL, "%s == null");
        add(Ops.ISNOTNULL, "%s != null");
                
        add(Ops.ISTYPEOF, "%s.class.equals(%s)");
        add(Ops.IN, "%2$s.contains(%1$s)"); 
        add(Ops.NOTIN, "!%2$s.contains(%1$s)");        
        add(Ops.LIKE, functions+".like(%s,%s)");
        add(Ops.LOWER, "%s.toLowerCase()");     
        add(Ops.SPLIT, "%s.split(%s)");
        add(Ops.SUBSTR1ARG, "%s.substring(%s)");
        add(Ops.SUBSTR2ARGS, "%s.substring(%s,%s)");
        add(Ops.TRIM, "%s.trim()");
        add(Ops.UPPER, "%s.toUpperCase()");
        
        // path types
        for (PathType type : new PathType[]{PathMetadata.LISTVALUE, PathMetadata.LISTVALUE_CONSTANT, PathMetadata.MAPVALUE, PathMetadata.MAPVALUE_CONSTANT}){
            add(type,"%s.get(%s)");    
        }
        add(PathMetadata.SIZE,"%s.size()");
        
    }
    
    public static <A extends Comparable<A>> boolean between(A a, A b, A c){
        return a.compareTo(b) > 0 && a.compareTo(c) < 0;
    }
        
    public static boolean like(String source, String pattern){
        return Pattern.compile(pattern.replace("%", ".*")).matcher(source).matches();
    }
    
}
