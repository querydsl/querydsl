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
package com.querydsl.core.types.dsl;

import java.sql.Time;
import java.util.Collection;
import java.util.Date;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.*;

/**
 * Factory class for {@link Expression} creation.
 *
 * @author tiwe
 *
 */
public final class Expressions {

    public static final NumberExpression<Integer> ONE = numberTemplate(Integer.class, "1");

    public static final NumberExpression<Integer> TWO = numberTemplate(Integer.class, "2");

    public static final NumberExpression<Integer> THREE = numberTemplate(Integer.class, "3");

    public static final NumberExpression<Integer> FOUR = numberTemplate(Integer.class, "4");

    public static final NumberExpression<Integer> ZERO = numberTemplate(Integer.class, "0");

    public static final BooleanExpression TRUE = booleanTemplate("true");

    public static final BooleanExpression FALSE = booleanTemplate("false");

    private Expressions() { }

    /**
     * Create a {@code source as alias} expression
     *
     * @param source source
     * @param alias alias
     * @param <D>
     * @return source as alias
     */
    public static <D> SimpleExpression<D> as(Expression<D> source, Path<D> alias) {
        if (source == null) {
            return as(Expressions.<D>nullExpression(), alias);
        } else {
            return Expressions.operation(alias.getType(), Ops.ALIAS, source, alias);
        }
    }

    /**
     * Create an expression representing the current date as a DateExpression instance
     *
     * @return current date
     */
    public static DateExpression<Date> currentDate() {
        return DateExpression.currentDate();
    }

    /**
     * Create an expression representing the current time instant as a DateTimeExpression instance
     *
     * @return current timestamp
     */
    public static DateTimeExpression<Date> currentTimestamp() {
        return DateTimeExpression.currentTimestamp();
    }

    /**
     * Create an expression representing the current time as a TimeExpression instance
     *
     * @return current time
     */
    public static TimeExpression<Time> currentTime() {
        return TimeExpression.currentTime();
    }

    /**
     * Create a {@code source as alias} expression
     *
     * @param source source
     * @param alias alias
     * @return source as alias
     */
    public static <D> SimpleExpression<D> as(Expression<D> source, String alias) {
        return as(source, ExpressionUtils.path(source.getType(), alias));
    }

    /**
     * Get the intersection of the given Boolean expressions
     *
     * @param exprs predicates
     * @return intersection of predicates
     */
    public static BooleanExpression allOf(BooleanExpression... exprs) {
        BooleanExpression rv = null;
        for (BooleanExpression b : exprs) {
            rv = rv == null ? b : rv.and(b);
        }
        return rv;
    }

    /**
     * Get the union of the given Boolean expressions
     *
     * @param exprs predicates
     * @return union of predicates
     */
    public static BooleanExpression anyOf(BooleanExpression... exprs) {
        BooleanExpression rv = null;
        for (BooleanExpression b : exprs) {
            rv = rv == null ? b : rv.or(b);
        }
        return rv;
    }

    /**
     * Create a Constant expression for the given value
     *
     * @param value constant
     * @return constant expression
     */
    public static <T> Expression<T> constant(T value) {
        return ConstantImpl.create(value);
    }

    /**
     * Create a {@code source as alias} expression
     *
     * @param source source
     * @param alias alias
     * @return source as alias
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <D> SimpleExpression<D> constantAs(D source, Path<D> alias) {
        if (source == null) {
            return as((Expression) nullExpression(), alias);
        } else {
            return as(ConstantImpl.create(source), alias);
        }
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T> SimpleTemplate<T> template(Class<? extends T> cl, String template, Object... args) {
        return simpleTemplate(cl, template, args);
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T> SimpleTemplate<T> template(Class<? extends T> cl, String template, ImmutableList<?> args) {
        return simpleTemplate(cl, template, args);
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T> SimpleTemplate<T> template(Class<? extends T> cl, Template template, Object... args) {
        return simpleTemplate(cl, template, args);
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T> SimpleTemplate<T> template(Class<? extends T> cl, Template template, ImmutableList<?> args) {
        return simpleTemplate(cl, template, args);
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T> SimpleTemplate<T> simpleTemplate(Class<? extends T> cl, String template, Object... args) {
        return simpleTemplate(cl, createTemplate(template), ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T> SimpleTemplate<T> simpleTemplate(Class<? extends T> cl, String template, ImmutableList<?> args) {
        return simpleTemplate(cl, createTemplate(template), args);
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T> SimpleTemplate<T> simpleTemplate(Class<? extends T> cl, Template template, Object... args) {
        return simpleTemplate(cl, template, ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T> SimpleTemplate<T> simpleTemplate(Class<? extends T> cl, Template template, ImmutableList<?> args) {
        return new SimpleTemplate<T>(cl, template, args);
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T> DslTemplate<T> dslTemplate(Class<? extends T> cl, String template, Object... args) {
        return dslTemplate(cl, createTemplate(template), ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T> DslTemplate<T> dslTemplate(Class<? extends T> cl, String template, ImmutableList<?> args) {
        return dslTemplate(cl, createTemplate(template), args);
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T> DslTemplate<T> dslTemplate(Class<? extends T> cl, Template template, Object... args) {
        return dslTemplate(cl, template, ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T> DslTemplate<T> dslTemplate(Class<? extends T> cl, Template template, ImmutableList<?> args) {
        return new DslTemplate<T>(cl, template, args);
    }


    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Comparable<?>> ComparableTemplate<T> comparableTemplate(Class<? extends T> cl,
            String template, Object... args) {
        return comparableTemplate(cl, createTemplate(template), ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Comparable<?>> ComparableTemplate<T> comparableTemplate(Class<? extends T> cl,
                                                                                     String template, ImmutableList<?> args) {
        return comparableTemplate(cl, createTemplate(template), args);
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Comparable<?>> ComparableTemplate<T> comparableTemplate(Class<? extends T> cl,
                                                                                     Template template, Object... args) {
        return comparableTemplate(cl, template, ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Comparable<?>> ComparableTemplate<T> comparableTemplate(Class<? extends T> cl,
                                                                                     Template template, ImmutableList<?> args) {
        return new ComparableTemplate<T>(cl, template, args);
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Comparable<?>> DateTemplate<T> dateTemplate(Class<? extends T> cl,
                                                                         String template, Object... args) {
        return dateTemplate(cl, createTemplate(template), ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Comparable<?>> DateTemplate<T> dateTemplate(Class<? extends T> cl,
                                                                         String template, ImmutableList<?> args) {
        return dateTemplate(cl, createTemplate(template), args);
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Comparable<?>> DateTemplate<T> dateTemplate(Class<? extends T> cl,
                                                                         Template template, Object... args) {
        return dateTemplate(cl, template, ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Comparable<?>> DateTemplate<T> dateTemplate(Class<? extends T> cl,
                                                                         Template template, ImmutableList<?> args) {
        return new DateTemplate<T>(cl, template, args);
    }


    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Comparable<?>> DateTimeTemplate<T> dateTimeTemplate(Class<? extends T> cl,
                                                                                 String template, Object... args) {
        return dateTimeTemplate(cl, createTemplate(template), ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Comparable<?>> DateTimeTemplate<T> dateTimeTemplate(Class<? extends T> cl,
                                                                                 String template, ImmutableList<?> args) {
        return dateTimeTemplate(cl, createTemplate(template), args);
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Comparable<?>> DateTimeTemplate<T> dateTimeTemplate(Class<? extends T> cl,
                                                                                   Template template, Object... args) {
        return dateTimeTemplate(cl, template, ImmutableList.copyOf(args));
    }


    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Comparable<?>> DateTimeTemplate<T> dateTimeTemplate(Class<? extends T> cl,
                                                                                 Template template, ImmutableList<?> args) {
        return new DateTimeTemplate<T>(cl, template, args);
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Comparable<?>> TimeTemplate<T> timeTemplate(Class<? extends T> cl,
                                                                           String template, Object... args) {
        return timeTemplate(cl, createTemplate(template), ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Comparable<?>> TimeTemplate<T> timeTemplate(Class<? extends T> cl,
                                                                         String template, ImmutableList<?> args) {
        return timeTemplate(cl, createTemplate(template), args);
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Comparable<?>> TimeTemplate<T> timeTemplate(Class<? extends T> cl,
                                                                         Template template, Object... args) {
        return timeTemplate(cl, template, ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Comparable<?>> TimeTemplate<T> timeTemplate(Class<? extends T> cl,
                                                                         Template template, ImmutableList<?> args) {
        return new TimeTemplate<T>(cl, template, args);
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Enum<T>> EnumTemplate<T> enumTemplate(Class<? extends T> cl,
                                                                     String template, Object... args) {
        return enumTemplate(cl, createTemplate(template), ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Enum<T>> EnumTemplate<T> enumTemplate(Class<? extends T> cl,
                                                                     String template, ImmutableList<?> args) {
        return enumTemplate(cl, createTemplate(template), args);
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Enum<T>> EnumTemplate<T> enumTemplate(Class<? extends T> cl,
                                                                     Template template, Object... args) {
        return enumTemplate(cl, template, ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Enum<T>> EnumTemplate<T> enumTemplate(Class<? extends T> cl,
                                                                   Template template, ImmutableList<?> args) {
        return new EnumTemplate<T>(cl, template, args);
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Number & Comparable<?>> NumberTemplate<T> numberTemplate(Class<? extends T> cl,
            String template, Object... args) {
        return numberTemplate(cl, createTemplate(template), ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Number & Comparable<?>> NumberTemplate<T> numberTemplate(Class<? extends T> cl,
                                                                                      String template, ImmutableList<?> args) {
        return numberTemplate(cl, createTemplate(template), args);
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Number & Comparable<?>> NumberTemplate<T> numberTemplate(Class<? extends T> cl,
                                                                                        Template template, Object... args) {
        return numberTemplate(cl, template, ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Number & Comparable<?>> NumberTemplate<T> numberTemplate(Class<? extends T> cl,
                                                                                      Template template, ImmutableList<?> args) {
        return new NumberTemplate<T>(cl, template, args);
    }

    /**
     * Create a new Template expression
     *
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static StringTemplate stringTemplate(String template, Object... args) {
        return stringTemplate(createTemplate(template), ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static StringTemplate stringTemplate(String template, ImmutableList<?> args) {
        return stringTemplate(createTemplate(template), args);
    }


    /**
     * Create a new Template expression
     *
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static StringTemplate stringTemplate(Template template, Object... args) {
        return stringTemplate(template, ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static StringTemplate stringTemplate(Template template, ImmutableList<?> args) {
        return new StringTemplate(template, ImmutableList.copyOf(args));
    }


    /**
     * Create a new Template expression
     *
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static BooleanTemplate booleanTemplate(String template, Object... args) {
        return booleanTemplate(createTemplate(template), ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static BooleanTemplate booleanTemplate(String template, ImmutableList<?> args) {
        return booleanTemplate(createTemplate(template), args);
    }

    /**
     * Create a new Template expression
     *
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static BooleanTemplate booleanTemplate(Template template, Object... args) {
        return booleanTemplate(template, ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static BooleanTemplate booleanTemplate(Template template, ImmutableList<?> args) {
        return new BooleanTemplate(template, ImmutableList.copyOf(args));
    }

    /**
     * Create a new Predicate operation
     *
     * @param operator operator
     * @param args operation arguments
     * @return operation expression
     */
    public static BooleanOperation predicate(Operator operator, Expression<?>... args) {
        return new BooleanOperation(operator, args);
    }

    /**
     * Create a new Operation expression
     *
     * @param type type of expression
     * @param operator operator
     * @param args operation arguments
     * @return operation expression
     */
    public static <T> SimpleOperation<T> operation(Class<? extends T> type, Operator operator,
            Expression<?>... args) {
        return simpleOperation(type, operator, args);

    }

    /**
     * Create a new Operation expression
     *
     * @param type type of expression
     * @param operator operator
     * @param args operation arguments
     * @return operation expression
     */
    public static <T> SimpleOperation<T> simpleOperation(Class<? extends T> type, Operator operator,
                                                         Expression<?>... args) {
        return new SimpleOperation<T>(type, operator, args);
    }

    /**
     * Create a new Operation expression
     *
     * @param type type of expression
     * @param operator operator
     * @param args operation arguments
     * @return operation expression
     */
    public static <T> DslOperation<T> dslOperation(Class<? extends T> type, Operator operator,
            Expression<?>... args) {
        return new DslOperation<T>(type, operator, args);
    }

    /**
     * Create a new Boolean operation
     *
     * @param operator operator
     * @param args operation arguments
     * @return operation expression
     */
    public static BooleanOperation booleanOperation(Operator operator, Expression<?>... args) {
        return predicate(operator, args);
    }

    /**
     * Create a new Operation expression
     *
     * @param type type of expression
     * @param operator operator
     * @param args operation arguments
     * @return operation expression
     */
    public static <T extends Comparable<?>> ComparableOperation<T> comparableOperation(Class<? extends T> type,
            Operator operator, Expression<?>... args) {
        return new ComparableOperation<T>(type, operator, args);
    }

    /**
     * Create a new Operation expression
     *
     * @param type type of expression
     * @param operator operator
     * @param args operation arguments
     * @return operation expression
     */
    public static <T extends Comparable<?>> DateOperation<T> dateOperation(Class<? extends T> type,
            Operator operator, Expression<?>... args) {
        return new DateOperation<T>(type, operator, args);
    }

    /**
     * Create a new Operation expression
     *
     * @param type type of expression
     * @param operator operator
     * @param args operation arguments
     * @return operation expression
     */
    public static <T extends Comparable<?>> DateTimeOperation<T> dateTimeOperation(Class<? extends T> type,
            Operator operator, Expression<?>... args) {
        return new DateTimeOperation<T>(type, operator, args);
    }

    /**
     * Create a new Operation expression
     *
     * @param type type of expression
     * @param operator operator
     * @param args operation arguments
     * @return operation expression
     */
    public static <T extends Comparable<?>> TimeOperation<T> timeOperation(Class<? extends T> type,
            Operator operator, Expression<?>... args) {
        return new TimeOperation<T>(type, operator, args);
    }

    /**
     * Create a new Operation expression
     *
     * @param type type of expression
     * @param operator operator
     * @param args operation arguments
     * @return operation expression
     */
    public static <T extends Number & Comparable<?>> NumberOperation<T> numberOperation(Class<? extends T> type,
            Operator operator, Expression<?>... args) {
        return new NumberOperation<T>(type, operator, args);
    }

    /**
     * Create a new Operation expression
     *
     * @param operator operator
     * @param args operation arguments
     * @return operation expression
     */
    public static StringOperation stringOperation(Operator operator, Expression<?>... args) {
        return new StringOperation(operator, args);
    }

    /**
     * Create a new Path expression
     *
     * @param type type of expression
     * @param variable variable name
     * @return path expression
     */
    public static <T> SimplePath<T> path(Class<? extends T> type, String variable) {
        return simplePath(type, variable);
    }

    /**
     * Create a new Path expression
     *
     * @param type type of expression
     * @param parent parent path
     * @param property property name
     * @return property path
     */
    public static <T> SimplePath<T> path(Class<? extends T> type, Path<?> parent, String property) {
        return simplePath(type, parent, property);
    }

    /**
     * Create a new Path expression
     *
     * @param type type of expression
     * @param metadata path metadata
     * @param <T> type of expression
     * @return path expression
     */
    public static <T> SimplePath<T> path(Class<? extends T> type, PathMetadata metadata) {
        return simplePath(type, metadata);
    }

    /**
     * Create a new Path expression
     *
     * @param type type of expression
     * @param variable variable name
     * @return path expression
     */
    public static <T> SimplePath<T> simplePath(Class<? extends T> type, String variable) {
        return new SimplePath<T>(type, PathMetadataFactory.forVariable(variable));
    }

    /**
     * Create a new Path expression
     *
     * @param type type of expression
     * @param parent parent path
     * @param property property name
     * @return property path
     */
    public static <T> SimplePath<T> simplePath(Class<? extends T> type, Path<?> parent, String property) {
        return new SimplePath<T>(type, PathMetadataFactory.forProperty(parent, property));
    }

    /**
     * Create a new Path expression
     *
     * @param type type of expression
     * @param metadata path metadata
     * @param <T> type of expression
     * @return path expression
     */
    public static <T> SimplePath<T> simplePath(Class<? extends T> type, PathMetadata metadata) {
        return new SimplePath<T>(type, metadata);
    }

    /**
     * Create a new Path expression
     *
     * @param type type of expression
     * @param variable variable name
     * @return path expression
     */
    public static <T> DslPath<T> dslPath(Class<? extends T> type, String variable) {
        return new DslPath<T>(type, PathMetadataFactory.forVariable(variable));
    }

    /**
     * Create a new Path expression
     *
     * @param type type of expression
     * @param parent parent path
     * @param property property name
     * @return property path
     */
    public static <T> DslPath<T> dslPath(Class<? extends T> type, Path<?> parent, String property) {
        return new DslPath<T>(type, PathMetadataFactory.forProperty(parent, property));
    }

    /**
     * Create a new Path expression
     *
     * @param type type of expression
     * @param metadata path metadata
     * @param <T> type of expression
     * @return path expression
     */
    public static <T> DslPath<T> dslPath(Class<? extends T> type, PathMetadata metadata) {
        return new DslPath<T>(type, metadata);
    }

    /**
     * Create a new Path expression
     *
     * @param type type of expression
     * @param variable variable name
     * @return path expression
     */
    public static <T extends Comparable<?>> ComparablePath<T> comparablePath(Class<? extends T> type,
            String variable) {
        return new ComparablePath<T>(type, PathMetadataFactory.forVariable(variable));
    }

    /**
     * Create a new Path expression
     *
     * @param type type of expression
     * @param parent parent path
     * @param property property path
     * @return path expression
     */
    public static <T extends Comparable<?>> ComparablePath<T> comparablePath(Class<? extends T> type,
            Path<?> parent, String property) {
        return new ComparablePath<T>(type, PathMetadataFactory.forProperty(parent, property));
    }

    /**
     * Create a new Path expression
     *
     * @param type type of expression
     * @param metadata path metadata
     * @param <T> type of expression
     * @return path expression
     */
    public static <T extends Comparable<?>> ComparablePath<T> comparablePath(Class<? extends T> type,
                                                                             PathMetadata metadata) {
        return new ComparablePath<T>(type, metadata);
    }

    /**
     * Create a new Path expression
     *
     * @param type type of expression
     * @param variable variable name
     * @return path expression
     */
    public static <T extends Comparable<?>> ComparableEntityPath<T> comparableEntityPath(Class<? extends T> type,
                                                                             String variable) {
        return new ComparableEntityPath<T>(type, PathMetadataFactory.forVariable(variable));
    }

    /**
     * Create a new Path expression
     *
     * @param type type of expression
     * @param parent parent path
     * @param property property name
     * @return path expression
     */
    public static <T extends Comparable<?>> ComparableEntityPath<T> comparableEntityPath(Class<? extends T> type,
                                                                             Path<?> parent, String property) {
        return new ComparableEntityPath<T>(type, PathMetadataFactory.forProperty(parent, property));
    }

    /**
     * Create a new Path expression
     *
     * @param type type of expression
     * @param metadata path metadata
     * @param <T> type of expression
     * @return path expression
     */
    public static <T extends Comparable<?>> ComparableEntityPath<T> comparableEntityPath(Class<? extends T> type,
                                                                             PathMetadata metadata) {
        return new ComparableEntityPath<T>(type, metadata);
    }

    /**
     * Create a new Path expression
     *
     * @param type type of expression
     * @param variable variable name
     * @return path expression
     */
    public static <T extends Comparable<?>> DatePath<T> datePath(Class<? extends T> type, String variable) {
        return new DatePath<T>(type, PathMetadataFactory.forVariable(variable));
    }

    /**
     * Create a new Path expression
     *
     * @param type type of expression
     * @param parent parent path
     * @param property property name
     * @return path expression
     */
    public static <T extends Comparable<?>> DatePath<T> datePath(Class<? extends T> type, Path<?> parent,
            String property) {
        return new DatePath<T>(type, PathMetadataFactory.forProperty(parent, property));
    }

    /**
     * Create a new Path expression
     *
     * @param type type of expression
     * @param metadata path metadata
     * @param <T> type of expression
     * @return new path instane
     */
    public static <T extends Comparable<?>> DatePath<T> datePath(Class<? extends T> type, PathMetadata metadata) {
        return new DatePath<T>(type, metadata);
    }

    /**
     * Create a new Path expression
     *
     * @param type type of expression
     * @param variable variable name
     * @return path expression
     */
    public static <T extends Comparable<?>> DateTimePath<T> dateTimePath(Class<? extends T> type, String variable) {
        return new DateTimePath<T>(type, PathMetadataFactory.forVariable(variable));
    }

    /**
     * Create a new Path expression
     *
     * @param type type of expression
     * @param parent parent path
     * @param property property name
     * @return path expression
     */
    public static <T extends Comparable<?>> DateTimePath<T> dateTimePath(Class<? extends T> type, Path<?> parent,
            String property) {
        return new DateTimePath<T>(type, PathMetadataFactory.forProperty(parent, property));
    }

    /**
     * Create a new Path expression
     *
     * @param type type of expression
     * @param metadata path metadata
     * @param <T> type of expression
     * @return path expression
     */
    public static <T extends Comparable<?>> DateTimePath<T> dateTimePath(Class<? extends T> type, PathMetadata metadata) {
        return new DateTimePath<T>(type, metadata);
    }

    /**
     * Create a new Path expression
     *
     * @param type type of expression
     * @param variable variable name
     * @return path expression
     */
    public static <T extends Comparable<?>> TimePath<T> timePath(Class<? extends T> type, String variable) {
        return new TimePath<T>(type, PathMetadataFactory.forVariable(variable));
    }

    /**
     * Create a new Path expression
     *
     * @param type type of expression
     * @param parent parent path
     * @param property property name
     * @return property path
     */
    public static <T extends Comparable<?>> TimePath<T> timePath(Class<? extends T> type, Path<?> parent,
            String property) {
        return new TimePath<T>(type, PathMetadataFactory.forProperty(parent, property));
    }

    /**
     * Create a new Path expression
     *
     * @param type type of expression
     * @param metadata path metadata
     * @param <T> type of expression
     * @return path expression
     */
    public static <T extends Comparable<?>> TimePath<T> timePath(Class<? extends T> type, PathMetadata metadata) {
        return new TimePath<T>(type, metadata);
    }

    /**
     * Create a new Path expression
     *
     * @param type type of expression
     * @param variable variable name
     * @return path expression
     */
    public static <T extends Number & Comparable<?>> NumberPath<T> numberPath(Class<? extends T> type,
            String variable) {
        return new NumberPath<T>(type, PathMetadataFactory.forVariable(variable));
    }

    /**
     * Create a new Path expression
     *
     * @param type type of expression
     * @param parent parent path
     * @param property property name
     * @return path expression
     */
    public static <T extends Number & Comparable<?>> NumberPath<T> numberPath(Class<? extends T> type,
            Path<?> parent, String property) {
        return new NumberPath<T>(type, PathMetadataFactory.forProperty(parent, property));
    }

    /**
     * Create new Path expression
     *
     * @param type type of expression
     * @param metadata path metadata
     * @param <T> type of expression
     * @return path expression
     */
    public static <T extends Number & Comparable<?>> NumberPath<T> numberPath(Class<? extends T> type, PathMetadata metadata) {
        return new NumberPath<T>(type, metadata);
    }

    /**
     * Create a new Path expression
     *
     * @param variable variable name
     * @return path expression
     */
    public static StringPath stringPath(String variable) {
        return new StringPath(PathMetadataFactory.forVariable(variable));
    }

    /**
     * Create a new Path expression
     *
     * @param parent parent path
     * @param property property name
     * @return property path
     */
    public static StringPath stringPath(Path<?> parent, String property) {
        return new StringPath(PathMetadataFactory.forProperty(parent, property));
    }

    /**
     * Create a new Path expression
     *
     * @param metadata path metadata
     * @return path expression
     */
    public static StringPath stringPath(PathMetadata metadata) {
        return new StringPath(metadata);
    }

    /**
     * Create a new Path expression
     *
     * @param variable variable name
     * @return path expression
     */
    public static BooleanPath booleanPath(String variable) {
        return new BooleanPath(PathMetadataFactory.forVariable(variable));
    }

    /**
     * Create a new Path expression
     *
     * @param parent parent path
     * @param property property name
     * @return property path
     */
    public static BooleanPath booleanPath(Path<?> parent, String property) {
        return new BooleanPath(PathMetadataFactory.forProperty(parent, property));
    }

    /**
     * Create a new Path expression
     *
     * @param metadata path metadata
     * @return path expression
     */
    public static BooleanPath booleanPath(PathMetadata metadata) {
        return new BooleanPath(metadata);
    }

    /**
     * Create a builder for a case expression
     *
     * @return case builder
     */
    public static CaseBuilder cases() {
        return new CaseBuilder();
    }

    /**
     * Combine the given expressions into a list expression
     *
     * @param exprs list elements
     * @return list expression
     */
    public static SimpleExpression<Tuple> list(SimpleExpression<?>... exprs) {
        return list(Tuple.class, exprs);
    }

    /**
     * Combine the given expressions into a list expression
     *
     * @param clazz type of list expression
     * @param exprs list elements
     * @return list expression
     */
    public static <T> SimpleExpression<T> list(Class<T> clazz, SimpleExpression<?>... exprs) {
        SimpleExpression<T> rv = (SimpleExpression<T>) exprs[0];
        for (int i = 1; i < exprs.length; i++) {
            rv = new SimpleOperation<T>(clazz, Ops.LIST, rv, exprs[i]);
        }
        return rv;
    }

    /**
     * Create a null expression for the specified type
     *
     * @return null expression
     */
    @SuppressWarnings("unchecked")//does not produce non-null instances of T
    public static <T> NullExpression<T> nullExpression() {
        return (NullExpression<T>) NullExpression.DEFAULT;
    }

    /**
     * Create a null expression for the specified type
     *
     * @param type type of expression
     * @param <T> type of expression
     * @return null expression
     */
    public static <T> NullExpression<T> nullExpression(Class<T> type) {
        return nullExpression();
    }

    /**
     * Create a null expression for the specified path
     *
     * @param path path for type cast
     * @param <T> type of expression
     * @return null expression
     */
    public static <T> NullExpression<T> nullExpression(Path<T> path) {
        return nullExpression();
    }

    /**
     * Create a new Enum operation expression
     *
     * @param type type of expression
     * @param operator operator
     * @param args operation arguments
     * @param <T> type of expression
     * @return operation expression
     */
    public static <T extends Enum<T>> EnumOperation<T> enumOperation(Class<? extends T> type, Operator operator,
                                                                     Expression<?>... args) {
        return new EnumOperation<T>(type, operator, args);
    }

    /**
     * Create a new Path expression
     *
     * @param type type of expression
     * @param variable variable name
     * @return path expression
     */
    public static <T extends Enum<T>> EnumPath<T> enumPath(Class<? extends T> type, String variable) {
        return new EnumPath<T>(type, PathMetadataFactory.forVariable(variable));
    }

    /**
     * Create a new Path expression
     *
     * @param type type of expression
     * @param parent parent path
     * @param property property name
     * @return property path
     */
    public static <T extends Enum<T>> EnumPath<T> enumPath(Class<? extends T> type, Path<?> parent, String property) {
        return new EnumPath<T>(type, PathMetadataFactory.forProperty(parent, property));
    }

    /**
     * Create a new Path expression
     *
     * @param type type of expression
     * @param metadata path metadata
     * @param <T> type of expression
     * @return path expression
     */
    public static <T extends Enum<T>> EnumPath<T> enumPath(Class<? extends T> type, PathMetadata metadata) {
        return new EnumPath<T>(type, metadata);
    }

    /**
     * Create a new Collection operation expression
     *
     * @param elementType element type
     * @param operator operator
     * @param args operation arguments
     * @param <T> type of expression
     * @return operation expression
     */
    public static <T> CollectionExpression<Collection<T>, T> collectionOperation(Class<T> elementType, Operator operator,
                                                                  Expression<?>... args) {
        return new CollectionOperation<T>(elementType, operator, args);
    }

    /**
     * Create a new Path expression
     *
     * @param type element type
     * @param queryType element expression type
     * @param metadata path metadata
     * @param <E> element type
     * @param <Q> element expression type
     * @return path expression
     */
    public static <E, Q extends SimpleExpression<? super E>> CollectionPath<E, Q> collectionPath(Class<E> type, Class<Q> queryType, PathMetadata metadata) {
        return new CollectionPath<E, Q>(type, queryType, metadata);
    }

    /**
     * Create a new Path expression
     *
     * @param type element type
     * @param queryType element expression type
     * @param metadata path metadata
     * @param <E> element type
     * @param <Q> element expression type
     * @return path expression
     */
    public static <E, Q extends SimpleExpression<? super E>> ListPath<E, Q> listPath(Class<E> type, Class<Q> queryType, PathMetadata metadata) {
        return new ListPath<E, Q>(type, queryType, metadata);
    }

    /**
     * Create a new Path expression
     *
     * @param type element type
     * @param queryType element expression type
     * @param metadata path metadata
     * @param <E> element type
     * @param <Q> element expression type
     * @return path expression
     */
    public static <E, Q extends SimpleExpression<? super E>> SetPath<E, Q> setPath(Class<E> type, Class<Q> queryType, PathMetadata metadata) {
        return new SetPath<E, Q>(type, queryType, metadata);
    }

    /**
     * Create a new Path expression
     *
     * @param keyType key type
     * @param valueType value type
     * @param queryType value expression type
     * @param metadata path metadata
     * @param <K> key type
     * @param <V> value type
     * @param <E> value expression type
     * @return path expression
     */
    public static <K, V, E extends SimpleExpression<? super V>> MapPath<K,V,E> mapPath(Class<? super K> keyType,
                                                                                       Class<? super V> valueType,
                                                                                       Class<E> queryType,
                                                                                       PathMetadata metadata) {
        return new MapPath<K,V,E>(keyType, valueType, queryType, metadata);
    }

    /**
     * Create a new Path expression
     *
     * @param arrayType array type
     * @param metadata path metadata
     * @param <A> array type
     * @param <E> element type
     * @return path expression
     */
    public static <A, E> ArrayPath<A, E> arrayPath(Class<A> arrayType, PathMetadata metadata) {
        return new ArrayPath<A, E>(arrayType, metadata);
    }

    private static Template createTemplate(String template) {
        return TemplateFactory.DEFAULT.create(template);
    }

}
