/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import javax.annotation.Nullable;

import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EBoolean;

/**
 * CascadingBoolean is a cascading builder for Boolean expressions.
 * 
 * @author tiwe
 * @version $Id$
 */
@SuppressWarnings("serial")
public class CascadingBoolean extends EBoolean{
    
    @Nullable
    private EBoolean expr;
    
    @Override
    public CascadingBoolean and(EBoolean right) {
        if (expr == null){
            expr = right;
        }else{
            expr = expr.and(right);
        }        
        return this;
    }

    public CascadingBoolean andAnyOf(EBoolean... args) {
        if (args.length > 0){
            EBoolean any = args[0];
            for (int i = 1; i < args.length; i++){
                any = any.or(args[i]);
            }    
            and(any);
        }
        return this;         
    }

    public CascadingBoolean not(EBoolean right) {
        return and(right.not());
    }

    @Override
    public CascadingBoolean or(EBoolean right) {
        if (expr == null){
            expr = right;
        }else{
            expr = expr.or(right);
        }
        return this;
    }
    
    public CascadingBoolean orAllOf(EBoolean... args) {
        if (args.length > 0){
            EBoolean all = args[0];
            for (int i = 1; i < args.length; i++){
                all = all.and(args[i]);
            }    
            or(all);
        }
        return this;
    }
    
    @Override
    public CascadingBoolean not(){
        expr = expr.not();
        return this;
    }
    
    public boolean hasValue(){
        return expr != null;
    }

    @Override
    public void accept(Visitor v) {
        if (expr != null){
            expr.accept(v);
        }else{
            throw new RuntimeException("CascadingBoolean has no value");
        }
    }
    
}
