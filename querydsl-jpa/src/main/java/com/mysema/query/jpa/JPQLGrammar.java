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
package com.mysema.query.jpa;

import com.mysema.query.types.CollectionExpression;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Ops;
import com.mysema.query.types.expr.ComparableExpression;
import com.mysema.query.types.expr.ComparableOperation;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.NumberOperation;
import com.mysema.query.types.expr.SimpleOperation;
import com.mysema.query.types.expr.StringExpression;
import com.mysema.query.types.expr.StringOperation;

/**
 * JPQLGrammar provides factory methods for JPQL specific operations
 * elements.
 *
 * @author tiwe
 */
@SuppressWarnings("unchecked")
public final class JPQLGrammar {

    private JPQLGrammar() {}
    
    public static <D> Expression<D> all(CollectionExpression<?,D> col) {
        return SimpleOperation.create((Class)col.getParameter(0), Ops.QuantOps.ALL, (Expression<?>)col);
    }

    public static <D> Expression<D> any(CollectionExpression<?,D> col) {
        return SimpleOperation.create((Class)col.getParameter(0), Ops.QuantOps.ANY, (Expression<?>)col);
    }

    public static <A extends Comparable<? super A>> ComparableExpression<A> avg(CollectionExpression<?,A> col) {
        return ComparableOperation.create((Class)col.getParameter(0), Ops.QuantOps.AVG_IN_COL, (Expression<?>)col);
    }

    public static <A extends Comparable<? super A>> ComparableExpression<A> max(CollectionExpression<?,A> left) {
        return ComparableOperation.create((Class)left.getParameter(0), Ops.QuantOps.MAX_IN_COL, (Expression<?>)left);
    }

    public static <A extends Comparable<? super A>> ComparableExpression<A> min(CollectionExpression<?,A> left) {
        return ComparableOperation.create((Class)left.getParameter(0), Ops.QuantOps.MIN_IN_COL, (Expression<?>)left);
    }

    public static <D> Expression<D> some(CollectionExpression<?,D> col) {
        return any(col);
    }

    /**
     * SUM returns Long when applied to state-fields of integral types (other
     * than BigInteger); Double when applied to state-fields of floating point
     * types; BigInteger when applied to state-fields of type BigInteger; and
     * BigDecimal when applied to state-fields of type BigDecimal.
     */
    public static <D extends Number & Comparable<? super D>> NumberExpression<?> sum(Expression<D> left) {
        Class<?> type = left.getType();
        if (type.equals(Byte.class) || type.equals(Integer.class) || type.equals(Short.class)) {
            type = Long.class;
        } else if (type.equals(Float.class)) {
            type = Double.class;
        }
        return NumberOperation.create((Class<D>) type, Ops.AggOps.SUM_AGG, left);
    }

    public static <D extends Number & Comparable<? super D>> NumberExpression<Long> sumAsLong(Expression<D> left) {
        return sum(left).longValue();
    }

    public static <D extends Number & Comparable<? super D>> NumberExpression<Double> sumAsDouble(Expression<D> left) {
        return sum(left).doubleValue();
    }
    
    public static StringExpression type(EntityPath<?> path) {
        return StringOperation.create(JPQLTemplates.TYPE, path);
    }

}
