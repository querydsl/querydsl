/*
 * Copyright 2012, Mysema Ltd
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

import javax.persistence.Entity;

import com.mysema.query.support.EnumConversion;
import com.mysema.query.support.NumberConversion;
import com.mysema.query.support.NumberConversions;
import com.mysema.query.types.Expression;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.Operation;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.Template;
import com.mysema.query.types.TemplateExpressionImpl;
import com.mysema.query.types.TemplateFactory;

/**
 * Conversions provides module specific projection conversion functionality
 *
 * @author tiwe
 *
 */
public final class Conversions {

    private static final Template ALL = TemplateFactory.DEFAULT.create("{0}.*");

    public static <RT> Expression<RT> convert(Expression<RT> expr) {
        if (isAggSumWithConversion(expr) || isCountAggConversion(expr)) {
            return new NumberConversion<RT>(expr);
        } else if (expr instanceof FactoryExpression) {
            FactoryExpression<RT> factorye = (FactoryExpression<RT>)expr;
            for (Expression<?> e : factorye.getArgs()) {
                if (isAggSumWithConversion(e) || isCountAggConversion(expr)) {
                    return new NumberConversions<RT>(factorye);
                }
            }
        }
        return expr;
    }

    public static <RT> Expression<RT> convertForNativeQuery(Expression<RT> expr) {
        if (expr instanceof Path && expr.getType().isAnnotationPresent(Entity.class)) {
            Path<?> path = (Path<?>)expr;
            if (path.getMetadata().getParent() == null) {
                return (Expression)TemplateExpressionImpl.create(expr.getType(), ALL, expr);
            }
        } else if (Number.class.isAssignableFrom(expr.getType())) {
            return new NumberConversion<RT>(expr);
        } else if (Enum.class.isAssignableFrom(expr.getType())) {
            return new EnumConversion<RT>(expr);
        } else if (expr instanceof FactoryExpression) {
            FactoryExpression<RT> factorye = (FactoryExpression<RT>)expr;
            for (Expression<?> e : factorye.getArgs()) {
                if (Number.class.isAssignableFrom(e.getType())) {
                    return new NumberConversions<RT>(factorye);
                } else if (Enum.class.isAssignableFrom(e.getType())) {
                    return new NumberConversions<RT>(factorye);
                }
            }
        }
        return expr;
    }

    private static boolean isAggSumWithConversion(Expression<?> expr) {
        expr = ExpressionUtils.extract(expr);
        if (expr instanceof Operation) {
            Operation<?> operation = (Operation<?>)expr;
            Class<?> type = operation.getType();
            if (type.equals(Float.class) || type.equals(Integer.class)
                    || type.equals(Short.class) || type.equals(Byte.class)) {
                if (operation.getOperator() == Ops.AggOps.SUM_AGG) {
                    return true;
                } else {
                    for (Expression<?> e : operation.getArgs()) {
                        if (isAggSumWithConversion(e)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private static boolean isCountAggConversion(Expression<?> expr) {
        expr = ExpressionUtils.extract(expr);
        if (expr instanceof Operation) {
            Operation<?> operation = (Operation<?>)expr;
            return operation.getOperator() == Ops.AggOps.COUNT_AGG;
        }
        return false;
    }

    private Conversions() {}

}
