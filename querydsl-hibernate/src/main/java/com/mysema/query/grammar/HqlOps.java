/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.mysema.query.grammar.Ops;

/**
 * Ops provides
 *
 * @author tiwe
 * @version $Id$
 */
public class HqlOps extends Ops {
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
        
        add(OpNumber.AVG, "avg(%s)");
        add(OpNumber.MAX, "max(%s)");
        add(OpNumber.MIN, "min(%s)");
        
        // various
        add(Op.EQ, "%s = %s",18);
        add(Op.ISTYPEOF, "%s.class = %s");
        add(Op.NE, "%s != %s",25);
        add(Op.INARRAY, "%s in (%s)");
        add(Op.NOTINARRAY, "%s not in (%s)");
        add(Op.INELEMENTS, "%s in elements(%s)");
        add(Op.ISNULL, "%s is null",26);
        add(Op.ISNOTNULL, "%s is not null",26);
        add(Op.SIZE, "size(%s)");
        
        // string
        add(OpString.CONCAT, "%s || %s",37);
        add(OpString.LIKE, "%s like %s",27);
        add(OpString.LOWER, "lower(%s)");        
        add(OpString.SUBSTR1ARG, "substring(%s,%s)");
        add(OpString.SUBSTR2ARGS, "substring(%s,%s,%s)");
        add(OpString.TRIM, "trim(%s)");
        add(OpString.UPPER, "upper(%s)");
        
        
        // HQL specific
        add(OpHql.EXISTS, "exists elements(%s)");
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
        add(OpHql.MAXINDEX, "maxindex(%s)");
        add(OpHql.MININDEX, "minindex(%s)");
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

    public interface OpHql<RT>{
        Op<Date> CURRENT_DATE = new OpImpl<Date>();
        Op<Date> CURRENT_TIME = new OpImpl<Date>();
        Op<Date> CURRENT_TIMESTAMP = new OpImpl<Date>();
        Op<Date> DAY = new OpImpl<Date>();
        Op<Boolean> EXISTS = new OpImpl<Boolean>();
        Op<Date> HOUR = new OpImpl<Date>();
        Op<Boolean> ISEMPTY = new OpImpl<Boolean>();
        Op<Boolean> ISNOTEMPTY = new OpImpl<Boolean>();
        Op<Object> MAXINDEX = new OpImpl<Object>();
        Op<Object> MININDEX = new OpImpl<Object>();
        Op<Date> MINUTE = new OpImpl<Date>();
        Op<Date> MONTH = new OpImpl<Date>();
        Op<Date> SECOND = new OpImpl<Date>();
        Op<Number> SUM = new OpImpl<Number>();
        Op<Date> SYSDATE = new OpImpl<Date>();
        Op<Date> YEAR = new OpImpl<Date>();
    }
    
}
