/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import com.mysema.query.grammar.Ops.Op;

/**
 * Quant provides expressions for quantification.
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
    public static class QBoolean<Q> extends Expr.EBoolean implements Quant{
        private final Expr<?> col;
        private final Op<?> op;
        public QBoolean(Op<?> op, CollectionType<Q> col) {
            this.op = op;
            this.col = (Expr<?>) col;
        }
        public Op<?> getOperator() {return op;}
        public Expr<?> getTarget() {return col;}                           
    }
    
    /**
     * The Class Comparable.
     */
    public static class QComparable<Q extends Comparable<? super Q>> extends Expr.EComparable<Q> implements Quant{
        private final Expr<?> col;
        private final Op<?> op;
        public QComparable(Class<Q> type, Op<?> op, CollectionType<Q> col) {
            super(type);
            this.op = op;
            this.col = (Expr<?>)col;
        }
        public Op<?> getOperator() {return op;}
        public Expr<?> getTarget() {return col;}                          
    }
    
    public static class QNumber<Q extends  Number & Comparable<? super Q>> extends Expr.ENumber<Q> implements Quant{
        private final Expr<?> col;
        private final Op<?> op;
        public QNumber(Class<Q> type, Op<?> op, CollectionType<Q> col) {
            super(type);
            this.op = op;
            this.col = (Expr<?>)col;
        }
        public Op<?> getOperator() {return op;}
        public Expr<?> getTarget() {return col;}                          
    }
        
    /**
     * The Class Simple.
     */
    public static class QSimple<Q> extends Expr.ESimple<Q> implements Quant{
        private final Expr<?> col;
        private final Op<?> op;
        public QSimple(Class<Q> type, Op<?> op, CollectionType<Q> col) {
            super(type);
            this.op = op;
            this.col = (Expr<?>)col;
        }
        public Op<?> getOperator() {return op;}
        public Expr<?> getTarget() {return col;}                       
    }

}
