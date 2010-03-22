/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.custom;

import java.util.Arrays;
import java.util.List;

import com.mysema.query.types.Custom;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Template;
import com.mysema.query.types.TemplateFactory;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EComparable;

/**
 * CComparable defines custom comparable expressions
 * 
 * @author tiwe
 *
 * @param <T>
 */
public class CComparable<T extends Comparable<?>> extends EComparable<T> implements Custom<T> {
    
    private static final long serialVersionUID = -6292853402028813007L;

    public static <T extends Comparable<?>> EComparable<T> create(Class<T> type, String template, Expr<?>... args){
        return new CComparable<T>(type, TemplateFactory.DEFAULT.create(template), Arrays.<Expr<?>>asList(args));
    }
    
    public static <T extends Comparable<?>> EComparable<T> create(Class<T> type, Template template, Expr<?>... args){
        return new CComparable<T>(type, template, Arrays.<Expr<?>>asList(args));
    }
    
    private final Custom<T> customMixin;

    public CComparable(Class<T> type, Template template, List<Expr<?>> args) {
        super(type);
        customMixin = new CustomMixin<T>(this, args, template);
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