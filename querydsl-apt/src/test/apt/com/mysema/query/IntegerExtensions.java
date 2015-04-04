package com.mysema.query;

import com.mysema.query.annotations.QueryDelegate;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.path.NumberPath;

public class IntegerExtensions {

    @QueryDelegate(Integer.class)
    public static NumberExpression<Integer> difference(NumberPath<Integer> left, NumberExpression<Integer> right) {
        return right.subtract(left);
    }
}
