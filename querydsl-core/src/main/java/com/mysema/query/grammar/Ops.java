/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

/**
 * Ops provides the operators for the fluent query grammar.
 * 
 * @author tiwe
 * @version $Id$
 */
public interface Ops {

    /**
     * The Class Op.
     */
    public static class Op<RT>{}
    
    // general
    Op<Boolean> EQ_PRIMITIVE = new Op<Boolean>();
    Op<Boolean> EQ_OBJECT = new Op<Boolean>();
    Op<Boolean> IN = new Op<Boolean>();
    Op<Boolean> ISNOTNULL = new Op<Boolean>();
    Op<Boolean> ISNULL = new Op<Boolean>();
    Op<Boolean> ISTYPEOF = new Op<Boolean>();
    Op<Boolean> NE_PRIMITIVE = new Op<Boolean>();
    Op<Boolean> NE_OBJECT = new Op<Boolean>();
    Op<Boolean> NOTIN =  new Op<Boolean>();
    
    // Boolean
    Op<Boolean> AND = new Op<Boolean>(); 
    Op<Boolean> NOT = new Op<Boolean>();
    Op<Boolean> OR = new Op<Boolean>();
    Op<Boolean> XNOR = new Op<Boolean>();
    Op<Boolean> XOR = new Op<Boolean>();
     
    // Comparable
    Op<Boolean> BETWEEN = new Op<Boolean>();
    Op<Boolean> GOE = new Op<Boolean>();
    Op<Boolean> GT = new Op<Boolean>();
    Op<Boolean> LOE = new Op<Boolean>();
    Op<Boolean> LT = new Op<Boolean>();
    Op<Boolean> NOTBETWEEN = new Op<Boolean>();
    Op<Number> NUMCAST = new Op<Number>();
    Op<String> STRING_CAST = new Op<String>();
    
    // Date / Comparable
    Op<Boolean> AFTER = new Op<Boolean>();
    Op<Boolean> BEFORE = new Op<Boolean>();
    Op<Boolean> AOE = new Op<Boolean>();
    Op<Boolean> BOE = new Op<Boolean>();
     
    // Number
    Op<Number> ADD = new Op<Number>();          
    Op<Number> DIV = new Op<Number>();        
    Op<Number> MOD = new Op<Number>();
    Op<Number> MULT = new Op<Number>();
//    Op<Number> SQRT = new Op<Number>();
    Op<Number> SUB = new Op<Number>();
        
    // String
    Op<Character> CHAR_AT = new Op<Character>();
    Op<String> CONCAT = new Op<String>();
    Op<Boolean> LIKE = new Op<Boolean>();
    Op<String> LOWER = new Op<String>();
    Op<String> SUBSTR1ARG = new Op<String>();
    Op<String> SUBSTR2ARGS = new Op<String>();
    Op<String> SPLIT = new Op<String>();
    Op<String> TRIM = new Op<String>();
    Op<String> UPPER = new Op<String>();
    Op<Boolean> MATCHES = new Op<Boolean>();
    Op<Number> STRING_LENGTH = new Op<Number>();  
    Op<Number> LAST_INDEX_2ARGS = new Op<Number>();  
    Op<Number> LAST_INDEX = new Op<Number>();  
    Op<Boolean> ISEMPTY = new Op<Boolean>();
    Op<Boolean> STARTSWITH = new Op<Boolean>();
    Op<Number> INDEXOF_2ARGS = new Op<Number>();  
    Op<Number> INDEXOF = new Op<Number>();  
    Op<Boolean> EQ_IGNORECASE = new Op<Boolean>();
    Op<Boolean> ENDSWITH = new Op<Boolean>();
    Op<Boolean> CONTAINS = new Op<Boolean>();
    
    
    // subquery operations
    Op<Boolean> EXISTS = new Op<Boolean>();
    
    
    
    /**
     * Aggreate operators
     *
     */
    public interface OpNumberAgg{
        Op<Number> AVG = new Op<Number>();
        Op<Number> MAX = new Op<Number>();
        Op<Number> MIN = new Op<Number>();   
    }
    
    /**
     * Date and time operators
     */
    public interface OpDateTime{
        Op<java.util.Date> CURRENT_DATE = new Op<java.util.Date>();
        Op<java.util.Date> CURRENT_TIME = new Op<java.util.Date>();
        Op<java.util.Date> CURRENT_TIMESTAMP = new Op<java.util.Date>();
        Op<Integer> DAY = new Op<Integer>();
        Op<Integer> HOUR = new Op<Integer>();        
        Op<Integer> MINUTE = new Op<Integer>();
        Op<Integer> MONTH = new Op<Integer>();
        Op<Integer> SECOND = new Op<Integer>();        
        Op<java.util.Date> SYSDATE = new Op<java.util.Date>();
        Op<Integer> YEAR = new Op<Integer>();
        Op<Integer> WEEK = new Op<Integer>();
        Op<Integer> DAY_OF_WEEK = new Op<Integer>();
        Op<Integer> DAY_OF_MONTH = new Op<Integer>();
        Op<Integer> DAY_OF_YEAR =new Op<Integer>();
    }
    
    /**
     * Math operators
     *
     */
    public interface OpMath{
        Op<Number> ABS = new Op<Number>();
        Op<Number> ACOS = new Op<Number>();
        Op<Number> ASIN = new Op<Number>();
        Op<Number> ATAN = new Op<Number>();
        Op<Number> CEIL = new Op<Number>();
        Op<Number> COS = new Op<Number>();
        Op<Number> TAN = new Op<Number>();
        Op<Number> SQRT = new Op<Number>();
        Op<Number> SIN = new Op<Number>();
        Op<Number> ROUND = new Op<Number>();
        Op<Number> RANDOM = new Op<Number>();
        Op<Number> POWER = new Op<Number>();
        Op<Number> MIN = new Op<Number>();
        Op<Number> MAX = new Op<Number>();
        Op<Number> MOD = new Op<Number>();
        Op<Number> LOG10 = new Op<Number>();
        Op<Number> LOG = new Op<Number>();
        Op<Number> FLOOR = new Op<Number>();
        Op<Number> EXP = new Op<Number>();        
    }
    
    /**
     * String operators
     */
    public interface OpString{
        Op<Number> LENGTH = new Op<Number>();
        Op<String> LTRIM = new Op<String>();
        Op<String> RTRIM = new Op<String>();
        Op<String> SPACE = new Op<String>();
    }
}
