/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.custom;

import java.util.Arrays;
import java.util.List;

import com.mysema.query.types.TemplateExpression;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Template;
import com.mysema.query.types.TemplateFactory;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * BooleanTemplate is a custom boolean expression
 *
 * @author tiwe
 *
 */
public class BooleanTemplate extends BooleanExpression implements TemplateExpression<Boolean> {

    private static final long serialVersionUID = 5749369427497731719L;

    public static BooleanExpression create(String template, Expression<?>... args){
        return new BooleanTemplate(TemplateFactory.DEFAULT.create(template), Arrays.<Expression<?>>asList(args));
    }

    public static BooleanExpression create(Template template, Expression<?>... args){
        return new BooleanTemplate(template, Arrays.<Expression<?>>asList(args));
    }

    private final TemplateExpression<Boolean> customMixin;

    public BooleanTemplate(Template template, List<Expression<?>> args){
        customMixin = new CustomMixin<Boolean>(this, args, template);
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
