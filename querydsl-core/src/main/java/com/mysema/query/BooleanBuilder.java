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
 * BooleanBuilder is a cascading builder for Boolean expressions.
 * 
 * @author tiwe
 * @version $Id$
 */
@SuppressWarnings("serial")
public class BooleanBuilder extends EBoolean{
    
    @Nullable
    private EBoolean expr;
    
    @Override
    public BooleanBuilder and(EBoolean right) {
        if (expr == null){
            expr = right;
        }else{
            expr = expr.and(right);
        }        
        return this;
    }

    /**
     * Create the intersection of this and the union of the given args
     * <p>(this && (arg1 || arg2 ... || argN))</p>
     * 
     * @param args
     * @return
     */
    public BooleanBuilder andAnyOf(EBoolean... args) {
        if (args.length > 0){
            EBoolean any = args[0];
            for (int i = 1; i < args.length; i++){
                any = any.or(args[i]);
            }    
            and(any);
        }
        return this;         
    }

    public BooleanBuilder not(EBoolean right) {
        return and(right.not());
    }

    @Override
    public BooleanBuilder or(EBoolean right) {
        if (expr == null){
            expr = right;
        }else{
            expr = expr.or(right);
        }
        return this;
    }
    
    /**
     * Create the union of this and the intersection of the given args
     * <p>(this || (arg1 && arg2 ... && argN))</p>
     * 
     * @param args
     * @return
     */
    public BooleanBuilder orAllOf(EBoolean... args) {
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
    public BooleanBuilder not(){
        expr = expr.not();
        return this;
    }
    
    /**
     * Returns true if the value is set, and false, if not
     * 
     * @return
     */
    public boolean hasValue(){
        return expr != null;
    }
    
    public EBoolean getValue(){
        return expr;
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
