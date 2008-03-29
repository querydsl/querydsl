/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import java.util.*;

import com.mysema.query.grammar.PathMetadata.PathType;
import com.mysema.query.grammar.PathMetadata.PathTypeImpl;

/**
 * Ops provides
 *
 * @author tiwe
 * @version $Id$
 */
public class HqlOps extends Ops {
    
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
    
    private static final Map<Op<?>,String> patterns = new HashMap<Op<?>,String>();
    
    private static final Map<Op<?>,Integer> precedence = new HashMap<Op<?>,Integer>();
    
    static{            
        // boolean
        add(OpBoolean.AND, "%s and %s",36);
        add(OpBoolean.NOT, "not %s",3);
        add(OpBoolean.OR, "%s or %s",38);
        add(OpBoolean.XNOR, "%s xnor %s",39);        
        add(OpBoolean.XOR, "%s xor %s",39);
        
        // comparison
        add(OpComparable.BETWEEN, "%s between %s and %s",30);
        add(OpComparable.NOTBETWEEN, "%s not between %s and %s",30);
        add(OpComparable.GOE, "%s >= %s",20);
        add(OpComparable.GT, "%s > %s",21);
        add(OpComparable.LOE, "%s <= %s",22);
        add(OpComparable.LT, "%s < %s",23);
           
        add(OpDate.AFTER, "%s > %s",21);
        add(OpDate.BEFORE, "%s < %s",23);
        
        // numeric
        add(OpNumber.ADD, "%s + %s",13);        
        add(OpNumber.DIV, "%s / %s",8);
        add(OpNumber.MOD, "%s % %s",10);
        add(OpNumber.MULT,"%s * %s",7);
        add(OpNumber.SUB, "%s - %s",12);
        add(OpNumber.SQRT, "sqrt(%s)");
        
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
        add(OpString.CONCAT, "%s || %s",37);
        add(OpString.LIKE, "%s like %s",27);
        add(OpString.LOWER, "lower(%s)");        
        add(OpString.SUBSTR1ARG, "substring(%s,%s)");
        add(OpString.SUBSTR2ARGS, "substring(%s,%s,%s)");
        add(OpString.TRIM, "trim(%s)");
        add(OpString.UPPER, "upper(%s)");
                
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
    
    private static void add(Op<?> op, String pattern){
        patterns.put(op, pattern);             
    }
    
    private static void add(Op<?> op, String pattern, int pre){
        patterns.put(op, pattern);
        precedence.put(op,pre);     
    }
    
    public static String getPattern(Op<?> op){
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
        Op<Date> CURRENT_DATE = new OpImpl<Date>();
        Op<Date> CURRENT_TIME = new OpImpl<Date>();
        Op<Date> CURRENT_TIMESTAMP = new OpImpl<Date>();
        Op<Date> DAY = new OpImpl<Date>();
        Op<Date> HOUR = new OpImpl<Date>();
        Op<Boolean> ISEMPTY = new OpImpl<Boolean>();
        Op<Boolean> ISNOTEMPTY = new OpImpl<Boolean>();
        Op<Date> MINUTE = new OpImpl<Date>();
        Op<Date> MONTH = new OpImpl<Date>();
        Op<Date> SECOND = new OpImpl<Date>();
        Op<Number> SUM = new OpImpl<Number>();
        Op<Date> SYSDATE = new OpImpl<Date>();
        Op<Date> YEAR = new OpImpl<Date>();
    }
    
    public interface OpNumberAgg{
        Op<Number> AVG = new OpImpl<Number>();
        Op<Number> MAX = new OpImpl<Number>();
        Op<Number> MIN = new OpImpl<Number>();   
    }
    
    public interface OpQuant{
        Op<Number> AVG_IN_COL = new OpImpl<Number>();
        Op<Number> MAX_IN_COL = new OpImpl<Number>();
        Op<Number> MIN_IN_COL = new OpImpl<Number>();   
        
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
