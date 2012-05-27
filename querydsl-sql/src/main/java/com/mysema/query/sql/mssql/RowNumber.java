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
package com.mysema.query.sql.mssql;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.mysema.query.types.Expression;
import com.mysema.query.types.MutableExpressionBase;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.ComparableExpression;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.template.NumberTemplate;

/**
 * RowNumber supports row_number constructs for MS SQL Server
 * 
 * <p>RowNumber doesn't provide the full interface for number expressions. To get an immutable copy with the full
 * expressiveness of Number expressions, call getValue().</p>
 *
 * @author tiwe
 *
 */
public class RowNumber extends MutableExpressionBase<Long> {

    private static final long serialVersionUID = 3499501725767772281L;

    private final List<Expression<?>> partitionBy = new ArrayList<Expression<?>>();

    private final List<OrderSpecifier<?>> orderBy = new ArrayList<OrderSpecifier<?>>();

    @Nullable
    private Expression<Long> target;
    
    private volatile NumberExpression<Long> value;

    public RowNumber() {
        super(Long.class);
    }
    
    public NumberExpression<Long> getValue() {
        if (value == null) {
            List<Expression<?>> args = new ArrayList<Expression<?>>(partitionBy.size() + orderBy.size());
            StringBuilder builder = new StringBuilder("row_number() over (");
            if (!partitionBy.isEmpty()) {
                builder.append("partition by ");
                appendPartition(args, builder);
            }
            if (!orderBy.isEmpty()) {
                if (!partitionBy.isEmpty()) {
                    builder.append(" ");
                }
                builder.append("order by ");
                appendOrder(args, builder);
            }
            builder.append(")");

            if (target != null) {
                builder.append(" as {" + args.size() + "}");
                args.add(target);
            }
            value = NumberTemplate.create(Long.class, builder.toString(), args.toArray(new Expression[args.size()]));
        }
        return value;
    }
    
    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {        
        return getValue().accept(v, context);
    }

    // TODO : externalize
    private void appendPartition(List<Expression<?>> args, StringBuilder builder) {
        boolean first = true;
        for (Expression<?> expr : partitionBy) {
            if (!first) {
                builder.append(", ");
            }
            builder.append("{"+args.size()+"}");
            args.add(expr);
            first = false;
        }
    }

    // TODO : externalize
    private void appendOrder(List<Expression<?>> args, StringBuilder builder) {
        boolean first = true;
        for (OrderSpecifier<?> expr : orderBy) {
            if (!first) {
                builder.append(", ");
            }
            builder.append("{" + args.size()+"}");
            if (!expr.isAscending()) {
                builder.append(" desc");
            }
            args.add(expr.getTarget());
            first = false;
        }
    }

    public RowNumber orderBy(OrderSpecifier<?>... order) {
        value = null;
        for (OrderSpecifier<?> o : order) {
            orderBy.add(o);
        }
        return this;
    }

    public RowNumber orderBy(ComparableExpression<?>... order) {
        value = null;
        for (ComparableExpression<?> o : order) {
            orderBy.add(o.asc());
        }
        return this;
    }

    public RowNumber partitionBy(Expression<?>... exprs) {
        value = null;
        for (Expression<?> expr : exprs) {
            partitionBy.add(expr);
        }
        return this;
    }

    public RowNumber as(Expression<Long> target) {
        value = null;
        this.target = target;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }else if (o instanceof RowNumber) {
            RowNumber rn = (RowNumber)o;
            return partitionBy.equals(rn.partitionBy) && orderBy.equals(rn.orderBy);
        } else {
            return false;
        }
    }

}
