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
package com.querydsl.core.group;

import com.mysema.commons.lang.Pair;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Expression;

/**
 * A pair of (Map) key and value 
 * 
 * @author sasa
 * @param <K> Map key type
 * @param <V> Map value type
 */
 public final class QPair<K, V> extends ConstructorExpression<Pair<K, V>> {

    private static final long serialVersionUID = -1943990903548916056L;

    public static <K, V> QPair<K, V> create(Expression<K> key, Expression<V> value) {
        return new QPair<K, V>(key, value);
    }
    
    @SuppressWarnings({"unchecked" })
    public QPair(Expression<K> key, Expression<V> value) {
        super((Class) Pair.class, new Class[]{Object.class, Object.class}, key, value);
    }
    
    public boolean equals(Expression<?> keyExpr, Expression<?> valueExpr) {
        return getArgs().get(0).equals(keyExpr) && getArgs().get(1).equals(valueExpr);
    }
    
    public boolean equals(Expression<?> keyExpr, Class<?> valueType) {
        return getArgs().get(0).equals(keyExpr) && valueType.isAssignableFrom(getArgs().get(1).getType());
    }
    
}