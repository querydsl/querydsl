/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import com.mysema.query.grammar.Ops.Op;



/**
 * Operation represents an operation with operator and arguments
 * 
 * @author tiwe
 * @version $Id$
 */
public interface Operation<OP,RT> {
    
    Expr<?>[] getArgs();
    Op<OP> getOperator();
    
    /**
     * The Class Boolean.
     */
    public static class Boolean extends Expr.Boolean implements Operation<java.lang.Boolean,java.lang.Boolean>{
        private final Expr<?>[] args;
        private final Op<java.lang.Boolean> op;
        public Boolean(Op<java.lang.Boolean> op, Expr<?>... args){
            this.op = op;
            this.args = args;
        }
        public Expr<?>[] getArgs() {return args;}
        public Op<java.lang.Boolean> getOperator() {return op;}    
    }    
    
    /**
     * The Class Comparable.
     */
    public static class Comparable<OpType,D extends java.lang.Comparable<D>> extends Expr.Comparable<D> implements Operation<OpType,D> {
        private final Expr<?>[] args;
        private final Op<OpType> op;
        public Comparable(Op<OpType> op, Expr<?>... args){
            super(null);
            this.op = op;
            this.args = args;
        }
        public Expr<?>[] getArgs() {return args;}
        public Op<OpType> getOperator() {return op;}    
    }
        
    /**
     * The Class Number.
     */
    public static class Number<N extends java.lang.Number,D extends java.lang.Comparable<D>> extends Comparable<N,D>{
        public Number(Op<N> op, Expr<?>[] args) {
            super(op, args);
        }        
    }
    
    /**
     * The Class String.
     */
    public static class String extends Expr.String implements Operation<java.lang.String,java.lang.String>{
        private final Expr<?>[] args;
        private final Op<java.lang.String> op;
        public String(Op<java.lang.String> op, Expr<?>... args){
            this.op = op;
            this.args = args;
        }
        public Expr<?>[] getArgs() {return args;}
        public Op<java.lang.String> getOperator() {return op;}    
    }

}
