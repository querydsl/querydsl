/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

import com.mysema.query.grammar.Ops.Op;
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
    
    public static final JavaOps DEFAULT = new JavaOps();
    
    public JavaOps(){       
        String functions = JavaOps.class.getName();
        
        add(Ops.AFTER, "%s.compareTo(%s) > 0");
        add(Ops.BEFORE, "%s.compareTo(%s) < 0");
        add(Ops.AOE, "%s.compareTo(%s) >= 0");
        add(Ops.BOE, "%s.compareTo(%s) <= 0");
        
        add(Ops.BETWEEN, functions+".between(%s,%s,%s)");
        add(Ops.NOTBETWEEN, "!"+functions+".between(%s,%s,%s)");        
                
        add(Ops.EQ_PRIMITIVE, "%s == %s");
        add(Ops.EQ_OBJECT, "%s.equals(%s)");      
        add(Ops.NE_OBJECT, "!%s.equals(%s)");  
        
        add(Ops.ISNULL, "%s == null");
        add(Ops.ISNOTNULL, "%s != null");
                
        add(Ops.ISTYPEOF, "%s.class.equals(%s)");
        add(Ops.IN, "%2$s.contains(%1$s)"); 
        add(Ops.NOTIN, "!%2$s.contains(%1$s)");        
        add(Ops.LIKE, functions+".like(%s,%s)");
        
        // java.lang.String
        add(Ops.CHAR_AT, "%s.charAt(%s)");
        add(Ops.LOWER, "%s.toLowerCase()");     
        add(Ops.SPLIT, "%s.split(%s)");
        add(Ops.SUBSTR1ARG, "%s.substring(%s)");
        add(Ops.SUBSTR2ARGS, "%s.substring(%s,%s)");
        add(Ops.TRIM, "%s.trim()");
        add(Ops.UPPER, "%s.toUpperCase()");        
        add(Ops.MATCHES, "%s.matches(%s)");
        add(Ops.STRING_LENGTH, "%s.length(%s)");
        add(Ops.LAST_INDEX_2ARGS, "%s.lastIndex(%s)");
        add(Ops.LAST_INDEX, "%s.lastIndex(%s,%s)");
        add(Ops.ISEMPTY, "%s.isEmpty()");
        add(Ops.STARTSWITH, "%s.startsWith(%s, 0)");
        add(Ops.INDEXOF_2ARGS, "%s.indexOf(%s,%s)");
        add(Ops.INDEXOF, "%s.indexOf(%s)");
        add(Ops.EQ_IGNORECASE, "%s.equalsIgnoreCase(%s)");
        add(Ops.ENDSWITH, "%s.endsWith(%s)");
        add(Ops.CONTAINS, "%s.contains(%s)");
        
        // math        
        try {
            for (Field f : Ops.OpMath.class.getFields()){
                Op<?> op = (Op<?>) f.get(null);
                add(op, "Math."+getPattern(op));
            }
        } catch (Exception e) {
            String error = "Caught " + e.getClass().getName();
            throw new RuntimeException(error, e);
        }        
        add(Ops.OpMath.MOD, "%s %% %s");
        
        // path types
        for (PathType type : new PathType[]{PathMetadata.LISTVALUE, PathMetadata.LISTVALUE_CONSTANT, PathMetadata.MAPVALUE, PathMetadata.MAPVALUE_CONSTANT}){
            add(type,"%s.get(%s)");    
        }        
        add(PathMetadata.ARRAYVALUE, "%s[%s]");
        add(PathMetadata.ARRAYVALUE_CONSTANT, "%s[%s]");
        
        add(PathMetadata.ARRAY_SIZE,"%s.length");
        add(PathMetadata.SIZE,"%s.size()");
        
    }
    
    public static <A extends Comparable<? super A>> boolean between(A a, A b, A c){
        return a.compareTo(b) > 0 && a.compareTo(c) < 0;
    }
            
    public static boolean like(String source, String pattern){
        return Pattern.compile(pattern.replace("%", ".*").replace("_",".")).matcher(source).matches();
    }
    
}
