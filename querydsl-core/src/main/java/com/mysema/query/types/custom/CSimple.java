/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.custom;

import java.util.List;

import com.mysema.query.types.Template;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.Expr;

/**
 * CSimple defines custom simple expressions
 * 
 * @author tiwe
 *
 * @param <T>
 */
@SuppressWarnings("serial")
public class CSimple<T> extends Expr<T> implements Custom<T> {
    
    public static <T> Expr<T> create(Class<? extends T> type, List<Expr<?>> args, Template template){
        return new CSimple<T>(type, args, template);
    }
    
    private final List<Expr<?>> args;
    
    private final Template template;
    
    public CSimple(Class<? extends T> type, List<Expr<?>> args, Template template) {
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