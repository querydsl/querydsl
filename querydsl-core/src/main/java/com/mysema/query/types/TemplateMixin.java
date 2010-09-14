/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.util.List;


/**
 * Mixin implementation of the TemplateExpression interface
 *
 * @author tiwe
 *
 * @param <T>
 */
public final class TemplateMixin<T> extends MixinBase<T> implements TemplateExpression<T> {

    private static final long serialVersionUID = 6951623726800809083L;

    private final Expression<T> self;

    private final List<Expression<?>> args;

    private final Template template;

    public TemplateMixin(TemplateExpression<T> self, List<Expression<?>> args, Template template){
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
