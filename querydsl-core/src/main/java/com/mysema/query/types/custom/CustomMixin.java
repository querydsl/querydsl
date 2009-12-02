/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.custom;

import java.util.List;

import com.mysema.query.types.Template;
import com.mysema.query.types.expr.Expr;

/**
 * @author tiwe
 *
 * @param <T>
 */
class CustomMixin<T> implements Custom<T> {
    
    private final List<Expr<?>> args;
    
    private final Template template;
    
    public CustomMixin(List<Expr<?>> args, Template template){
        this.args = args;
        this.template = template;
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
