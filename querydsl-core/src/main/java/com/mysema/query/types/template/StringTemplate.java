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
import com.mysema.query.types.TemplateExpressionImpl;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.StringExpression;

/**
 * StringTemplate defines custom String expressions
 *
 * @author tiwe
 *
 */
public class StringTemplate extends StringExpression implements TemplateExpression<String> {

    private static final long serialVersionUID = 3181686132439356614L;

    public static StringExpression create(String template, Expression<?>... args){
        return new StringTemplate(TemplateFactory.DEFAULT.create(template), Arrays.<Expression<?>>asList(args));
    }

    public static StringExpression create(Template template, Expression<?>... args){
        return new StringTemplate(template, Arrays.<Expression<?>>asList(args));
    }

    private final TemplateExpression<String> templateMixin;

    public StringTemplate(Template template, List<Expression<?>> args){
        templateMixin = new TemplateExpressionImpl<String>(String.class, args, template);
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
