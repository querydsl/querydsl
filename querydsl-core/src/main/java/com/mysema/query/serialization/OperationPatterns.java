/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.serialization;

import java.util.HashMap;
import java.util.Map;

import com.mysema.query.grammar.Ops;
import com.mysema.query.grammar.Ops.Op;
import com.mysema.query.grammar.Ops.OpNumberAgg;
import com.mysema.query.grammar.types.PathMetadata;
import com.mysema.query.grammar.types.PathMetadata.PathType;

/**
 * OperationPatterns provides operator patterns for SQL/HQL serialization
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class OperationPatterns{
   
    private final Map<Op<?>,String> patterns = new HashMap<Op<?>,String>();
        
    private final Map<Op<?>,Integer> precedence = new HashMap<Op<?>,Integer>();
    
    public OperationPatterns(){            
        // boolean
        add(Ops.AND, "%s && %s",36);
        add(Ops.NOT, "!%s",3);
        add(Ops.OR, "%s || %s",38);
        add(Ops.XNOR, "%s xnor %s",39);        
        add(Ops.XOR, "%s xor %s",39);
        
        // comparison
        add(Ops.BETWEEN, "%s between %s and %s",30);
        add(Ops.NOTBETWEEN, "%s not between %s and %s",30);
        add(Ops.GOE, "%s >= %s",20);
        add(Ops.GT, "%s > %s",21);
        add(Ops.LOE, "%s <= %s",22);
        add(Ops.LT, "%s < %s",23);        
        
        add(Ops.AFTER, "%s > %s",21);
        add(Ops.BEFORE, "%s < %s",23);
        add(Ops.AOE, "%s >= %s",21);
        add(Ops.BOE, "%s <= %s",23);
        
        // numeric
        add(Ops.ADD, "%s + %s",13);        
        add(Ops.DIV, "%s / %s",8);
        add(Ops.MOD, "%s % %s",10);
        add(Ops.MULT,"%s * %s",7);
        add(Ops.SUB, "%s - %s",12);
//        add(Ops.SQRT, "sqrt(%s)");
        
        // various
        add(Ops.EQ_PRIMITIVE, "%s = %s",18);
        add(Ops.EQ_OBJECT, "%s = %s",18);
        add(Ops.ISTYPEOF, "%s.class = %s");
        add(Ops.NE_PRIMITIVE, "%s != %s",25);
        add(Ops.NE_OBJECT, "%s != %s",25);
        add(Ops.IN, "%s in %s");
        add(Ops.NOTIN, "%s not in %s");        
        add(Ops.ISNULL, "%s is null",26);
        add(Ops.ISNOTNULL, "%s is not null",26);
        
        add(Ops.EXISTS, "exists(%s)");
        
        // string
        add(Ops.CONCAT, "%s + %s",37);
        add(Ops.LIKE, "%s like %s",27);
        add(Ops.LOWER, "lower(%s)");        
        add(Ops.SUBSTR1ARG, "substring(%s,%s)");
        add(Ops.SUBSTR2ARGS, "substring(%s,%s,%s)");
        add(Ops.TRIM, "trim(%s)");
        add(Ops.UPPER, "upper(%s)");
        
        // date time
        add(Ops.OpDateTime.SYSDATE, "sysdate");
        add(Ops.OpDateTime.CURRENT_DATE, "current_date()");
        add(Ops.OpDateTime.CURRENT_TIME, "current_time()");
        add(Ops.OpDateTime.CURRENT_TIMESTAMP, "current_timestamp()");
        add(Ops.OpDateTime.SECOND, "second(%s)");
        add(Ops.OpDateTime.MINUTE, "minute(%s)");
        add(Ops.OpDateTime.HOUR, "hour(%s)");
        add(Ops.OpDateTime.DAY, "day(%s)");
        add(Ops.OpDateTime.WEEK, "week(%s)");
        add(Ops.OpDateTime.MONTH, "month(%s)");
        add(Ops.OpDateTime.YEAR, "year(%s)");
        add(Ops.OpDateTime.DAY_OF_WEEK, "dayofweek(%s)");
        add(Ops.OpDateTime.DAY_OF_MONTH, "dayofmonth(%s)");
        add(Ops.OpDateTime.DAY_OF_YEAR, "dayofyear(%s)");
        
        // math
        add(Ops.OpMath.ABS,"abs(%s)");
        add(Ops.OpMath.ACOS,"acos(%s)");
        add(Ops.OpMath.ASIN,"asin(%s)");
        add(Ops.OpMath.ATAN,"atan(%s)");
        add(Ops.OpMath.CEIL,"ceil(%s)");
        add(Ops.OpMath.COS,"cos(%s)");
        add(Ops.OpMath.TAN,"tan(%s)");
        add(Ops.OpMath.SQRT,"sqrt(%s)");
        add(Ops.OpMath.SIN,"sin(%s)");
        add(Ops.OpMath.ROUND,"round(%s)");
        add(Ops.OpMath.RANDOM,"random()");
        add(Ops.OpMath.POWER,"pow(%s,%s)");
        add(Ops.OpMath.MIN,"min(%s,%s)");
        add(Ops.OpMath.MAX,"max(%s,%s)");
        add(Ops.OpMath.MOD,"mod(%s,%s)");
        add(Ops.OpMath.LOG10,"log10(%s)");
        add(Ops.OpMath.LOG,"log(%s)");
        add(Ops.OpMath.FLOOR,"floor(%s)");               
        add(Ops.OpMath.EXP,"exp(%s)");
        
        // string 
        add(Ops.OpString.LENGTH, "length(%s)");
        add(Ops.OpString.LTRIM, "ltrim(%s)");
        add(Ops.OpString.RTRIM, "rtrim(%s)");
        add(Ops.OpString.SPACE, "space(%s)");
                
        // path types
        for (PathType type : new PathType[]{PathMetadata.LISTVALUE, PathMetadata.LISTVALUE_CONSTANT, PathMetadata.MAPVALUE, PathMetadata.MAPVALUE_CONSTANT}){
            add(type,"%s.get(%s)");    
        }
        add(PathMetadata.PROPERTY,"%s.%s"); 
        add(PathMetadata.SIZE,"%s.size");
        add(PathMetadata.VARIABLE,"%s"); 
        

        // numeric aggregates
        add(OpNumberAgg.AVG, "avg(%s)");
        add(OpNumberAgg.MAX, "max(%s)");
        add(OpNumberAgg.MIN, "min(%s)");
        
    }
    
    protected void add(Op<?> op, String pattern){
        patterns.put(op, pattern);             
    }
    
    protected void add(Op<?> op, String pattern, int pre){
        patterns.put(op, pattern);
        precedence.put(op,pre);     
    }
    
    public String getPattern(Op<?> op){
        return patterns.get(op);
    }
    
    public int getPrecedence(Op<?> operator) {
        if (precedence.containsKey(operator)){
            return precedence.get(operator);
        }else{
            return -1;
        }
    }

    public OperationPatterns toUpperCase() {
        for (Map.Entry<Op<?>,String> entry : patterns.entrySet()){
            patterns.put(entry.getKey(), entry.getValue().toUpperCase());
        }
        return this;
    }

    public OperationPatterns toLowerCase() {
        for (Map.Entry<Op<?>,String> entry : patterns.entrySet()){
            patterns.put(entry.getKey(), entry.getValue().toLowerCase());
        }        
        return this;
    }
    
}
