/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.template;

import java.util.Arrays;
import java.util.List;

import com.mysema.query.types.TemplateExpression;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Template;
import com.mysema.query.types.TemplateFactory;
import com.mysema.query.types.TemplateImpl;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.SimpleExpression;

/**
 * SimpleTemplate defines custom simple expressions
 *
 * @author tiwe
 *
 * @param <T>
 */
public class SimpleTemplate<T> extends SimpleExpression<T> implements TemplateExpression<T> {

    private static final long serialVersionUID = -4697578522909045745L;

    public static <T> SimpleExpression<T> create(Class<? extends T> type, String template, Expression<?>... args){
        return new SimpleTemplate<T>(type, TemplateFactory.DEFAULT.create(template), Arrays.<Expression<?>>asList(args));
    }

    public static <T> SimpleExpression<T> create(Class<? extends T> type, Template template, Expression<?>... args){
        return new SimpleTemplate<T>(type, template, Arrays.<Expression<?>>asList(args));
    }

    private final TemplateExpression<T> customMixin;

    public SimpleTemplate(Class<? extends T> type, Template template, List<Expression<?>> args) {
        super(type);
        customMixin = new TemplateImpl<T>(type, args, template);
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public Expression<?> getArg(int index) {
        return customMixin.getArg(index);
    }

    @Override
    public List<Expression<?>> getArgs() {
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
