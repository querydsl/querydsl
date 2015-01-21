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

import java.util.LinkedHashMap;
import java.util.Map;

import com.querydsl.core.types.*;

/**
 * RelationalPathUtils provides static utility methods for {@link RelationalPath} instances
 *
 * @author tiwe
 *
 */
@SuppressWarnings("unchecked")
public final class RelationalPathUtils {

    public static <T> FactoryExpression<T> createProjection(RelationalPath<T> path) {
        if (path.getType().equals(path.getClass())) {
            throw new IllegalArgumentException("RelationalPath based projection can only be used with generated Bean types");
        }
        try {
            // ensure that empty constructor is available
            path.getType().getConstructor();
            return createBeanProjection(path);
        } catch (NoSuchMethodException e) {
            // fallback to constructor projection
            return createConstructorProjection(path);
        }
    }

    private static <T> FactoryExpression<T> createConstructorProjection(RelationalPath<T> path) {
        Expression<?>[] exprs = path.getColumns().toArray(new Expression[path.getColumns().size()]);
        return Projections.<T>constructor((Class)path.getType(), exprs);
    }

    private static <T> FactoryExpression<T> createBeanProjection(RelationalPath<T> path) {
        Map<String,Expression<?>> bindings = new LinkedHashMap<String,Expression<?>>();
        for (Path<?> column : path.getColumns()) {
            bindings.put(column.getMetadata().getName(), column);
        }
        if (bindings.isEmpty()) {
            throw new IllegalArgumentException("No bindings could be derived from " + path);
        }
        return new QBean<T>((Class)path.getType(), true, bindings);
    }

    private RelationalPathUtils() {}

}
