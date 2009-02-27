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
    public static class Op<RT>{
        private final String symbol;
        public Op(String symbol){
            this.symbol = symbol;
        }
        public String toString(){
            return symbol;
        }
    }
    
    // general
    Op<Boolean> EQ_PRIMITIVE = new Op<Boolean>("equals");
    Op<Boolean> EQ_OBJECT = new Op<Boolean>("equals");
    Op<Boolean> IN = new Op<Boolean>("in");
    Op<Boolean> ISNOTNULL = new Op<Boolean>("is not null");
    Op<Boolean> ISNULL = new Op<Boolean>("is null");
    Op<Boolean> ISTYPEOF = new Op<Boolean>("type of");
    Op<Boolean> NE_PRIMITIVE = new Op<Boolean>("not equals");
    Op<Boolean> NE_OBJECT = new Op<Boolean>("not equals");
    Op<Boolean> NOTIN =  new Op<Boolean>("not in");
    
    // Boolean
    Op<Boolean> AND = new Op<Boolean>("and"); 
    Op<Boolean> NOT = new Op<Boolean>("not");
    Op<Boolean> OR = new Op<Boolean>("or");
    Op<Boolean> XNOR = new Op<Boolean>("xnor");
    Op<Boolean> XOR = new Op<Boolean>("xor");
     
    // Comparable
    Op<Boolean> BETWEEN = new Op<Boolean>("between");
    Op<Boolean> GOE = new Op<Boolean>("goe");
    Op<Boolean> GT = new Op<Boolean>("gt");
    Op<Boolean> LOE = new Op<Boolean>("loe");
    Op<Boolean> LT = new Op<Boolean>("lt");
    Op<Boolean> NOTBETWEEN = new Op<Boolean>("not between");
    Op<Number> NUMCAST = new Op<Number>("num cast");
    Op<String> STRING_CAST = new Op<String>("string cast");
    
    // Date / Comparable
    Op<Boolean> AFTER = new Op<Boolean>("after");
    Op<Boolean> BEFORE = new Op<Boolean>("before");
    Op<Boolean> AOE = new Op<Boolean>("aoe");
    Op<Boolean> BOE = new Op<Boolean>("boe");
     
    // Number
    Op<Number> ADD = new Op<Number>("add");          
    Op<Number> DIV = new Op<Number>("div");        
    Op<Number> MOD = new Op<Number>("mod");
    Op<Number> MULT = new Op<Number>("mult");
//    Op<Number> SQRT = new Op<Number>();
    Op<Number> SUB = new Op<Number>("sub");
        
    // String
    Op<Character> CHAR_AT = new Op<Character>("char at");
    Op<String> CONCAT = new Op<String>("concat");
    Op<Boolean> LIKE = new Op<Boolean>("like");
    Op<String> LOWER = new Op<String>("lower");
    Op<String> SUBSTR1ARG = new Op<String>("substring");
    Op<String> SUBSTR2ARGS = new Op<String>("substring");
    Op<String> SPLIT = new Op<String>("split");
    Op<String> TRIM = new Op<String>("trim");
    Op<String> UPPER = new Op<String>("upper");
    Op<Boolean> MATCHES = new Op<Boolean>("matches");
    Op<Number> STRING_LENGTH = new Op<Number>("length");  
    Op<Number> LAST_INDEX_2ARGS = new Op<Number>("last index");  
    Op<Number> LAST_INDEX = new Op<Number>("last index");  
    Op<Boolean> ISEMPTY = new Op<Boolean>("is empty");
    Op<Boolean> STARTSWITH = new Op<Boolean>("starts with");
    Op<Number> INDEXOF_2ARGS = new Op<Number>("index of");  
    Op<Number> INDEXOF = new Op<Number>("index of");  
    Op<Boolean> EQ_IGNORECASE = new Op<Boolean>("eq ic");
    Op<Boolean> ENDSWITH = new Op<Boolean>("ends with");
    Op<Boolean> CONTAINS = new Op<Boolean>("contains");
    
    
    // subquery operations
    Op<Boolean> EXISTS = new Op<Boolean>("exists");
    
    
    
    /**
     * Aggreate operators
     *
     */
    public interface OpNumberAgg{
        Op<Number> AVG = new Op<Number>("avg");
        Op<Number> MAX = new Op<Number>("max");
        Op<Number> MIN = new Op<Number>("min");   
    }
    
    /**
     * Date and time operators
     */
    public interface OpDateTime{
        Op<java.util.Date> CURRENT_DATE = new Op<java.util.Date>("current date");
        Op<java.util.Date> CURRENT_TIME = new Op<java.util.Date>("current time");
        Op<java.util.Date> CURRENT_TIMESTAMP = new Op<java.util.Date>("current timestamp");
        Op<Integer> DAY = new Op<Integer>("day");
        Op<Integer> HOUR = new Op<Integer>("hour");        
        Op<Integer> MINUTE = new Op<Integer>("minute");
        Op<Integer> MONTH = new Op<Integer>("month");
        Op<Integer> SECOND = new Op<Integer>("second");        
        Op<java.util.Date> SYSDATE = new Op<java.util.Date>("sysdate");
        Op<Integer> YEAR = new Op<Integer>("year");
        Op<Integer> WEEK = new Op<Integer>("week");
        Op<Integer> DAY_OF_WEEK = new Op<Integer>("day of week");
        Op<Integer> DAY_OF_MONTH = new Op<Integer>("day of month");
        Op<Integer> DAY_OF_YEAR =new Op<Integer>("day of year");
    }
    
    /**
     * Math operators
     *
     */
    public interface OpMath{
        Op<Number> ABS = new Op<Number>("abs");
        Op<Number> ACOS = new Op<Number>("acos");
        Op<Number> ASIN = new Op<Number>("asin");
        Op<Number> ATAN = new Op<Number>("atan");
        Op<Number> CEIL = new Op<Number>("ceil");
        Op<Number> COS = new Op<Number>("cos");
        Op<Number> TAN = new Op<Number>("tan");
        Op<Number> SQRT = new Op<Number>("sqrt");
        Op<Number> SIN = new Op<Number>("sin");
        Op<Number> ROUND = new Op<Number>("round");
        Op<Number> RANDOM = new Op<Number>("random");
        Op<Number> POWER = new Op<Number>("power");
        Op<Number> MIN = new Op<Number>("min");
        Op<Number> MAX = new Op<Number>("max");
        Op<Number> MOD = new Op<Number>("mod");
        Op<Number> LOG10 = new Op<Number>("log10");
        Op<Number> LOG = new Op<Number>("log");
        Op<Number> FLOOR = new Op<Number>("floor");
        Op<Number> EXP = new Op<Number>("exp");        
    }
    
    /**
     * String operators
     */
    public interface OpString{
        Op<Number> LENGTH = new Op<Number>("length");
        Op<String> LTRIM = new Op<String>("ltrim");
        Op<String> RTRIM = new Op<String>("trim");
        Op<String> SPACE = new Op<String>("space");
    }
}
