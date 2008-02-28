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
public class Ops {

    /**
     * Operators (the return type is encoded in the 1st generic parameter)
     */
    public interface Op<RT> {
        Op<Boolean> EQ = new OpImpl<Boolean>();
        Op<Boolean> INARRAY = new OpImpl<Boolean>();
        Op<Boolean> INELEMENTS = new OpImpl<Boolean>();
        Op<Boolean> ISNOTNULL = new OpImpl<Boolean>();
        Op<Boolean> ISNULL = new OpImpl<Boolean>();
        Op<Boolean> ISTYPEOF = new OpImpl<Boolean>();
        Op<Boolean> NE = new OpImpl<Boolean>();
    }
    
    /**
     * Boolean operators (operators used with boolean operands)
     */
    public interface OpBoolean{ 
        Op<Boolean> AND = new OpImpl<Boolean>(); 
        Op<Boolean> NOT = new OpImpl<Boolean>();
        Op<Boolean> OR = new OpImpl<Boolean>();
        Op<Boolean> XNOR = new OpImpl<Boolean>();
        Op<Boolean> XOR = new OpImpl<Boolean>();
    }    
        
    /**
     * Operators for Comparable objects
     */
    public interface OpComparable{
        Op<Boolean> BETWEEN = new OpImpl<Boolean>();
        Op<Boolean> GOE = new OpImpl<Boolean>();
        Op<Boolean> GT = new OpImpl<Boolean>();
        Op<Boolean> LOE = new OpImpl<Boolean>();
        Op<Boolean> LT = new OpImpl<Boolean>();
    }
    
    /**
     * Date Operators (operators used with Date operands)
     */
    public interface OpDate{       
        Op<Boolean> AFTER = new OpImpl<Boolean>();
        Op<Boolean> BEFORE = new OpImpl<Boolean>();        
    }
   
    public static final class OpImpl<RT> implements Op<RT> {}
    
    /**
     * Numeric Operators (operators used with numeric operands)
     */
    public interface OpNumber{
        Op<Number> ADD = new OpImpl<Number>();   
        Op<Number> DIV = new OpImpl<Number>();        
        Op<Number> MOD = new OpImpl<Number>();
        Op<Number> MULT = new OpImpl<Number>();
        Op<Number> SUB = new OpImpl<Number>();
    }
    
    /**
     * String Operators (operators used with String operands)
     */
    public interface OpString{       
        Op<String> CONCAT = new OpImpl<String>();
        Op<Boolean> LIKE = new OpImpl<Boolean>();
        Op<String> LOWER = new OpImpl<String>();
        Op<String> SUBSTR = new OpImpl<String>();
        Op<String> UPPER = new OpImpl<String>();
    }
    
}
