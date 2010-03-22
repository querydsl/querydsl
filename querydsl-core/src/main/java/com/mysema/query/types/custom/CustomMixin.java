/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.custom;

import java.io.Serializable;
import java.util.List;

import com.mysema.query.types.Custom;
import com.mysema.query.types.Template;
import com.mysema.query.types.expr.Expr;

/**
 * @author tiwe
 *
 * @param <T>
 */
public final class CustomMixin<T> implements Custom<T>, Serializable {
    
    private static final long serialVersionUID = 6951623726800809083L;

    private final Expr<T> self;
    
    private final List<Expr<?>> args;
    
    private final Template template;
    
    public CustomMixin(Expr<T> self, List<Expr<?>> args, Template template){
        this.self = self;
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

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
       if (o == this){
           return true;
       }else if (o instanceof Custom){
           Custom c = (Custom)o;
           return c.getTemplate().equals(template) 
               && c.getType().equals(self.getType());
       }else{
           return false;
       }
    }
    
    @Override
    public int hashCode(){
        return getType().hashCode();
    }

    @Override
    public Class<? extends T> getType() {
        return self.getType();
    }

    @Override
    public Expr<T> asExpr() {
        return self;
    }
}
