/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

/**
 * Ops provides the operators for the fluent query grammar
 *
 * @author tiwe
 * @version $Id$
 */
public interface Ops {

    /**
     * Operators (the return type is encoded in the 1st generic parameter)
     */
    public interface Op<RT> {
        Op<java.lang.Boolean> EQ = new OpImpl<java.lang.Boolean>();
        Op<java.lang.Boolean> IN = new OpImpl<java.lang.Boolean>();
        Op<java.lang.Boolean> ISNOTNULL = new OpImpl<java.lang.Boolean>();
        Op<java.lang.Boolean> ISNULL = new OpImpl<java.lang.Boolean>();
        Op<java.lang.Boolean> ISTYPEOF = new OpImpl<java.lang.Boolean>();
        Op<java.lang.Boolean> NE = new OpImpl<java.lang.Boolean>();
        Op<java.lang.Boolean> NOTIN =  new OpImpl<java.lang.Boolean>();
    }
    
    public static final class OpImpl<RT> implements Op<RT>{}
    
    // Boolean
    Op<java.lang.Boolean> AND = new OpImpl<java.lang.Boolean>(); 
    Op<java.lang.Boolean> NOT = new OpImpl<java.lang.Boolean>();
    Op<java.lang.Boolean> OR = new OpImpl<java.lang.Boolean>();
    Op<java.lang.Boolean> XNOR = new OpImpl<java.lang.Boolean>();
    Op<java.lang.Boolean> XOR = new OpImpl<java.lang.Boolean>();
     
    // Comparable
    Op<java.lang.Boolean> BETWEEN = new OpImpl<java.lang.Boolean>();
    Op<java.lang.Boolean> GOE = new OpImpl<java.lang.Boolean>();
    Op<java.lang.Boolean> GT = new OpImpl<java.lang.Boolean>();
    Op<java.lang.Boolean> LOE = new OpImpl<java.lang.Boolean>();
    Op<java.lang.Boolean> LT = new OpImpl<java.lang.Boolean>();
    Op<java.lang.Boolean> NOTBETWEEN = new OpImpl<java.lang.Boolean>();
    
    // Date
    Op<java.lang.Boolean> AFTER = new OpImpl<java.lang.Boolean>();
    Op<java.lang.Boolean> BEFORE = new OpImpl<java.lang.Boolean>();    
     
    // Number
    Op<java.lang.Number> ADD = new OpImpl<java.lang.Number>();          
    Op<java.lang.Number> DIV = new OpImpl<java.lang.Number>();        
    Op<java.lang.Number> MOD = new OpImpl<java.lang.Number>();
    Op<java.lang.Number> MULT = new OpImpl<java.lang.Number>();
    Op<java.lang.Number> SQRT = new OpImpl<java.lang.Number>();
    Op<java.lang.Number> SUB = new OpImpl<java.lang.Number>();
    
    // String
    Op<java.lang.String> CONCAT = new OpImpl<java.lang.String>();
    Op<java.lang.Boolean> LIKE = new OpImpl<java.lang.Boolean>();
    Op<java.lang.String> LOWER = new OpImpl<java.lang.String>();
    Op<java.lang.String> SUBSTR1ARG = new OpImpl<java.lang.String>();
    Op<java.lang.String> SUBSTR2ARGS = new OpImpl<java.lang.String>();
    Op<java.lang.String> TRIM = new OpImpl<java.lang.String>();
    Op<java.lang.String> UPPER = new OpImpl<java.lang.String>();
       
}
