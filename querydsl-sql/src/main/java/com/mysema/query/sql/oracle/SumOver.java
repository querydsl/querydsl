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
package com.mysema.query.sql.oracle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import com.mysema.query.types.Expression;
import com.mysema.query.types.MutableExpressionBase;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.template.NumberTemplate;

/**
 * SumOver is a fluent type for Oracle specific sum over / partition by / order
 * by constructs
 *
 * <p>SumOver doesn't provide the full interface for number expressions. To get an immutable copy with the full
 * expressiveness of Number expressions, call getValue().</p>
 *
 * @author tiwe
 */
public class SumOver<A extends Number & Comparable<? super A>> extends MutableExpressionBase<A> {

    private static final long serialVersionUID = -4130672293308756779L;

    // TODO : change this to List<OrderSpecifier<?>>
    private List<Expression<?>> orderBy = new ArrayList<Expression<?>>();

    @Nullable
    private Expression<?> partitionBy;

    private final Expression<A> target;

    private volatile NumberExpression<A> value;
    
    public SumOver(Expression<A> expr) {
        super(expr.getType());
        target = expr;
    }
    
    public NumberExpression<A> getValue() {
        if (value == null) {
            List<Expression<?>> args = new ArrayList<Expression<?>>();
            StringBuilder builder = new StringBuilder();
            builder.append("sum({0}) over (");
            args.add(target);
            if (partitionBy != null) {
                builder.append("partition by {1}");
                args.add(partitionBy);
            }
            if (!orderBy.isEmpty()) {
                if (partitionBy != null) {
                    builder.append(" ");
                }
                builder.append("order by ");
                boolean first = true;
                for (Expression<?> expr : orderBy) {
                    if (!first) {
                        builder.append(", ");
                    }
                    builder.append("{" + args.size()+"}");
                    args.add(expr);
                    first = false;
                }
            }
            builder.append(")");
            value = NumberTemplate.<A>create(
                    (Class<A>)target.getType(),
                    builder.toString(),
                    args.toArray(new Expression[args.size()]));
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {        
        return getValue().accept(v, context);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof SumOver) {
            SumOver so = (SumOver)o;
            return so.target.equals(target)
                && so.partitionBy.equals(partitionBy)
                && so.orderBy.equals(orderBy);
        } else {
            return false;
        }
    }


    public SumOver<A> orderBy(Expression<?>... orderBy) {
        value = null;
        this.orderBy.addAll(Arrays.asList(orderBy));
        return this;
    }
    
    public SumOver<A> partition(Expression<?> partitionBy) {
        value = null;
        this.partitionBy = partitionBy;
        return this;
    }

}
