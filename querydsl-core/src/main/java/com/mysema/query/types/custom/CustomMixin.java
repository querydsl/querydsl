/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.custom;

import java.util.List;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Template;
import com.mysema.query.types.TemplateExpression;
import com.mysema.query.types.expr.MixinBase;

/**
 * Mixin implementation of the Custom interface
 *
 * @author tiwe
 *
 * @param <T>
 */
public final class CustomMixin<T> extends MixinBase<T> implements TemplateExpression<T> {

    private static final long serialVersionUID = 6951623726800809083L;

    private final Expression<T> self;

    private final List<Expression<?>> args;

    private final Template template;

    public CustomMixin(TemplateExpression<T> self, List<Expression<?>> args, Template template){
        this.self = self;
        this.args = args;
        this.template = template;
    }

    @Override
    public Expression<?> getArg(int index) {
        return getArgs().get(index);
    }

    @Override
    public List<Expression<?>> getArgs() {
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
       }else if (o instanceof TemplateExpression){
           TemplateExpression c = (TemplateExpression)o;
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

}
