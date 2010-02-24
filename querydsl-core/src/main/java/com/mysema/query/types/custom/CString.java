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
    
    public static EString create(String template, Expr<?>... args){
        return new CString(TemplateFactory.DEFAULT.create(template), Arrays.<Expr<?>>asList(args));
    }
    
    public static EString create(Template template, Expr<?>... args){
        return new CString(template, Arrays.<Expr<?>>asList(args));
    }
    
    private final Custom<String> customMixin;
    
    public CString(Template template, List<Expr<?>> args){
        customMixin = new CustomMixin<String>(this, args, template);
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