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

import com.mysema.query.types.Template;
import com.mysema.query.types.TemplateExpression;
import com.mysema.query.types.TemplateExpressionImpl;
import com.mysema.query.types.TemplateFactory;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.DateExpression;

/**
 * DateTemplate defines custom date expressions
 *
 * @author tiwe
 *
 * @param <T> expression type
 */
public class DateTemplate<T extends Comparable<?>> extends DateExpression<T> implements TemplateExpression<T> {

    private static final long serialVersionUID = 4975559746071238026L;

    public static <T extends Comparable<?>> DateExpression<T> create(Class<T> type, String template, Object... args) {
        return new DateTemplate<T>(type, TemplateFactory.DEFAULT.create(template), Arrays.asList(args));
    }

    public static <T extends Comparable<?>> DateExpression<T> create(Class<T> type, Template template, Object... args) {
        return new DateTemplate<T>(type, template, Arrays.asList(args));
    }

    private final TemplateExpression<T> templateMixin;

    public DateTemplate(Class<T> type, Template template, List<?> args) {
        super(new TemplateExpressionImpl<T>(type, template, args));
        templateMixin = (TemplateExpression<T>)mixin;
    }

    @Override
    public final <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
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
