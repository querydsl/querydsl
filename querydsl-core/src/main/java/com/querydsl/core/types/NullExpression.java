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
package com.querydsl.core.types;

import java.util.Collections;

/**
 * NullExpression defines a general null expression
 *
 * @author tiwe
 *
 * @param <T> expression type
 */
public class NullExpression<T> extends TemplateExpressionImpl<T> {

    private static final Template NULL_TEMPLATE = TemplateFactory.DEFAULT.create("null");

    private static final long serialVersionUID = -5311968198973316411L;

    /**
     * Default instance for an {@link java.lang.Object} typed NullExpression
     */
    public static final NullExpression<Object> DEFAULT = new NullExpression<Object>(Object.class);

    public NullExpression(Class<? extends T> type) {
        super(type, NULL_TEMPLATE, Collections.<Expression<?>>emptyList());
    }

}
