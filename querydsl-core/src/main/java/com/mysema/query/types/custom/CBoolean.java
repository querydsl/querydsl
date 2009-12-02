/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.custom;

import java.util.List;

import com.mysema.query.types.Template;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

/**
 * CBoolean is a custom boolean expression
 * 
 * @author tiwe
 *
 */
@SuppressWarnings("serial")
public class CBoolean extends EBoolean implements Custom<Boolean> {
    
    public static EBoolean create(List<Expr<?>> args, Template template){
        return new CBoolean(args, template);
    }
    
    private final Custom<Boolean> customMixin;
    
    public CBoolean(List<Expr<?>> args, Template template){
        customMixin = new CustomMixin<Boolean>(args, template);
    }
    
    @Override
    public void accept(Visitor v){
        v.visit(this);
    }
    
    @Override
    public Expr<?> getArg(int index) {
        return customMixin.getArg(index);
    }

    @Override
    public List<Expr<?>> getArgs() {
        return customMixin.getArgs();
    }

    @Override
    public Template getTemplate() {
        return customMixin.getTemplate();
    }
}