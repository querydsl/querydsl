/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import com.mysema.query.grammar.Grammar;
import com.mysema.query.grammar.types.Expr;

/**
 * CascadingBoolean is a cascading builder for Boolean expressions
 * 
 * @author tiwe
 * @version $Id$
 */
public class CascadingBoolean {
    private Expr.EBoolean expr;

    public CascadingBoolean and(Expr.EBoolean right) {
        expr = (expr == null) ? right : expr.and(right);
        return this;
    }
    public void clear(){
        expr = null;
    }
    public CascadingBoolean not(Expr.EBoolean right){
        return and(Grammar.not(right));
    }
    
    public CascadingBoolean or(Expr.EBoolean right) {
        expr = (expr == null) ? right : expr.or(right);
        return this;
    }
    
    public Expr.EBoolean create(){
        return expr;
    }
    @Deprecated
    public Expr.EBoolean self(){
        return create();
    }

}
