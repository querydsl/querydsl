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
package com.querydsl.sql;

import java.util.List;

import com.querydsl.core.types.Template;
import com.querydsl.core.types.TemplateExpression;
import com.querydsl.core.types.TemplateExpressionImpl;
import com.querydsl.core.types.TemplateFactory;
import com.querydsl.core.types.Visitor;
import com.querydsl.core.types.expr.SimpleExpression;

/**
 * Represents a table valued function call
 *
 * @author tiwe
 *
 * @param <T>
 */
public class RelationalFunctionCall<T> extends SimpleExpression<T> implements TemplateExpression<T> {

    private static final long serialVersionUID = 256739044928186923L;

    private static Template createTemplate(String function, int argCount) {
        StringBuilder builder = new StringBuilder();
        builder.append(function);
        builder.append("(");
        for (int i = 0; i < argCount; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append("{"+ i + "}");
        }
        builder.append(")");
        return TemplateFactory.DEFAULT.create(builder.toString());
    }

    private final TemplateExpression<T> templateMixin;

    /**
     * Create a new RelationalFunctionCall for the given function and arguments
     *
     * @param type
     * @param function
     * @param args
     * @return
     */
    public static <T> RelationalFunctionCall<T> create(Class<? extends T> type, String function, Object... args) {
        return new RelationalFunctionCall<T>(type, function, args);
    }

    public RelationalFunctionCall(Class<? extends T> type, String function, Object... args) {
        super(TemplateExpressionImpl.create((Class)type, createTemplate(function, args.length), args));
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
