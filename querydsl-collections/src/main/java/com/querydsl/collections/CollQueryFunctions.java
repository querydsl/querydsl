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
package com.querydsl.collections;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Operator;
import com.querydsl.core.types.Ops;
import com.querydsl.core.util.MathUtils;
import com.querydsl.core.util.ReflectionUtils;

/**
 * {@code CollQueryFunctions} defines function implementation for use in ColQueryTemplates
 *
 * @author tiwe
 *
 */
public final class CollQueryFunctions {

    private interface BinaryFunction {

        Number apply(Number num1, Number num2);
    }

    private static final BinaryFunction SUM = new BinaryFunction() {
        @Override
        public Number apply(Number num1, Number num2) {
            return MathUtils.sum(num1, num2);
        }
    };

    private static final BinaryFunction MAX = new BinaryFunction() {
        @Override
        public Number apply(Number num1, Number num2) {
            if (num1.getClass().equals(num2.getClass()) && num1 instanceof Comparable) {
                @SuppressWarnings("unchecked") // The types are interchangeable, guarded by previous check
                Comparable<Number> left = (Comparable<Number>) num1;
                return left.compareTo(num2) < 0 ? num2 : num1;
            } else {
                BigDecimal n1 = new BigDecimal(num1.toString());
                BigDecimal n2 = new BigDecimal(num2.toString());
                return n1.compareTo(n2) < 0 ? num2 : num1;
            }
        }
    };

    private static final BinaryFunction MIN = new BinaryFunction() {
        @Override
        public Number apply(Number num1, Number num2) {
            if (num1.getClass().equals(num2.getClass()) && num1 instanceof Comparable) {
                @SuppressWarnings("unchecked") // The types are interchangeable, guarded by previous check
                Comparable<Number> left = (Comparable<Number>) num1;
                return left.compareTo(num2) < 0 ? num1 : num2;
            } else {
                BigDecimal n1 = new BigDecimal(num1.toString());
                BigDecimal n2 = new BigDecimal(num2.toString());
                return n1.compareTo(n2) < 0 ? num1 : num2;
            }
        }
    };

    private static final List<Object> nullList = Arrays.asList((Object) null);

    public static boolean equals(Object o1, Object o2) {
        return Objects.equal(o1, o2);
    }

    public static <T extends Comparable<? super T>> int compareTo(T c1, T c2) {
        if (c1 == null) {
            return c2 == null ? 0 : -1;
        } else if (c2 == null) {
            return 1;
        } else {
            return c1.compareTo(c2);
        }
    }

    public static <A extends Comparable<? super A>> boolean between(A a, A b, A c) {
        return compareTo(a, b) >= 0 && compareTo(a, c) <= 0;
    }

    public static double cot(double x) {
        return Math.cos(x) / Math.sin(x);
    }

    public static double coth(double x) {
        return Math.cosh(x) / Math.sinh(x);
    }

    public static double degrees(double x) {
        return x * 180.0 / Math.PI;
    }

    public static double radians(double x) {
        return x * Math.PI / 180.0;
    }

    public static double log(double x, int y) {
        return Math.log(x) / Math.log(y);
    }

    @Nullable
    public static <T> T coalesce(T... args) {
        for (T arg : args) {
            if (arg != null) {
                return arg;
            }
        }
        return null;
    }

    public static <T> T nullif(T first, T second) {
        if (first.equals(second)) {
            return null;
        } else {
            return first;
        }
    }

    public static int getYearMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR) * 100 + cal.get(Calendar.MONTH) + 1;
    }

    public static int getDayOfMonth(Date date) {
        return getField(date, Calendar.DAY_OF_MONTH);
    }

    public static int getDayOfWeek(Date date) {
        return getField(date, Calendar.DAY_OF_WEEK);
    }

    public static int getDayOfYear(Date date) {
        return getField(date, Calendar.DAY_OF_YEAR);
    }

    private static int getField(Date date, int field) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(field);
    }

    public static int getHour(Date date) {
        return getField(date, Calendar.HOUR_OF_DAY);
    }

    public static int getMilliSecond(Date date) {
        return getField(date, Calendar.MILLISECOND);
    }

    public static int getMinute(Date date) {
        return getField(date, Calendar.MINUTE);
    }

    public static int getMonth(Date date) {
        return getField(date, Calendar.MONTH) + 1;
    }

    public static int getSecond(Date date) {
        return getField(date, Calendar.SECOND);
    }

    public static int getWeek(Date date) {
        return getField(date, Calendar.WEEK_OF_YEAR);
    }

    public static int getYear(Date date) {
        return getField(date, Calendar.YEAR);
    }

    public static int getYearWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR) * 100 + cal.get(Calendar.WEEK_OF_YEAR);
    }

    public static <T> Collection<T> leftJoin(Collection<T> coll) {
        if (coll.isEmpty()) {
            @SuppressWarnings("unchecked") // List only contains null
            Collection<T> rv = (Collection<T>) nullList;
            return rv;
        } else {
            return coll;
        }
    }

    private static Number reduce(Iterable<Number> source, BinaryFunction f) {
        Iterator<Number> it = source.iterator();
        Number result = it.next();
        while (it.hasNext()) {
            result = f.apply(result, it.next());
        }
        return result;
    }

    public static Number aggregate(Collection<Number> source, Expression<?> expr, Operator aggregator) {
        @SuppressWarnings("unchecked") // This is a number expression
        Class<Number> numberType = (Class<Number>) expr.getType();
        if (aggregator == Ops.AggOps.AVG_AGG) {
            Number sum = reduce(source, SUM);
            return sum.doubleValue() / source.size();
        } else if (aggregator == Ops.AggOps.COUNT_AGG) {
            return (long) source.size();
        } else if (aggregator == Ops.AggOps.COUNT_DISTINCT_AGG) {
            if (!Set.class.isInstance(source)) {
                source = Sets.newHashSet(source);
            }
            return (long) source.size();
        } else if (aggregator == Ops.AggOps.MAX_AGG) {
            return MathUtils.cast(reduce(source, MAX), numberType);
        } else if (aggregator == Ops.AggOps.MIN_AGG) {
            return MathUtils.cast(reduce(source, MIN), numberType);
        } else if (aggregator == Ops.AggOps.SUM_AGG) {
            return MathUtils.cast(reduce(source, SUM), numberType);
        } else {
            throw new IllegalArgumentException("Unknown operator " + aggregator);
        }
    }

    public static boolean like(final String str, String like) {
        final StringBuilder pattern = new StringBuilder(like.length() + 4);
        for (int i = 0; i < like.length(); i++) {
            final char ch = like.charAt(i);
            if (ch == '%') {
                pattern.append(".*");
                continue;
            } else if (ch == '_') {
                pattern.append('.');
                continue;
            } else if (ch == '.' || ch == '$' || ch == '^') {
                pattern.append('\\');
            }
            pattern.append(ch);
        }
        if (pattern.toString().equals(like)) {
            return str.equals(like);
        } else {
            return str.matches(pattern.toString());
        }
    }

    public static boolean like(String str, String like, char escape) {
        return like(str, like);
    }

    public static boolean likeIgnoreCase(String str, String like) {
        final StringBuilder pattern = new StringBuilder(like.length() + 4);
        for (int i = 0; i < like.length(); i++) {
            final char ch = like.charAt(i);
            if (ch == '%') {
                pattern.append(".*");
                continue;
            } else if (ch == '_') {
                pattern.append('.');
                continue;
            } else if (ch == '.' || ch == '$' || ch == '^') {
                pattern.append('\\');
            }
            pattern.append(ch);
        }
        if (pattern.toString().equals(like)) {
            return str.equalsIgnoreCase(like);
        } else {
            return Pattern.compile(pattern.toString(), Pattern.CASE_INSENSITIVE)
                    .matcher(str).matches();
        }
    }

    public static boolean likeIgnoreCase(String str, String like, char escape) {
        return likeIgnoreCase(str, like);
    }

    public static <T> T get(Object parent, String f) {
        try {
            Field field = ReflectionUtils.getFieldOrNull(parent.getClass(), f);
            if (field != null) {
                field.setAccessible(true);
                @SuppressWarnings("unchecked")
                T rv = (T) field.get(parent);
                return rv;
            } else {
                throw new IllegalArgumentException("No field " + f + " for " + parent.getClass());
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    private CollQueryFunctions() { }


}
