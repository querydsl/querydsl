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

    public static Expression<String> create(String template, Expression<?>... args){
        return create(TemplateFactory.DEFAULT.create(template), args);
    }

    public static Expression<String> create(Template template, Expression<?>... args){
        return new TemplateExpressionImpl<String>(String.class, Arrays.<Expression<?>>asList(args), template);
    }
    
    public TemplateExpressionImpl(Class<? extends T> type, List<Expression<?>> args, Template template){
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
