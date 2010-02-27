/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.custom;

import java.util.Arrays;
import java.util.List;

import com.mysema.query.types.Template;
import com.mysema.query.types.TemplateFactory;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

/**
 * CBoolean is a custom boolean expression
 * 
 * @author tiwe
 *
 */
public class CBoolean extends EBoolean implements Custom<Boolean> {
    
    private static final long serialVersionUID = 5749369427497731719L;

    public static EBoolean create(String template, Expr<?>... args){
        return new CBoolean(TemplateFactory.DEFAULT.create(template), Arrays.<Expr<?>>asList(args));
    }
    
    public static EBoolean create(Template template, Expr<?>... args){
        return new CBoolean(template, Arrays.<Expr<?>>asList(args));
    }
    
    private final Custom<Boolean> customMixin;
    
    public CBoolean(Template template, List<Expr<?>> args){
        customMixin = new CustomMixin<Boolean>(this, args, template);
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
    
    @Override
    public boolean equals(Object o){
        return customMixin.equals(o);
    }
    
    @Override
    public int hashCode(){
        return getType().hashCode();
    }
}