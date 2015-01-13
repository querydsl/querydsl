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

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.List;

import com.querydsl.core.Tuple;

/**
 * Projection template that allows implementing arbitrary mapping of rows to result objects.
 * 
 * <p>Example</p>
 * 
 * <pre><code>
 * {@code MappingProjection<Pair<String, String>>} mapping = new {@code MappingProjection<Pair<String, String>>}(Pair.class, str1, str2) {
 *     {@code @Override}
 *     {@code protected Pair<String, String>} map(Tuple row) {
 *          return Pair.of(row.get(str1), row.get(str2));
 *     }            
 * }; 
 * </code></pre>
 *
 * @param <T> expression type
 */
@Immutable
public abstract class MappingProjection<T> extends FactoryExpressionBase<T> {

    private static final long serialVersionUID = -948494350919774466L;

    private final QTuple qTuple;

    /**
     * Create a new MappingProjection instance
     * 
     * @param type
     * @param args
     */
    @SuppressWarnings("unchecked")
    public MappingProjection(Class<? super T> type, Expression<?>... args) {
        super((Class)type);
        qTuple = new QTuple(ExpressionUtils.distinctList(args));
    }

    /**
     * Create a new MappingProjection instance
     * 
     * @param type
     * @param args
     */
    @SuppressWarnings("unchecked")
    public MappingProjection(Class<? super T> type, Expression<?>[]... args) {
        super((Class)type);
        qTuple = new QTuple(ExpressionUtils.distinctList(args));
    }

    public T newInstance(Object... values) {
        return map(qTuple.newInstance(values));
    }

    /**
     * Creates a result object from the given row.
     *
     * @param row The row to map
     * @return The result object
     */
    protected abstract T map(Tuple row);

    public List<Expression<?>> getArgs() {
        return qTuple.getArgs();
    }

    public <R, C> R accept(Visitor<R, C> v, @Nullable C context) {
        return v.visit(this, context);
    }
    
}