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

import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Operator;
import com.querydsl.core.types.expr.SimpleOperation;

/**
 * WindowOver is the first part of a WindowFunction construction
 *
 * @author tiwe
 *
 * @param <T>
 */
public class WindowOver<T> extends SimpleOperation<T> {

    private static final long serialVersionUID = 464583892898579544L;

    public WindowOver(Class<T> type, Operator<? super T> op) {
        super(type, op, ImmutableList.<Expression<?>>of());
    }

    public WindowOver(Class<T> type, Operator<? super T> op, Expression<?> arg) {
        super(type, op, ImmutableList.<Expression<?>>of(arg));
    }

    public WindowOver(Class<T> type, Operator<? super T> op, Expression<?> arg1, Expression<?> arg2) {
        super(type, op, ImmutableList.<Expression<?>>of(arg1, arg2));
    }

    /**
     * @return
     */
    public WindowFirstLast<T> keepFirst() {
        return new WindowFirstLast<T>(this, true);
    }

    /**
     * @return
     */
    public WindowFirstLast<T> keepLast() {
        return new WindowFirstLast<T>(this, false);
    }

    /**
     * @return
     */
    public WindowFunction<T> over() {
        return new WindowFunction<T>(this);
    }

}
