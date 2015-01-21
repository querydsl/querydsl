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
package com.querydsl.jpa;

import java.util.List;

import javax.persistence.Entity;

import com.google.common.collect.Lists;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLOps;
import com.querydsl.core.support.EnumConversion;
import com.querydsl.core.support.NumberConversion;
import com.querydsl.core.support.NumberConversions;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.FactoryExpressionUtils;
import com.querydsl.core.types.Operation;
import com.querydsl.core.types.OperationImpl;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Path;

/**
 * Conversions provides module specific projection conversion functionality
 *
 * @author tiwe
 *
 */
public final class Conversions {

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

    private static boolean isEntityPathAndNeedsWrapping(Expression<?> expr) {
        if ((expr instanceof Path && expr.getType().isAnnotationPresent(Entity.class)) ||
            (expr instanceof EntityPath && !RelationalPath.class.isInstance(expr))) {
            Path<?> path = (Path<?>)expr;
            if (path.getMetadata().getParent() == null) {
                return true;
            }
        }
        return false;
    }

    private static <RT> FactoryExpression<RT> createEntityPathConversions(FactoryExpression<RT> factorye) {
        List<Expression<?>> conversions = Lists.newArrayList();
        for (Expression<?> e : factorye.getArgs()) {
            if (isEntityPathAndNeedsWrapping(e)) {
                conversions.add(OperationImpl.create(e.getType(), SQLOps.ALL, e));
            } else {
                conversions.add(e);
            }
        }
        return FactoryExpressionUtils.wrap(factorye, conversions);
    }

    public static <RT> Expression<RT> convertForNativeQuery(Expression<RT> expr) {
        if (isEntityPathAndNeedsWrapping(expr)) {
            return OperationImpl.create(expr.getType(), SQLOps.ALL, expr);
        } else if (Number.class.isAssignableFrom(expr.getType())) {
            return new NumberConversion<RT>(expr);
        } else if (Enum.class.isAssignableFrom(expr.getType())) {
            return new EnumConversion<RT>(expr);
        } else if (expr instanceof FactoryExpression) {
            FactoryExpression<RT> factorye = (FactoryExpression<RT>)expr;
            boolean numberConversions = false;
            boolean hasEntityPath = false;
            for (Expression<?> e : factorye.getArgs()) {
                if (isEntityPathAndNeedsWrapping(e)) {
                    hasEntityPath = true;
                } else if (Number.class.isAssignableFrom(e.getType())) {
                    numberConversions = true;
                } else if (Enum.class.isAssignableFrom(e.getType())) {
                    numberConversions = true;
                }
            }
            if (hasEntityPath) {
                factorye = createEntityPathConversions(factorye);
            }
            if (numberConversions) {
                factorye = new NumberConversions<RT>(factorye);
            }
            return factorye;
        }
        return expr;
    }

    private static boolean isAggSumWithConversion(Expression<?> expr) {
        expr = ExpressionUtils.extract(expr);
        if (expr instanceof Operation) {
            Operation<?> operation = (Operation<?>)expr;
            Class<?> type = operation.getType();
            if (type.equals(Float.class) || type.equals(Integer.class) || type.equals(Long.class)
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
