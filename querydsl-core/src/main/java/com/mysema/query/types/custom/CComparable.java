/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.custom;

import java.util.List;

import com.mysema.query.types.Template;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.Expr;

/**
 * CComparable defines custom comparable expressions
 * 
 * @author tiwe
 *
 * @param <T>
 */
public class CComparable<T extends Comparable<?>> extends EComparable<T> implements Custom<T> {
    
    public static <T extends Comparable<?>> EComparable<T> create(Class<T> type, List<Expr<?>> args, Template template){
        return new CComparable<T>(type, args, template);
    }
    
    private final List<Expr<?>> args;
    
    private final Template template;

    public CComparable(Class<T> type, List<Expr<?>> args, Template template) {
        super(type);
        this.args = args;
        this.template = template;
    }
    
    @Override
    public void accept(Visitor v){
        v.visit(this);
    }
    
    @Override
    public Expr<?> getArg(int index) {
        return getArgs().get(index);
    }

    @Override
    public List<Expr<?>> getArgs() {
        return args;
    }
    
    @Override
    public Template getTemplate() {
        return template;
    }
}