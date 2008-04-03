/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mysema.query.grammar.Ops.Op;
import com.mysema.query.grammar.types.PathMetadata;
import com.mysema.query.grammar.types.PathMetadata.PathType;

/**
 * HqlOps provides
 *
 * @author tiwe
 * @version $Id$
 */
public class HqlOps  {
    
    public static final Set<Op<?>> wrapCollectionsForOp;
    
    static{
        Set<Op<?>> ops = new HashSet<Op<?>>();
        ops.add(Ops.IN);
        ops.add(Ops.NOTIN);
        ops.add(OpQuant.ALL);
        ops.add(OpQuant.ANY);
        ops.add(OpQuant.EXISTS);
        ops.add(OpQuant.NOTEXISTS);
        wrapCollectionsForOp = Collections.unmodifiableSet(ops);
    }
    
    private static final Map<Op<?>,java.lang.String> patterns = new HashMap<Op<?>,java.lang.String>();
    
    private static final Map<Op<?>,Integer> precedence = new HashMap<Op<?>,Integer>();
    
    static{            
        // boolean
        add(Ops.AND, "%s and %s",36);
        add(Ops.NOT, "not %s",3);
        add(Ops.OR, "%s or %s",38);
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
        
        // numeric
        add(Ops.ADD, "%s + %s",13);        
        add(Ops.DIV, "%s / %s",8);
        add(Ops.MOD, "%s % %s",10);
        add(Ops.MULT,"%s * %s",7);
        add(Ops.SUB, "%s - %s",12);
        add(Ops.SQRT, "sqrt(%s)");
        
        // numeric aggregates
        add(OpNumberAgg.AVG, "avg(%s)");
        add(OpNumberAgg.MAX, "max(%s)");
        add(OpNumberAgg.MIN, "min(%s)");
        
        // various
        add(Ops.EQ, "%s = %s",18);
        add(Ops.ISTYPEOF, "%s.class = %s");
        add(Ops.NE, "%s != %s",25);
        add(Ops.IN, "%s in %s");
        add(Ops.NOTIN, "%s not in %s");        
        add(Ops.ISNULL, "%s is null",26);
        add(Ops.ISNOTNULL, "%s is not null",26);
//        add(Op.SIZE, "size(%s)");
        
        // string
        add(Ops.CONCAT, "%s || %s",37);
        add(Ops.LIKE, "%s like %s",27);
        add(Ops.LOWER, "lower(%s)");        
        add(Ops.SUBSTR1ARG, "substring(%s,%s)");
        add(Ops.SUBSTR2ARGS, "substring(%s,%s,%s)");
        add(Ops.TRIM, "trim(%s)");
        add(Ops.UPPER, "upper(%s)");
                
        // HQL specific
        add(OpHql.SUM, "sum(%s)");
        add(OpHql.SYSDATE, "sysdate");
        add(OpHql.CURRENT_DATE, "current_date()");
        add(OpHql.CURRENT_TIME, "current_time()");
        add(OpHql.CURRENT_TIMESTAMP, "current_timestamp()");
        add(OpHql.SECOND, "second(%s)");
        add(OpHql.MINUTE, "minute(%s)");
        add(OpHql.HOUR, "hour(%s)");
        add(OpHql.DAY, "day(%s)");
        add(OpHql.MONTH, "month(%s)");
        add(OpHql.YEAR, "year(%s)");
        
        // quantified expressions
        add(OpQuant.AVG_IN_COL, "avg(%s)");
        add(OpQuant.MAX_IN_COL, "max(%s)");
        add(OpQuant.MIN_IN_COL, "min(%s)");
        
        add(OpQuant.ANY, "any %s");
        add(OpQuant.ALL, "all %s");
        add(OpQuant.EXISTS, "exists %s");
        add(OpQuant.NOTEXISTS, "not exists %s");        
        
        // path types
        for (PathType type : new PathType[]{PathMetadata.LISTVALUE, PathMetadata.LISTVALUE_CONSTANT, PathMetadata.MAPVALUE, PathMetadata.MAPVALUE_CONSTANT}){
            add(type,"%s[%s]");    
        }
        add(PathMetadata.PROPERTY,"%s.%s"); // TODO : as string
        add(PathMetadata.SIZE,"%s.size");
        add(PathMetadata.VARIABLE,"%s"); // TODO : as string
        
        // HQL types
        add(HqlPathType.MINELEMENT, "minelement(%s)");
        add(HqlPathType.MAXELEMENT, "max(%s");
        add(HqlPathType.MININDEX, "minelement(%s)");
        add(HqlPathType.MAXINDEX, "minelement(%s)");
        add(HqlPathType.LISTINDICES, "indices(%s)");
        add(HqlPathType.MAPINDICES, "indices(%s)");
    }
    
    private static void add(Op<?> op, java.lang.String pattern){
        patterns.put(op, pattern);             
    }
    
    private static void add(Op<?> op, java.lang.String pattern, int pre){
        patterns.put(op, pattern);
        precedence.put(op,pre);     
    }
    
    public static java.lang.String getPattern(Op<?> op){
        return patterns.get(op);
    }
    
    public static int getPrecedence(Op<?> operator) {
        if (precedence.containsKey(operator)){
            return precedence.get(operator);
        }else{
            return -1;
        }
    }
    
    public interface OpHql{
        Op<java.util.Date> CURRENT_DATE = new Op<java.util.Date>();
        Op<java.util.Date> CURRENT_TIME = new Op<java.util.Date>();
        Op<java.util.Date> CURRENT_TIMESTAMP = new Op<java.util.Date>();
        Op<java.util.Date> DAY = new Op<java.util.Date>();
        Op<java.util.Date> HOUR = new Op<java.util.Date>();
        Op<java.lang.Boolean> ISEMPTY = new Op<java.lang.Boolean>();
        Op<java.lang.Boolean> ISNOTEMPTY = new Op<java.lang.Boolean>();
        Op<java.util.Date> MINUTE = new Op<java.util.Date>();
        Op<java.util.Date> MONTH = new Op<java.util.Date>();
        Op<java.util.Date> SECOND = new Op<java.util.Date>();
        Op<java.lang.Number> SUM = new Op<java.lang.Number>();
        Op<java.util.Date> SYSDATE = new Op<java.util.Date>();
        Op<java.util.Date> YEAR = new Op<java.util.Date>();
    }
    
    public interface OpNumberAgg{
        Op<java.lang.Number> AVG = new Op<java.lang.Number>();
        Op<java.lang.Number> MAX = new Op<java.lang.Number>();
        Op<java.lang.Number> MIN = new Op<java.lang.Number>();   
    }
    
    public interface OpQuant{
        Op<java.lang.Number> AVG_IN_COL = new Op<java.lang.Number>();
        Op<java.lang.Number> MAX_IN_COL = new Op<java.lang.Number>();
        Op<java.lang.Number> MIN_IN_COL = new Op<java.lang.Number>();   
        
//        some / any = true for any
//        all        = true for all
//        exists     = true is subselect matches
//        not exists = true if subselect doesn't match
        Op<?> ANY = new Op<Object>();
        Op<?> ALL = new Op<Object>();
        Op<?> EXISTS = new Op<Object>();
        Op<?> NOTEXISTS = new Op<Object>();
    }
    
    public interface HqlPathType{
        PathType MINELEMENT = new PathType(); 
        PathType MAXELEMENT = new PathType();
        PathType MININDEX =  new PathType();
        PathType MAXINDEX = new PathType();
        PathType LISTINDICES = new PathType();
        PathType MAPINDICES = new PathType();    
    } 
    
}
