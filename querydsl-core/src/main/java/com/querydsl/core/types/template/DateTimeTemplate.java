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
package com.querydsl.core.types.template;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.Template;
import com.querydsl.core.types.TemplateExpression;
import com.querydsl.core.types.TemplateExpressionImpl;
import com.querydsl.core.types.TemplateFactory;
import com.querydsl.core.types.Visitor;
import com.querydsl.core.types.expr.DateTimeExpression;

/**
 * DateTimeTemplate defines custom dateTime expressions
 *
 * @author tiwe
 *
 * @param <T> expression type
 */
public class DateTimeTemplate<T extends Comparable<?>> extends DateTimeExpression<T> implements TemplateExpression<T> {

    private static final long serialVersionUID = -2289699666347576749L;

    public static <T extends Comparable<?>> DateTimeExpression<T> create(Class<T> type, String template) {
        return new DateTimeTemplate<T>(type, TemplateFactory.DEFAULT.create(template), ImmutableList.of());
    }
    
    public static <T extends Comparable<?>> DateTimeExpression<T> create(Class<T> type, String template, Object one) {
        return new DateTimeTemplate<T>(type, TemplateFactory.DEFAULT.create(template), ImmutableList.of(one));
    }
    
    public static <T extends Comparable<?>> DateTimeExpression<T> create(Class<T> type, String template, Object one, Object two) {
        return new DateTimeTemplate<T>(type, TemplateFactory.DEFAULT.create(template), ImmutableList.of(one, two));
    }
    
    public static <T extends Comparable<?>> DateTimeExpression<T> create(Class<T> type, String template, Object... args) {
        return new DateTimeTemplate<T>(type, TemplateFactory.DEFAULT.create(template), ImmutableList.copyOf(args));
    }

    public static <T extends Comparable<?>> DateTimeExpression<T> create(Class<T> type, Template template, Object... args) {
        return new DateTimeTemplate<T>(type, template, ImmutableList.copyOf(args));
    }

    private final TemplateExpressionImpl<T> templateMixin;

    public DateTimeTemplate(Class<T> type, Template template, ImmutableList<?> args) {
        super(new TemplateExpressionImpl<T>(type, template, args));
        templateMixin = (TemplateExpressionImpl<T>)mixin;
    }

    @Override
    public final <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(templateMixin, context);
    }
    
    @Override
    public Object getArg(int index) {
        return templateMixin.getArg(index);
    }

    @Override
    public List<?> getArgs() {
        return templateMixin.getArgs();
    }

    @Override
    public Template getTemplate() {
        return templateMixin.getTemplate();
    }

}
