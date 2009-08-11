/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import javax.annotation.Nullable;

import com.mysema.query.types.expr.EBoolean;

/**
 * CascadingBoolean is a cascading builder for Boolean expressions
 * 
 * @author tiwe
 * @version $Id$
 */
public class CascadingBoolean {
    
    @Nullable
    private EBoolean expr;

    public CascadingBoolean and(EBoolean right) {
        expr = (expr == null) ? right : expr.and(right);
        return this;
    }

    public void clear() {
        expr = null;
    }

    public CascadingBoolean not(EBoolean right) {
        return and(right.not());
    }

    public CascadingBoolean or(EBoolean right) {
        expr = (expr == null) ? right : expr.or(right);
        return this;
    }

    public EBoolean create() {
        return expr;
    }

    public String toString() {
        return expr != null ? expr.toString() : super.toString();
    }

}
