package com.querydsl.core.support;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.BooleanExpression;

/**
 *
 * @param <T>
 */
public interface ExtendedSubQuery<T> extends SubQueryExpression<T> {

    BooleanExpression eq(Expression<? extends T> expr);

    BooleanExpression eq(T constant);

    BooleanExpression ne(Expression<? extends T> expr);

    BooleanExpression ne(T constant);

    BooleanExpression contains(Expression<? extends T> right);

    BooleanExpression contains(T constant);

    BooleanExpression exists();

    BooleanExpression notExists();

    BooleanExpression lt(Expression<? extends T> expr);

    BooleanExpression lt(T constant);

    BooleanExpression gt(Expression<? extends T> expr);

    BooleanExpression gt(T constant);

    BooleanExpression loe(Expression<? extends T> expr);

    BooleanExpression loe(T constant);

    BooleanExpression goe(Expression<? extends T> expr);

    BooleanExpression goe(T constant);

}
