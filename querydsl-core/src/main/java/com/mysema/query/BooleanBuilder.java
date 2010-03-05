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
public final class BooleanBuilder extends EBoolean implements Cloneable{
    
    private static final long serialVersionUID = -4129485177345542519L;
    
    // move to EBoolean ?!?
    @Nullable
    public static EBoolean allOf(EBoolean... exprs){
        EBoolean rv = null;
        for (EBoolean b : exprs){
            rv = rv == null ? b : rv.and(b);
        }
        return rv;
    }

    // move to EBoolean ?!?
    @Nullable
    public static EBoolean anyOf(EBoolean... exprs){
        EBoolean rv = null;
        for (EBoolean b : exprs){
            rv = rv == null ? b : rv.or(b);
        }
        return rv;
    }
    
    @Nullable
    private EBoolean expr;
        
    @Override
    public void accept(Visitor v) {
        if (expr != null){
            expr.accept(v);
        }else{
            throw new QueryException("CascadingBoolean has no value");
        }
    }

    @Override
    public BooleanBuilder and(@Nullable EBoolean right) {
        if (right != null){
            if (expr == null){
                expr = right;
            }else{
                expr = expr.and(right);
            }    
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

    public BooleanBuilder andNot(EBoolean right) {
        return and(right.not());
    }
    
    @Override
    public BooleanBuilder clone() throws CloneNotSupportedException{
        return (BooleanBuilder) super.clone();
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this){
            return true;
        }else if (o instanceof EBoolean){
            return expr != null ? expr.equals(o) : false;    
        }else{
            return false;
        }               
    }
    
    @Nullable
    public EBoolean getValue(){
        return expr;
    }
    
    @Override
    public int hashCode(){
        return Boolean.class.hashCode();
    }
    
    /**
     * Returns true if the value is set, and false, if not
     * 
     * @return
     */
    public boolean hasValue(){
        return expr != null;
    }

    @Override
    public BooleanBuilder not(){
        if (expr != null){
            expr = expr.not();    
        }        
        return this;
    }

    @Override
    public BooleanBuilder or(@Nullable EBoolean right) {
        if (right != null){
            if (expr == null){
                expr = right;
            }else{
                expr = expr.or(right);
            }    
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
    
    public BooleanBuilder orNot(EBoolean right){
        return or(right.not());
    }
    
}
