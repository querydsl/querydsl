/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.custom;

import java.util.List;

import com.mysema.query.types.Template;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.Expr;

/**
 * CString defines custom String expressions
 * 
 * @author tiwe
 *
 */
@SuppressWarnings("serial")
public class CString extends EString implements Custom<String> {
    
    public static EString create(List<Expr<?>> args, Template template){
        return new CString(args, template);
    }
    
    private final Custom<String> customMixin;
    
    public CString(List<Expr<?>> args, Template template){
        customMixin = new CustomMixin<String>(args, template);
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