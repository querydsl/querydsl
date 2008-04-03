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

import com.mysema.query.grammar.Ops;
import com.mysema.query.grammar.Ops.Op;
import com.mysema.query.grammar.Ops.OpImpl;
import com.mysema.query.grammar.types.PathMetadata.PathType;
import com.mysema.query.grammar.types.PathMetadata.PathTypeImpl;

/**
 * Ops provides
 *
 * @author tiwe
 * @version $Id$
 */
public class HqlOps  {
    
    public static final Set<Op<?>> wrapCollectionsForOp;
    
    static{
        Set<Op<?>> ops = new HashSet<Op<?>>();
        ops.add(Op.IN);
        ops.add(Op.NOTIN);
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
        add(Op.EQ, "%s = %s",18);
        add(Op.ISTYPEOF, "%s.class = %s");
        add(Op.NE, "%s != %s",25);
        add(Op.IN, "%s in %s");
        add(Op.NOTIN, "%s not in %s");        
        add(Op.ISNULL, "%s is null",26);
        add(Op.ISNOTNULL, "%s is not null",26);
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
        for (PathType type : new PathType[]{PathType.LISTVALUE, PathType.LISTVALUE_CONSTANT, PathType.MAPVALUE, PathType.MAPVALUE_CONSTANT}){
            add(type,"%s[%s]");    
        }
        add(PathType.PROPERTY,"%s.%s"); // TODO : as string
        add(PathType.SIZE,"%s.size");
        add(PathType.VARIABLE,"%s"); // TODO : as string
        
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
        Op<java.util.Date> CURRENT_DATE = new OpImpl<java.util.Date>();
        Op<java.util.Date> CURRENT_TIME = new OpImpl<java.util.Date>();
        Op<java.util.Date> CURRENT_TIMESTAMP = new OpImpl<java.util.Date>();
        Op<java.util.Date> DAY = new OpImpl<java.util.Date>();
        Op<java.util.Date> HOUR = new OpImpl<java.util.Date>();
        Op<java.lang.Boolean> ISEMPTY = new OpImpl<java.lang.Boolean>();
        Op<java.lang.Boolean> ISNOTEMPTY = new OpImpl<java.lang.Boolean>();
        Op<java.util.Date> MINUTE = new OpImpl<java.util.Date>();
        Op<java.util.Date> MONTH = new OpImpl<java.util.Date>();
        Op<java.util.Date> SECOND = new OpImpl<java.util.Date>();
        Op<java.lang.Number> SUM = new OpImpl<java.lang.Number>();
        Op<java.util.Date> SYSDATE = new OpImpl<java.util.Date>();
        Op<java.util.Date> YEAR = new OpImpl<java.util.Date>();
    }
    
    public interface OpNumberAgg{
        Op<java.lang.Number> AVG = new OpImpl<java.lang.Number>();
        Op<java.lang.Number> MAX = new OpImpl<java.lang.Number>();
        Op<java.lang.Number> MIN = new OpImpl<java.lang.Number>();   
    }
    
    public interface OpQuant{
        Op<java.lang.Number> AVG_IN_COL = new OpImpl<java.lang.Number>();
        Op<java.lang.Number> MAX_IN_COL = new OpImpl<java.lang.Number>();
        Op<java.lang.Number> MIN_IN_COL = new OpImpl<java.lang.Number>();   
        
//        some / any = true for any
//        all        = true for all
//        exists     = true is subselect matches
//        not exists = true if subselect doesn't match
        Op<?> ANY = new OpImpl<Object>();
        Op<?> ALL = new OpImpl<Object>();
        Op<?> EXISTS = new OpImpl<Object>();
        Op<?> NOTEXISTS = new OpImpl<Object>();
    }
    
    public interface HqlPathType{
        PathType MINELEMENT = new PathTypeImpl(); 
        PathType MAXELEMENT = new PathTypeImpl();
        PathType MININDEX =  new PathTypeImpl();
        PathType MAXINDEX = new PathTypeImpl();
        PathType LISTINDICES = new PathTypeImpl();
        PathType MAPINDICES = new PathTypeImpl();    
    } 
    
}
