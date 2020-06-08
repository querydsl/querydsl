/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.r2dbc;

import com.querydsl.core.types.*;
import com.querydsl.sql.SQLOps;

import java.util.List;

/**
 * UnionUtils provides static utility methods for Union handling
 *
 * @author tiwe
 */
final class UnionUtils {

    public static <T> Expression<T> union(List<SubQueryExpression<T>> union, boolean unionAll) {
        final Operator operator = unionAll ? SQLOps.UNION_ALL : SQLOps.UNION;
        Expression<T> rv = union.get(0);
        for (int i = 1; i < union.size(); i++) {
            rv = ExpressionUtils.operation(rv.getType(), operator, rv, union.get(i));
        }
        return rv;
    }

    public static <T> Expression<T> union(List<SubQueryExpression<T>> union, Path<T> alias,
                                          boolean unionAll) {
        final Expression<T> rv = union(union, unionAll);
        return ExpressionUtils.as(rv, alias);
    }

    private UnionUtils() {
    }

}
