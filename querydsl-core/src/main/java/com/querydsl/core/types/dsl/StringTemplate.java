/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.core.types.dsl;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.*;

/**
 * {@code StringTemplate} defines custom String expressions
 *
 * @author tiwe
 *
 */
public class StringTemplate extends StringExpression implements TemplateExpression<String> {

    private static final long serialVersionUID = 3181686132439356614L;

    private final TemplateExpressionImpl<String> templateMixin;

    protected StringTemplate(Template template, ImmutableList<?> args) {
        super(ExpressionUtils.template(String.class, template, args));
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
