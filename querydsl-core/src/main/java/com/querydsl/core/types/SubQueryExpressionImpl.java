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

import javax.annotation.concurrent.Immutable;

import com.querydsl.core.QueryMetadata;

/**
 * SubQueryExpressionImpl is the default implementation of the {@link SubQueryExpression} interface
 *
 * @author tiwe
 *
 */
@Immutable
public class SubQueryExpressionImpl<T> extends ExpressionBase<T> implements SubQueryExpression<T> {

    private static final long serialVersionUID = 6775967804458163L;

    private final QueryMetadata metadata;

    public SubQueryExpressionImpl(Class<? extends T> type, QueryMetadata metadata) {
        super(type);
        this.metadata = metadata;
    }

    @SuppressWarnings("unchecked")
    public final boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof SubQueryExpression) {
            SubQueryExpression<T> s = (SubQueryExpression<T>)o;
            return s.getMetadata().equals(metadata);
        } else {
            return false;
        }
    }

    @Override
    public final QueryMetadata getMetadata() {
        return metadata;
    }
    
    @Override
    public final <R, C> R accept(Visitor<R, C> v, C context) {
        return v.visit(this, context);
    }
    
}
