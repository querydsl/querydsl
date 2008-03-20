/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import com.mysema.query.grammar.Grammar;
import com.mysema.query.grammar.Types.ExprBoolean;

/**
 * CascadingBoolean provides
 *
 * @author tiwe
 * @version $Id$
 */
public class CascadingBoolean {
    private ExprBoolean expr;

    public CascadingBoolean and(ExprBoolean right) {
        expr = (expr == null) ? right : expr.and(right);
        return this;
    }
    public void clear(){
        expr = null;
    }
    public CascadingBoolean not(ExprBoolean right){
        return and(Grammar.not(right));
    }
    
    public CascadingBoolean or(ExprBoolean right) {
        expr = (expr == null) ? right : expr.or(right);
        return this;
    }
    
    public ExprBoolean self(){
        return expr;
    }

}
