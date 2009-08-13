/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import javax.annotation.Nullable;

import com.mysema.query.types.expr.EBoolean;

// TODO: Auto-generated Javadoc
/**
 * CascadingBoolean is a cascading builder for Boolean expressions.
 * 
 * @author tiwe
 * @version $Id$
 */
public class CascadingBoolean {
    
    /** The expr. */
    @Nullable
    private EBoolean expr;

    /**
     * And.
     * 
     * @param right the right
     * 
     * @return the cascading boolean
     */
    public CascadingBoolean and(EBoolean right) {
        expr = (expr == null) ? right : expr.and(right);
        return this;
    }

    /**
     * Clear.
     */
    public void clear() {
        expr = null;
    }

    /**
     * Not.
     * 
     * @param right the right
     * 
     * @return the cascading boolean
     */
    public CascadingBoolean not(EBoolean right) {
        return and(right.not());
    }

    /**
     * Or.
     * 
     * @param right the right
     * 
     * @return the cascading boolean
     */
    public CascadingBoolean or(EBoolean right) {
        expr = (expr == null) ? right : expr.or(right);
        return this;
    }

    /**
     * Creates the.
     * 
     * @return the e boolean
     */
    public EBoolean create() {
        return expr;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return expr != null ? expr.toString() : super.toString();
    }

}
