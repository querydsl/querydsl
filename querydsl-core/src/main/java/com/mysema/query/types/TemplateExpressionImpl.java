/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.util.Arrays;
import java.util.List;

import com.mysema.commons.lang.Assert;

/**
 * Default implementation of the TemplateExpression interface
 *
 * @author tiwe
 *
 * @param <T>
 */
public class TemplateExpressionImpl<T> extends ExpressionBase<T> implements TemplateExpression<T> {

    private static final long serialVersionUID = 6951623726800809083L;

    private final List<Expression<?>> args;

    private final Template template;

    public static <C> Expression<C> create(Class<C> cl, String template, Expression<?>... args){
        return create(cl, TemplateFactory.DEFAULT.create(template), args);
    }

    public static <C> Expression<C> create(Class<C> cl, Template template, Expression<?>... args){
        return new TemplateExpressionImpl<C>(cl, template, args);
    }

    public TemplateExpressionImpl(Class<? extends T> type, Template template, Expression<?>... args){
        this(type, template, Arrays.<Expression<?>>asList(args));
    }    
    
    public TemplateExpressionImpl(Class<? extends T> type, Template template, List<Expression<?>> args){
        super(type);
        this.args = Assert.notNull(args,"args");
        this.template = Assert.notNull(template,"template");
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
