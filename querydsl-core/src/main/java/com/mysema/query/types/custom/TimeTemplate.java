/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.custom;

import java.util.Arrays;
import java.util.List;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Template;
import com.mysema.query.types.TemplateExpression;
import com.mysema.query.types.TemplateFactory;
import com.mysema.query.types.TemplateMixin;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.TimeExpression;

/**
 * @author tiwe
 *
 * @param <T>
 */
public class TimeTemplate<T extends Comparable<?>> extends TimeExpression<T> implements TemplateExpression<T> {

    private static final long serialVersionUID = -7684306954555037051L;

    public static <T extends Comparable<?>> TimeTemplate<T> create(Class<T> type, String template, Expression<?>... args){
        return new TimeTemplate<T>(type, TemplateFactory.DEFAULT.create(template), Arrays.<Expression<?>>asList(args));
    }

    public static <T extends Comparable<?>> TimeTemplate<T> create(Class<T> type, Template template, Expression<?>... args){
        return new TimeTemplate<T>(type, template, Arrays.<Expression<?>>asList(args));
    }

    private final TemplateExpression<T> customMixin;

    public TimeTemplate(Class<T> type, Template template, List<Expression<?>> args) {
        super(type);
        customMixin = new TemplateMixin<T>(this, args, template);
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
