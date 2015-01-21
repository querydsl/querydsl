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
import com.querydsl.core.types.PredicateTemplate;
import com.querydsl.core.types.Template;
import com.querydsl.core.types.TemplateExpression;
import com.querydsl.core.types.TemplateFactory;
import com.querydsl.core.types.Visitor;
import com.querydsl.core.types.expr.BooleanExpression;

/**
 * BooleanTemplate is a custom boolean expression
 *
 * @author tiwe
 *
 */
public class BooleanTemplate extends BooleanExpression implements TemplateExpression<Boolean> {

    private static final long serialVersionUID = 5749369427497731719L;

    public static BooleanExpression create(String template) {
        return new BooleanTemplate(TemplateFactory.DEFAULT.create(template), ImmutableList.of());
    }
    
    public static BooleanExpression create(String template, Object one) {
        return new BooleanTemplate(TemplateFactory.DEFAULT.create(template), ImmutableList.of(one));
    }
    
    public static BooleanExpression create(String template, Object one, Object two) {
        return new BooleanTemplate(TemplateFactory.DEFAULT.create(template), ImmutableList.of(one, two));
    }
    
    public static BooleanExpression create(String template, Object... args) {
        return new BooleanTemplate(TemplateFactory.DEFAULT.create(template), ImmutableList.copyOf(args));
    }

    public static BooleanExpression create(Template template, Object... args) {
        return new BooleanTemplate(template, ImmutableList.copyOf(args));
    }
    
    public static final BooleanExpression TRUE = create("true");
    
    public static final BooleanExpression FALSE = create("false");

    private final PredicateTemplate templateMixin;

    public BooleanTemplate(Template template, ImmutableList<?> args) {
        super(new PredicateTemplate(template, args));
        this.templateMixin = (PredicateTemplate)mixin;
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
