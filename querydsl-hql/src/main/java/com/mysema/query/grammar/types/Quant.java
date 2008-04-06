/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import com.mysema.query.grammar.Ops.Op;

/**
 * Quant provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public interface Quant {
    
    Op<?> getOperator();
    Expr<?> getTarget();
        
    /**
     * The Class Boolean.
     */
    public static class Boolean<Q> extends Expr.Boolean implements Quant{
        private final Expr<?> col;
        private final Op<?> op;
        public Boolean(Op<?> op, CollectionType<Q> col) {
            this.op = op;
            this.col = (Expr<?>) col;
        }
        public Op<?> getOperator() {return op;}
        public Expr<?> getTarget() {return col;}                           
    }
    
    /**
     * The Class Comparable.
     */
    public static class Comparable<Q extends java.lang.Comparable<Q>> extends Expr.Comparable<Q> implements Quant{
        private final Expr<?> col;
        private final Op<?> op;
        public Comparable(Op<?> op, CollectionType<Q> col) {
            super(null);
            this.op = op;
            this.col = (Expr<?>)col;
        }
        public Op<?> getOperator() {return op;}
        public Expr<?> getTarget() {return col;}                          
    }
        
    /**
     * The Class Simple.
     */
    public static class Simple<Q> extends Expr.Simple<Q> implements Quant{
        private final Expr<?> col;
        private final Op<?> op;
        public Simple(Op<?> op, CollectionType<Q> col) {
            super(null);
            this.op = op;
            this.col = (Expr<?>)col;
        }
        public Op<?> getOperator() {return op;}
        public Expr<?> getTarget() {return col;}                       
    }

}
