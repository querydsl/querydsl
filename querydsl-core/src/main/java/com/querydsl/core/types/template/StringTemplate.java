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
import com.querydsl.core.types.expr.StringExpression;

/**
 * StringTemplate defines custom String expressions
 *
 * @author tiwe
 *
 */
public class StringTemplate extends StringExpression implements TemplateExpression<String> {

    private static final long serialVersionUID = 3181686132439356614L;

    public static StringExpression create(String template) {
        return new StringTemplate(TemplateFactory.DEFAULT.create(template), ImmutableList.of());
    }
    
    public static StringExpression create(String template, Object one) {
        return new StringTemplate(TemplateFactory.DEFAULT.create(template), ImmutableList.of(one));
    }
    
    public static StringExpression create(String template, Object one, Object two) {
        return new StringTemplate(TemplateFactory.DEFAULT.create(template), ImmutableList.of(one, two));
    }
    
    public static StringExpression create(String template, Object... args) {
        return new StringTemplate(TemplateFactory.DEFAULT.create(template), ImmutableList.copyOf(args));
    }

    public static StringExpression create(Template template, Object... args) {
        return new StringTemplate(template, ImmutableList.copyOf(args));
    }

    private final TemplateExpressionImpl<String> templateMixin;

    public StringTemplate(Template template, ImmutableList<?> args) {
        super(new TemplateExpressionImpl<String>(String.class, template, args));
        this.templateMixin = (TemplateExpressionImpl<String>)mixin;
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
