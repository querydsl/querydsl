package com.querydsl;

import com.querydsl.core.annotations.QueryDelegate;
import com.querydsl.core.types.dsl.BooleanExpression;

public class InnerExtensions {

    public static class ExampleEntity2Extensions {

        @QueryDelegate(ExampleEntity2.class)
        public static BooleanExpression isZero(QExampleEntity2 left) {
            return left.id.eq(0);
        }
    }
}
