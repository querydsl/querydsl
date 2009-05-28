/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import com.mysema.query.types.Grammar;
import com.mysema.query.types.OrderSpecifier;

/**
 * EComparable represents comparable expressions
 * 
 * @author tiwe
 * 
 * @param <D> Java type
 * @see java.lang.Comparable
 */
@SuppressWarnings("unchecked")
public abstract class EComparable<D extends Comparable> extends Expr<D> {
    private OrderSpecifier<D> asc;
    private OrderSpecifier<D> desc;
    private EString stringCast;

    public EComparable(Class<? extends D> type) {
        super(type);
    }

    /**
     * Create a <code>this &gt; right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public final EBoolean after(D right) {
        return Grammar.after(this, right);
    }

    /**
     * Create a <code>this &gt; right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public final EBoolean after(Expr<D> right) {
        return Grammar.after(this, right);
    }

    /**
     * Create a <code>this &gt;= right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public final EBoolean aoe(D right) {
        return Grammar.aoe(this, right);
    }

    /**
     * Create a <code>this &gt;= right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public final EBoolean aoe(Expr<D> right) {
        return Grammar.aoe(this, right);
    }

    /**
     * Get an OrderSpecifier for ascending order of this expression
     * 
     * @return
     */
    public final OrderSpecifier<D> asc() {
        if (asc == null){
            asc = Grammar.asc(this);
        }            
        return asc;
    }

    /**
     * Create a <code>this &lt; right</code> expression 
     * 
     * @param right rhs of the comparison
     * @return
     */
    public final EBoolean before(D right) {
        return Grammar.before(this, right);
    }

    /**
     * Create a <code>this &lt; right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public final EBoolean before(Expr<D> right) {
        return Grammar.before(this, right);
    }

    /**
     * Create a <code>this &lt;= right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public final EBoolean boe(D right) {
        return Grammar.boe(this, right);
    }

    /**
     * Create a <code>this &lt; right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public final EBoolean boe(Expr<D> right) {
        return Grammar.boe(this, right);
    }

    /**
     * Create a <code>first &lt; this &lt; second</code> expression
     * 
     * @param first
     * @param second
     * @return
     */
    public final EBoolean between(D first, D second) {
        return Grammar.between(this, first, second);
    }

    /**
     * Create a <code>first &lt; this &lt; second</code> expression
     * 
     * @param first
     * @param second
     * @return
     */
    public final EBoolean between(Expr<D> first, Expr<D> second) {
        return Grammar.between(this, first, second);
    }

    
    /**
     * Create a cast expression to the given numeric type
     * 
     * @param <A>
     * @param type
     * @return
     */
    public <A extends Number & Comparable<? super A>> ENumber<A> castToNum(Class<A> type) {
        return Grammar.numericCast(this, type);
    }

    /**
     * Get an OrderSpecifier for descending order of this expression
     * 
     * @return
     */
    public final OrderSpecifier<D> desc() {
        if (desc == null){
            desc = Grammar.desc(this);
        }            
        return desc;
    }

    public final EBoolean notBetween(D first, D second) {
        return Grammar.notBetween(this, first, second);
    }

    public final EBoolean notBetween(Expr<D> first, Expr<D> second) {
        return Grammar.notBetween(this, first, second);
    }

    /**
     * Get a cast to String expression 
     * 
     * @see     java.lang.Object#toString()
     * @return
     */
    public EString stringValue() {
        if (stringCast == null){
            stringCast = Grammar.stringCast(this);
        }            
        return stringCast;
    }

}