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
import com.mysema.query.types.expr.EDate;
import com.mysema.query.types.expr.Expr;

@SuppressWarnings("serial")
public class CDate<T extends Comparable<?>> extends EDate<T> implements Custom<T> {
    
    public static <T extends Comparable<?>> EDate<T> create(Class<T> type, String template, Expr<?>... args){
        return new CDate<T>(type, TemplateFactory.DEFAULT.create(template), Arrays.<Expr<?>>asList(args));
    }
    
    public static <T extends Comparable<?>> EDate<T> create(Class<T> type, Template template, Expr<?>... args){
        return new CDate<T>(type, template, Arrays.<Expr<?>>asList(args));
    }
    
    private final Custom<T> customMixin;

    public CDate(Class<T> type, Template template, List<Expr<?>> args) {
        super(type);
        customMixin = new CustomMixin<T>(args, template);
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
