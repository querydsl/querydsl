/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.core.util;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.querydsl.core.types.Operator;
import com.querydsl.core.types.Ops;

/**
 * MathUtils provides Math related functionality
 *
 * @author tiwe
 *
 */
public final class MathUtils {

    private MathUtils() { }

    @SuppressWarnings("unchecked")
    public static <D extends Number> D result(D num1, Number num2, Operator operator) {
        BigDecimal d1 = toBigDecimal(num1);
        BigDecimal d2 = toBigDecimal(num2);
        if (operator == Ops.ADD) {
            return MathUtils.cast(d1.add(d2), (Class<D>) num1.getClass());
        } else if (operator == Ops.SUB) {
            return MathUtils.cast(d1.subtract(d2), (Class<D>) num1.getClass());
        } else if (operator == Ops.MULT) {
            return MathUtils.cast(d1.multiply(d2), (Class<D>) num1.getClass());
        } else if (operator == Ops.DIV) {
            return MathUtils.cast(d1.divide(d2), (Class<D>) num1.getClass());
        } else {
            throw new IllegalArgumentException(operator.toString());
        }
    }

    @SuppressWarnings("unchecked")
    public static <D extends Number> D sum(D num1, Number num2) {
        BigDecimal res = toBigDecimal(num1).add(toBigDecimal(num2));
        return MathUtils.cast(res, (Class<D>) num1.getClass());
    }

    @SuppressWarnings("unchecked")
    public static <D extends Number> D difference(D num1, Number num2) {
        BigDecimal res = toBigDecimal(num1).subtract(toBigDecimal(num2));
        return MathUtils.cast(res, (Class<D>) num1.getClass());
    }

    public static <D extends Number> D cast(Number num, Class<D> type) {
        D rv;
        if (type.isInstance(num)) {
            rv = type.cast(num);
        } else if (type.equals(Byte.class)) {
            rv = type.cast(num.byteValue());
        } else if (type.equals(Double.class)) {
            rv = type.cast(num.doubleValue());
        } else if (type.equals(Float.class)) {
            rv = type.cast(num.floatValue());
        } else if (type.equals(Integer.class)) {
            rv = type.cast(num.intValue());
        } else if (type.equals(Long.class)) {
            rv = type.cast(num.longValue());
        } else if (type.equals(Short.class)) {
            rv = type.cast(num.shortValue());
        } else if (type.equals(BigDecimal.class)) {
            rv = type.cast(new BigDecimal(num.toString()));
        } else if (type.equals(BigInteger.class)) {
            if (num instanceof BigDecimal) {
                rv = type.cast(((BigDecimal) num).toBigInteger());
            } else {
                rv = type.cast(new BigInteger(num.toString()));
            }
        } else {
            throw new IllegalArgumentException(String.format("Unsupported target type : %s", type.getSimpleName()));
        }
        return rv;
    }

    private static BigDecimal toBigDecimal(Number num) {
        if (num instanceof BigDecimal) {
            return (BigDecimal) num;
        } else {
            return new BigDecimal(num.toString());
        }
    }
}
