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

import java.util.UUID;

import javax.annotation.concurrent.Immutable;

/**
 * ParamExpressionImpl defines a parameter in a querydsl with an optional name
 *
 * @author tiwe
 *
 * @param <T> expression type
 */
@Immutable
public class ParamExpressionImpl<T> extends ExpressionBase<T> implements ParamExpression<T> {

    private static final long serialVersionUID = -6872502615009012503L;

    private final String name;

    private final boolean anon;

    public ParamExpressionImpl(Class<? extends T> type, String name) {
        super(type);
        this.name = name;
        this.anon = false;
    }
    
    public ParamExpressionImpl(Class<? extends T> type) {
        super(type);
        this.name = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        this.anon = true;
    }

    @Override
    public final <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public final boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof ParamExpression<?>) {
            ParamExpression<?> other = (ParamExpression<?>)o;
            return other.getType().equals(getType())
                && other.getName().equals(name)
                && other.isAnon() == anon;
        } else {
            return false;
        }
    }

    public final String getName() {
        return name;
    }

    public final boolean isAnon() {
        return anon;
    }

    public final String getNotSetMessage() {
        if (!anon) {
            return "The parameter " + name + " needs to be set";
        } else {
            return "A parameter of type " + getType().getName() + " was not set";
        }
    }
}
