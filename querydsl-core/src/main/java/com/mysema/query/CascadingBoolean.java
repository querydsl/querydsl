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
    private Expr.Boolean expr;

    public CascadingBoolean and(Expr.Boolean right) {
        expr = (expr == null) ? right : expr.and(right);
        return this;
    }
    public void clear(){
        expr = null;
    }
    public CascadingBoolean not(Expr.Boolean right){
        return and(Grammar.not(right));
    }
    
    public CascadingBoolean or(Expr.Boolean right) {
        expr = (expr == null) ? right : expr.or(right);
        return this;
    }
    
    public Expr.Boolean self(){
        return expr;
    }

}
