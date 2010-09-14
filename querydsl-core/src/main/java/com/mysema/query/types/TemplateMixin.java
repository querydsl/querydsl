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
public class TemplateMixin<T> extends MixinBase<T> implements TemplateExpression<T> {

    private static final long serialVersionUID = 6951623726800809083L;

    private final List<Expression<?>> args;

    private final Template template;

    public TemplateMixin(Class<? extends T> type, List<Expression<?>> args, Template template){
        super(type);
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
               && c.getType().equals(type);
       }else{
           return false;
       }
    }

    @Override
    public int hashCode(){
        return getType().hashCode();
    }
    
    @Override
    public <R, C> R accept(Visitor<R, C> v, C context) {
        return v.visit(this, context);
    }

}
