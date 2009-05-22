/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import com.mysema.query.types.expr.EBoolean;


/**
 * CascadingBoolean is a cascading builder for Boolean expressions
 * 
 * @author tiwe
 * @version $Id$
 */
public class CascadingBoolean {
    private EBoolean expr;

    public CascadingBoolean and(EBoolean right) {
        expr = (expr == null) ? right : expr.and(right);
        return this;
    }
    public void clear(){
        expr = null;
    }
    public CascadingBoolean not(EBoolean right){
        return and(Grammar.not(right));
    }
    
    public CascadingBoolean or(EBoolean right) {
        expr = (expr == null) ? right : expr.or(right);
        return this;
    }
    
    public EBoolean create(){
        return expr;
    }
    
    public String toString(){
        return expr != null ? expr.toString() : super.toString();
    }

}
