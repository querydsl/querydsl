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

import javax.annotation.Nullable;

import com.querydsl.core.types.Expression;

public interface Projection {

    @Nullable
    <T> T get(Expression<T> expr);

    @Nullable
    <T> T get(int index, Class<T> type);

    @Nullable
    <T> Expression<T> getExpr(Expression<T> expr);

    @Nullable
    <T> Expression<T> getExpr(int index, Class<T> type);

    Object[] toArray();

}
