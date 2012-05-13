/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
import com.mysema.query.types.expr.DslExpression;

/**
 * DslTemplate defines custom simple expressions
 *
 * @author tiwe
 *
 * @param <T> expression type
 */
public class DslTemplate<T> extends DslExpression<T> implements TemplateExpression<T> {

    private static final long serialVersionUID = -4697578522909045745L;

    public static <T> DslExpression<T> create(Class<? extends T> type, String template, Expression<?>... args) {
        return new DslTemplate<T>(type, TemplateFactory.DEFAULT.create(template), Arrays.<Expression<?>>asList(args));
    }

    public static <T> DslExpression<T> create(Class<? extends T> type, Template template, Expression<?>... args) {
        return new DslTemplate<T>(type, template, Arrays.<Expression<?>>asList(args));
    }

    private final TemplateExpression<T> templateMixin;

    public DslTemplate(Class<? extends T> type, Template template, List<Expression<?>> args) {
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
    public boolean equals(Object o) {
        return templateMixin.equals(o);
    }

    @Override
    public int hashCode() {
        return getType().hashCode();
    }

}
