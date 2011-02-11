/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.template;

import java.util.Arrays;
import java.util.List;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Template;
import com.mysema.query.types.TemplateExpression;
import com.mysema.query.types.TemplateExpressionImpl;
import com.mysema.query.types.TemplateFactory;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EnumExpression;

/**
 * EnumTemplate defines custom enum expressions
 *
 * @author tiwe
 *
 * @param <T> expression type
 */
public class EnumTemplate<T extends Enum<T>> extends EnumExpression<T> implements TemplateExpression<T> {

    private static final long serialVersionUID = 351057421752203377L;

    public static <T extends Enum<T>> EnumExpression<T> create(Class<T> type, String template, Expression<?>... args){
        return new EnumTemplate<T>(type, TemplateFactory.DEFAULT.create(template), Arrays.<Expression<?>>asList(args));
    }

    public static <T extends Enum<T>> EnumExpression<T> create(Class<T> type, Template template, Expression<?>... args){
        return new EnumTemplate<T>(type, template, Arrays.<Expression<?>>asList(args));
    }

    private final TemplateExpression<T> templateMixin;

    public EnumTemplate(Class<T> type, Template template, List<Expression<?>> args) {
        super(type);
        templateMixin = new TemplateExpressionImpl<T>(type, template, args);
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public Expression<?> getArg(int index) {
        return templateMixin.getArg(index);
    }

    @Override
    public List<Expression<?>> getArgs() {
        return templateMixin.getArgs();
    }

    @Override
    public Template getTemplate() {
        return templateMixin.getTemplate();
    }

    @Override
    public boolean equals(Object o){
        return templateMixin.equals(o);
    }

    @Override
    public int hashCode(){
        return getType().hashCode();
    }

}
