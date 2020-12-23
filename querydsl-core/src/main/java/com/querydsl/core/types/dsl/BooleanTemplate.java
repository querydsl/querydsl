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

import com.querydsl.core.types.*;

/**
 * {@code BooleanTemplate} is a custom boolean expression
 *
 * @author tiwe
 *
 */
public class BooleanTemplate extends BooleanExpression implements TemplateExpression<Boolean> {

    private static final long serialVersionUID = 5749369427497731719L;

    private final PredicateTemplate templateMixin;

    protected BooleanTemplate(PredicateTemplate mixin) {
        super(mixin);
        this.templateMixin = mixin;
    }

    protected BooleanTemplate(Template template, List<?> args) {
        super(ExpressionUtils.predicateTemplate(template, args));
        this.templateMixin = (PredicateTemplate) mixin;
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
