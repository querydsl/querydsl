/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import com.mysema.query.grammar.Ops.Op;
import com.mysema.query.grammar.types.Expr.EBoolean;
import com.mysema.query.grammar.types.Expr.EComparable;
import com.mysema.query.grammar.types.Expr.ENumber;
import com.mysema.query.grammar.types.Expr.EString;



/**
 * Operation represents an operation with operator and arguments
 * 
 * @author tiwe
 * @version $Id$
 */
public interface Operation<OP,RT> {
    
    Class<? extends RT> getType();
    Expr<?>[] getArgs();
    Op<OP> getOperator();
    
    /**
     * The Class Boolean.
     */
    public static class OBoolean extends EBoolean implements Operation<Boolean,Boolean>{
        private final Expr<?>[] args;
        private final Op<Boolean> op;
        public OBoolean(Op<Boolean> op, Expr<?>... args){
            this.op = op;
            this.args = args;
        }
        public Expr<?>[] getArgs() {return args;}
        public Op<Boolean> getOperator() {return op;}  

    }    
    
    /**
     * The Class Comparable.
     */
    public static class OComparable<OpType,D extends Comparable<? super D>> extends EComparable<D> implements Operation<OpType,D> {
        private final Expr<?>[] args;
        private final Op<OpType> op;
        public OComparable(Class<D> type, Op<OpType> op, Expr<?>... args){
            super(type);
            this.op = op;
            this.args = args;
        }
        
        public OComparable(Op<OpType> op, Expr<?>... args){
            this(null, op, args);
        }
        public Expr<?>[] getArgs() {return args;}
        public Op<OpType> getOperator() {return op;}         
    }
        
    /**
     * The Class Number.
     */
    public static class ONumber<OpType extends Number, D extends Number & Comparable<? super D>> extends ENumber<D> implements Operation<OpType,D>{
        private final Expr<?>[] args;
        private final Op<OpType> op;
        public ONumber(Class<? extends D> type, Op<OpType> op, Expr<?>... args){
            super(type);
            this.op = op;
            this.args = args;
        }
        
        public ONumber(Op<OpType> op, Expr<?>... args){
            this(null, op, args);
        }
        public Expr<?>[] getArgs() {return args;}
        public Op<OpType> getOperator() {return op;}       
    }
    
    /**
     * The Class String.
     */
    public static class OString extends EString implements Operation<String,String>{
        private final Expr<?>[] args;
        private final Op<String> op;
        public OString(Op<String> op, Expr<?>... args){
            this.op = op;
            this.args = args;
        }
        public Expr<?>[] getArgs() {return args;}
        public Op<String> getOperator() {return op;}    
    }
    
    public static class OStringArray extends Expr<String[]> implements Operation<String,String[]>{
        private final Expr<?>[] args;
        private final Op<String> op;
        public OStringArray(Op<String> op, Expr<?>... args){
            super(null);
            this.op = op;
            this.args = args;
        }
        public Expr<?>[] getArgs() {return args;}
        public Op<String> getOperator() {return op;}
    }
    
}
