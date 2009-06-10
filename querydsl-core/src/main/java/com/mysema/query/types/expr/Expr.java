/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import java.util.Collection;

import com.mysema.query.serialization.ToStringVisitor;
import com.mysema.query.types.Grammar;
import com.mysema.query.types.ValidationVisitor;

/**
 * Expr represents a general typed expression in a Query instance. The generic type parameter
 * is a reference to the type the expression instance is bound to.
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class Expr<D> {

    private final Class<? extends D> type;
    private String toString;

    public Expr(Class<? extends D> type) {
        this.type = type;
    }

    /**
     * Get the Java type for this expression
     * 
     * @return
     */
    public Class<? extends D> getType() {
        return type;
    }

    /**
     * Create a <code>this == right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public final EBoolean eq(D right) {
        return Grammar.eq(this, right);
    }

    /**
     * Create a <code>this == right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public final EBoolean eq(Expr<? super D> right) {
        return Grammar.eq(this, right);
    }

    /**
     * Create a <code>this &lt;&gt; right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public final EBoolean ne(D right) {
        return Grammar.ne(this, right);
    }

    /**
     * Create a <code>this &lt;&gt; right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public final EBoolean ne(Expr<? super D> right) {
        return Grammar.ne(this, right);
    }

    /**
     * Create a <code>this in right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public final EBoolean in(ECollection<? extends D> right) {
        return Grammar.in(this, right);
    }

    /**
     * Create a <code>this in right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public final EBoolean in(Collection<? extends D> right) {
        return Grammar.in(this, right);
    }

    /**
     * Create a <code>this in right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public final EBoolean in(D... right) {
        return Grammar.in(this, right);
    }

    /**
     * Create a <code>this not in right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public final EBoolean notIn(ECollection<? extends D> right) {
        return Grammar.notIn(this, right);
    }

    /**
     * Create a <code>this not in right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public final EBoolean notIn(Collection<? extends D> right) {
        return Grammar.in(this, right);
    }

    /**
     * Create a <code>this not in right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public final EBoolean notIn(D... right) {
        return Grammar.notIn(this, right);
    }

    /**
     * validate this expression for consistency
     * 
     */
    protected void validate() {
        new ValidationVisitor().handle(this);
    }

    @Override
    public String toString() {
        if (toString == null) {
            toString = new ToStringVisitor().handle(this).toString();
        }
        return toString;
    }
    
    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : super.hashCode();
    }
}
