package com.mysema.query;

import com.mysema.query.annotations.QueryDelegate;
import com.mysema.query.types.expr.BooleanExpression;

public class InnerExtensions {

    public static class ExampleEntity2Extensions {

        @QueryDelegate(ExampleEntity2.class)
        public static BooleanExpression isZero(QExampleEntity2 left) {
            return left.id.eq(0);
        }
    }
}
