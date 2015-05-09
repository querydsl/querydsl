package com.querydsl;

import com.querydsl.core.annotations.QueryDelegate;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;

public class IntegerExtensions {

    @QueryDelegate(Integer.class)
    public static NumberExpression<Integer> difference(NumberPath<Integer> left, NumberExpression<Integer> right) {
        return right.subtract(left);
    }
}
