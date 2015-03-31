/*
 * Copyright 2011, Mysema Ltd
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
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.*;

/**
 * Expression factory class
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

    private Expressions() {}

    public static <D> SimpleExpression<D> as(Expression<D> source, Path<D> alias) {
        if (source == null) {
            return as(Expressions.<D>nullExpression(), alias);
        } else {
            return Expressions.operation(alias.getType(), Ops.ALIAS, source, alias);
        }
    }

    /**
     * Get an expression representing the current date as a DateExpression instance
     *
     * @return
     */
    public static DateExpression<Date> currentDate() {
        return DateExpression.currentDate();
    }

    /**
     * Get an expression representing the current time instant as a DateTimeExpression instance
     *
     * @return
     */
    public static DateTimeExpression<Date> currentTimestamp() {
        return DateTimeExpression.currentTimestamp();
    }

    /**
     * Get an expression representing the current time as a TimeExpression instance
     *
     * @return
     */
    public static TimeExpression<Time> currentTime() {
        return TimeExpression.currentTime();
    }

    /**
     * Create the alias expression source as alias
     *
     * @param source
     * @param alias
     * @return
     */
    public static <D> SimpleExpression<D> as(Expression<D> source, String alias) {
        return as(source, ExpressionUtils.path(source.getType(), alias));
    }

    /**
     * Get the intersection of the given Boolean expressions
     *
     * @param exprs
     * @return
     */
    public static BooleanExpression allOf(BooleanExpression... exprs) {
        return BooleanExpression.allOf(exprs);
    }

    /**
     * Get the union of the given Boolean expressions
     *
     * @param exprs
     * @return
     */
    public static BooleanExpression anyOf(BooleanExpression... exprs) {
        return BooleanExpression.anyOf(exprs);
    }

    /**
     * Create a Constant expression for the given value
     *
     * @param value
     * @return
     */
    public static <T> Expression<T> constant(T value) {
        return ConstantImpl.create(value);
    }

    /**
     * Get the alias expression source as alias
     *
     * @param source
     * @param alias
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <D> SimpleExpression<D> constantAs(D source, Path<D> alias) {
        if (source == null) {
            return as((Expression)nullExpression(), alias);
        } else {
            return as(ConstantImpl.create(source), alias);
        }
    }

    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T> SimpleTemplate<T> template(Class<? extends T> cl, String template, Object... args) {
        return simpleTemplate(cl, template, args);
    }

    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T> SimpleTemplate<T> template(Class<? extends T> cl, String template, ImmutableList<?> args) {
        return simpleTemplate(cl, template, args);
    }

    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T> SimpleTemplate<T> template(Class<? extends T> cl, Template template, Object... args) {
        return simpleTemplate(cl, template, args);
    }

    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T> SimpleTemplate<T> template(Class<? extends T> cl, Template template, ImmutableList<?> args) {
        return simpleTemplate(cl, template, args);
    }

    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T> SimpleTemplate<T> simpleTemplate(Class<? extends T> cl, String template, Object... args) {
        return simpleTemplate(cl, createTemplate(template), ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T> SimpleTemplate<T> simpleTemplate(Class<? extends T> cl, String template, ImmutableList<?> args) {
        return simpleTemplate(cl, createTemplate(template), args);
    }

    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T> SimpleTemplate<T> simpleTemplate(Class<? extends T> cl, Template template, Object... args) {
        return simpleTemplate(cl, template, ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T> SimpleTemplate<T> simpleTemplate(Class<? extends T> cl, Template template, ImmutableList<?> args) {
        return new SimpleTemplate<T>(cl, template, args);
    }

    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T> DslTemplate<T> dslTemplate(Class<? extends T> cl, String template, Object... args) {
        return dslTemplate(cl, createTemplate(template), ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T> DslTemplate<T> dslTemplate(Class<? extends T> cl, String template, ImmutableList<?> args) {
        return dslTemplate(cl, createTemplate(template), args);
    }

    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T> DslTemplate<T> dslTemplate(Class<? extends T> cl, Template template, Object... args) {
        return dslTemplate(cl, template, ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T> DslTemplate<T> dslTemplate(Class<? extends T> cl, Template template, ImmutableList<?> args) {
        return new DslTemplate<T>(cl, template, args);
    }


    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T extends Comparable<?>> ComparableTemplate<T> comparableTemplate(Class<? extends T> cl,
            String template, Object... args) {
        return comparableTemplate(cl, createTemplate(template), ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T extends Comparable<?>> ComparableTemplate<T> comparableTemplate(Class<? extends T> cl,
                                                                                     String template, ImmutableList<?> args) {
        return comparableTemplate(cl, createTemplate(template), args);
    }

    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T extends Comparable<?>> ComparableTemplate<T> comparableTemplate(Class<? extends T> cl,
                                                                                     Template template, Object... args) {
        return comparableTemplate(cl, template, ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T extends Comparable<?>> ComparableTemplate<T> comparableTemplate(Class<? extends T> cl,
                                                                                     Template template, ImmutableList<?> args) {
        return new ComparableTemplate<T>(cl, template, args);
    }

    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T extends Comparable<?>> DateTemplate<T> dateTemplate(Class<? extends T> cl,
                                                                         String template, Object... args) {
        return dateTemplate(cl, createTemplate(template), ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T extends Comparable<?>> DateTemplate<T> dateTemplate(Class<? extends T> cl,
                                                                         String template, ImmutableList<?> args) {
        return dateTemplate(cl, createTemplate(template), args);
    }

    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T extends Comparable<?>> DateTemplate<T> dateTemplate(Class<? extends T> cl,
                                                                         Template template, Object... args) {
        return dateTemplate(cl, template, ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T extends Comparable<?>> DateTemplate<T> dateTemplate(Class<? extends T> cl,
                                                                         Template template, ImmutableList<?> args) {
        return new DateTemplate<T>(cl, template, args);
    }


    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T extends Comparable<?>> DateTimeTemplate<T> dateTimeTemplate(Class<? extends T> cl,
                                                                                 String template, Object... args) {
        return dateTimeTemplate(cl, createTemplate(template), ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T extends Comparable<?>> DateTimeTemplate<T> dateTimeTemplate(Class<? extends T> cl,
                                                                                 String template, ImmutableList<?> args) {
        return dateTimeTemplate(cl, createTemplate(template), args);
    }

    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T extends Comparable<?>> DateTimeTemplate<T> dateTimeTemplate(Class<? extends T> cl,
                                                                                   Template template, Object... args) {
        return dateTimeTemplate(cl, template, ImmutableList.copyOf(args));
    }


    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T extends Comparable<?>> DateTimeTemplate<T> dateTimeTemplate(Class<? extends T> cl,
                                                                                 Template template, ImmutableList<?> args) {
        return new DateTimeTemplate<T>(cl, template, args);
    }

    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T extends Comparable<?>> TimeTemplate<T> timeTemplate(Class<? extends T> cl,
                                                                           String template, Object... args) {
        return timeTemplate(cl, createTemplate(template), ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T extends Comparable<?>> TimeTemplate<T> timeTemplate(Class<? extends T> cl,
                                                                         String template, ImmutableList<?> args) {
        return timeTemplate(cl, createTemplate(template), args);
    }

    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T extends Comparable<?>> TimeTemplate<T> timeTemplate(Class<? extends T> cl,
                                                                         Template template, Object... args) {
        return timeTemplate(cl, template, ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T extends Comparable<?>> TimeTemplate<T> timeTemplate(Class<? extends T> cl,
                                                                         Template template, ImmutableList<?> args) {
        return new TimeTemplate<T>(cl, template, args);
    }

    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T extends Enum<T>> EnumTemplate<T> enumTemplate(Class<? extends T> cl,
                                                                     String template, Object... args) {
        return enumTemplate(cl, createTemplate(template), ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T extends Enum<T>> EnumTemplate<T> enumTemplate(Class<? extends T> cl,
                                                                     String template, ImmutableList<?> args) {
        return enumTemplate(cl, createTemplate(template), args);
    }

    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T extends Enum<T>> EnumTemplate<T> enumTemplate(Class<? extends T> cl,
                                                                     Template template, Object... args) {
        return enumTemplate(cl, template, ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T extends Enum<T>> EnumTemplate<T> enumTemplate(Class<? extends T> cl,
                                                                   Template template, ImmutableList<?> args) {
        return new EnumTemplate<T>(cl, template, args);
    }

    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T extends Number & Comparable<?>> NumberTemplate<T> numberTemplate(Class<? extends T> cl,
            String template, Object... args) {
        return numberTemplate(cl, createTemplate(template), ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T extends Number & Comparable<?>> NumberTemplate<T> numberTemplate(Class<? extends T> cl,
                                                                                      String template, ImmutableList<?> args) {
        return numberTemplate(cl, createTemplate(template), args);
    }

    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T extends Number & Comparable<?>> NumberTemplate<T> numberTemplate(Class<? extends T> cl,
                                                                                        Template template, Object... args) {
        return numberTemplate(cl, template, ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl
     * @param template
     * @param args
     * @return
     */
    public static <T extends Number & Comparable<?>> NumberTemplate<T> numberTemplate(Class<? extends T> cl,
                                                                                      Template template, ImmutableList<?> args) {
        return new NumberTemplate<T>(cl, template, args);
    }

    /**
     * Create a new Template expression
     *
     * @param template
     * @param args
     * @return
     */
    public static StringTemplate stringTemplate(String template, Object... args) {
        return stringTemplate(createTemplate(template), ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param template
     * @param args
     * @return
     */
    public static StringTemplate stringTemplate(String template, ImmutableList<?> args) {
        return stringTemplate(createTemplate(template), args);
    }


    /**
     * Create a new Template expression
     *
     * @param template
     * @param args
     * @return
     */
    public static StringTemplate stringTemplate(Template template, Object... args) {
        return stringTemplate(template, ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param template
     * @param args
     * @return
     */
    public static StringTemplate stringTemplate(Template template, ImmutableList<?> args) {
        return new StringTemplate(template, ImmutableList.copyOf(args));
    }


    /**
     * Create a new Template expression
     *
     * @param template
     * @param args
     * @return
     */
    public static BooleanTemplate booleanTemplate(String template, Object... args) {
        return booleanTemplate(createTemplate(template), ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param template
     * @param args
     * @return
     */
    public static BooleanTemplate booleanTemplate(String template, ImmutableList<?> args) {
        return booleanTemplate(createTemplate(template), args);
    }

    /**
     * Create a new Template expression
     *
     * @param template
     * @param args
     * @return
     */
    public static BooleanTemplate booleanTemplate(Template template, Object... args) {
        return booleanTemplate(template, ImmutableList.copyOf(args));
    }

    /**
     * Create a new Template expression
     *
     * @param template
     * @param args
     * @return
     */
    public static BooleanTemplate booleanTemplate(Template template, ImmutableList<?> args) {
        return new BooleanTemplate(template, ImmutableList.copyOf(args));
    }

    /**
     * Create a new SubQuery expression
     *
     * @param type
     * @param metadata
     * @return
     */
    public static <T> SimpleSubQuery<T> subQuery(Class<T> type, QueryMetadata metadata) {
        return new SimpleSubQuery<T>(type, metadata);
    }

    /**
     * Create a new Predicate operation
     *
     * @param operation
     * @param args
     * @return
     */
    public static BooleanOperation predicate(Operator operation, Expression<?>... args) {
        return new BooleanOperation(operation, args);
    }

    /**
     * Create a new Operation expression
     *
     * @param type
     * @param operator
     * @param args
     * @return
     */
    public static <T> SimpleOperation<T> operation(Class<? extends T> type, Operator operator,
            Expression<?>... args) {
        return simpleOperation(type, operator, args);

    }

    /**
     * Create a new Operation expression
     *
     * @param type
     * @param operator
     * @param args
     * @return
     */
    public static <T> SimpleOperation<T> simpleOperation(Class<? extends T> type, Operator operator,
                                                         Expression<?>... args) {
        return new SimpleOperation<T>(type, operator, args);
    }

    /**
     * Create a new Operation expression
     *
     * @param type
     * @param operator
     * @param args
     * @return
     */
    public static <T> DslOperation<T> dslOperation(Class<? extends T> type, Operator operator,
            Expression<?>... args) {
        return new DslOperation<T>(type, operator, args);
    }

    /**
     * Create a new Boolean operation
     *
     * @param operation
     * @param args
     * @return
     */
    public static BooleanOperation booleanOperation(Operator operation, Expression<?>... args) {
        return predicate(operation, args);
    }

    /**
     * Create a new Operation expression
     *
     * @param type
     * @param operator
     * @param args
     * @return
     */
    public static <T extends Comparable<?>> ComparableOperation<T> comparableOperation(Class<? extends T> type,
            Operator operator, Expression<?>... args) {
        return new ComparableOperation<T>(type, operator, args);
    }

    /**
     * Create a new Operation expression
     *
     * @param type
     * @param operator
     * @param args
     * @return
     */
    public static <T extends Comparable<?>> DateOperation<T> dateOperation(Class<? extends T> type,
            Operator operator, Expression<?>... args) {
        return new DateOperation<T>(type, operator, args);
    }

    /**
     * Create a new Operation expression
     *
     * @param type
     * @param operator
     * @param args
     * @return
     */
    public static <T extends Comparable<?>> DateTimeOperation<T> dateTimeOperation(Class<? extends T> type,
            Operator operator, Expression<?>... args) {
        return new DateTimeOperation<T>(type, operator, args);
    }

    /**
     * Create a new Operation expression
     *
     * @param type
     * @param operator
     * @param args
     * @return
     */
    public static <T extends Comparable<?>> TimeOperation<T> timeOperation(Class<? extends T> type,
            Operator operator, Expression<?>... args) {
        return new TimeOperation<T>(type, operator, args);
    }

    /**
     * Create a new Operation expression
     *
     * @param type
     * @param operator
     * @param args
     * @return
     */
    public static <T extends Number & Comparable<?>> NumberOperation<T> numberOperation(Class<? extends T> type,
            Operator operator, Expression<?>... args) {
        return new NumberOperation<T>(type, operator, args);
    }

    /**
     * Create a new Operation expression
     *
     * @param operator
     * @param args
     * @return
     */
    public static StringOperation stringOperation(Operator operator, Expression<?>... args) {
        return new StringOperation(operator, args);
    }

    /**
     * Create a new Path expression
     *
     * @param type
     * @param variable
     * @return
     */
    public static <T> SimplePath<T> path(Class<? extends T> type, String variable) {
        return simplePath(type, variable);
    }

    /**
     * Create a new Path expression
     *
     * @param type
     * @param parent
     * @param property
     * @return
     */
    public static <T> SimplePath<T> path(Class<? extends T> type, Path<?> parent, String property) {
        return simplePath(type, parent, property);
    }

    /**
     * Create a new Path expression
     *
     * @param type
     * @param metadata
     * @param <T>
     * @return
     */
    public static <T> SimplePath<T> path(Class<? extends T> type, PathMetadata metadata) {
        return simplePath(type, metadata);
    }

    /**
     * Create a new Path expression
     *
     * @param type
     * @param variable
     * @return
     */
    public static <T> SimplePath<T> simplePath(Class<? extends T> type, String variable) {
        return new SimplePath<T>(type, PathMetadataFactory.forVariable(variable));
    }

    /**
     * Create a new Path expression
     *
     * @param type
     * @param parent
     * @param property
     * @return
     */
    public static <T> SimplePath<T> simplePath(Class<? extends T> type, Path<?> parent, String property) {
        return new SimplePath<T>(type, PathMetadataFactory.forProperty(parent, property));
    }

    /**
     * Create a new Path expression
     *
     * @param type
     * @param metadata
     * @param <T>
     * @return
     */
    public static <T> SimplePath<T> simplePath(Class<? extends T> type, PathMetadata metadata) {
        return new SimplePath<T>(type, metadata);
    }

    /**
     * Create a new Path expression
     *
     * @param type
     * @param variable
     * @return
     */
    public static <T> DslPath<T> dslPath(Class<? extends T> type, String variable) {
        return new DslPath<T>(type, PathMetadataFactory.forVariable(variable));
    }

    /**
     * Create a new Path expression
     *
     * @param type
     * @param parent
     * @param property
     * @return
     */
    public static <T> DslPath<T> dslPath(Class<? extends T> type, Path<?> parent, String property) {
        return new DslPath<T>(type, PathMetadataFactory.forProperty(parent, property));
    }

    /**
     * Create a new Path expression
     *
     * @param type
     * @param metadata
     * @param <T>
     * @return
     */
    public static <T> DslPath<T> dslPath(Class<? extends T> type, PathMetadata metadata) {
        return new DslPath<T>(type, metadata);
    }

    /**
     * Create a new Path expression
     *
     * @param type
     * @param variable
     * @return
     */
    public static <T extends Comparable<?>> ComparablePath<T> comparablePath(Class<? extends T> type,
            String variable) {
        return new ComparablePath<T>(type, PathMetadataFactory.forVariable(variable));
    }

    /**
     * Create a new Path expression
     *
     * @param type
     * @param parent
     * @param property
     * @return
     */
    public static <T extends Comparable<?>> ComparablePath<T> comparablePath(Class<? extends T> type,
            Path<?> parent, String property) {
        return new ComparablePath<T>(type, PathMetadataFactory.forProperty(parent, property));
    }

    /**
     * Create a new Path expression
     *
     * @param type
     * @param metadata
     * @param <T>
     * @return
     */
    public static <T extends Comparable<?>> ComparablePath<T> comparablePath(Class<? extends T> type,
                                                                             PathMetadata metadata) {
        return new ComparablePath<T>(type, metadata);
    }

    /**
     * Create a new Path expression
     *
     * @param type
     * @param variable
     * @return
     */
    public static <T extends Comparable<?>> ComparableEntityPath<T> comparableEntityPath(Class<? extends T> type,
                                                                             String variable) {
        return new ComparableEntityPath<T>(type, PathMetadataFactory.forVariable(variable));
    }

    /**
     * Create a new Path expression
     *
     * @param type
     * @param parent
     * @param property
     * @return
     */
    public static <T extends Comparable<?>> ComparableEntityPath<T> comparableEntityPath(Class<? extends T> type,
                                                                             Path<?> parent, String property) {
        return new ComparableEntityPath<T>(type, PathMetadataFactory.forProperty(parent, property));
    }

    /**
     * Create a new Path expression
     *
     * @param type
     * @param metadata
     * @param <T>
     * @return
     */
    public static <T extends Comparable<?>> ComparableEntityPath<T> comparableEntityPath(Class<? extends T> type,
                                                                             PathMetadata metadata) {
        return new ComparableEntityPath<T>(type, metadata);
    }

    /**
     * Create a new Path expression
     *
     * @param type
     * @param variable
     * @return
     */
    public static <T extends Comparable<?>> DatePath<T> datePath(Class<? extends T> type, String variable) {
        return new DatePath<T>(type, PathMetadataFactory.forVariable(variable));
    }

    /**
     * Create a new Path expression
     *
     * @param type
     * @param parent
     * @param property
     * @return
     */
    public static <T extends Comparable<?>> DatePath<T> datePath(Class<? extends T> type, Path<?> parent,
            String property) {
        return new DatePath<T>(type, PathMetadataFactory.forProperty(parent, property));
    }

    /**
     * Create a new Path expression
     *
     * @param type
     * @param metadata
     * @param <T>
     * @return
     */
    public static <T extends Comparable<?>> DatePath<T> datePath(Class<? extends T> type, PathMetadata metadata) {
        return new DatePath<T>(type, metadata);
    }

    /**
     * Create a new Path expression
     *
     * @param type
     * @param variable
     * @return
     */
    public static <T extends Comparable<?>> DateTimePath<T> dateTimePath(Class<? extends T> type, String variable) {
        return new DateTimePath<T>(type, PathMetadataFactory.forVariable(variable));
    }

    /**
     * Create a new Path expression
     *
     * @param type
     * @param parent
     * @param property
     * @return
     */
    public static <T extends Comparable<?>> DateTimePath<T> dateTimePath(Class<? extends T> type, Path<?> parent,
            String property) {
        return new DateTimePath<T>(type, PathMetadataFactory.forProperty(parent, property));
    }

    /**
     * Create a new Path expression
     *
     * @param type
     * @param metadata
     * @param <T>
     * @return
     */
    public static <T extends Comparable<?>> DateTimePath<T> dateTimePath(Class<? extends T> type, PathMetadata metadata) {
        return new DateTimePath<T>(type, metadata);
    }

    /**
     * Create a new Path expression
     *
     * @param type
     * @param variable
     * @return
     */
    public static <T extends Comparable<?>> TimePath<T> timePath(Class<? extends T> type, String variable) {
        return new TimePath<T>(type, PathMetadataFactory.forVariable(variable));
    }

    /**
     * Create a new Path expression
     *
     * @param type
     * @param parent
     * @param property
     * @return
     */
    public static <T extends Comparable<?>> TimePath<T> timePath(Class<? extends T> type, Path<?> parent,
            String property) {
        return new TimePath<T>(type, PathMetadataFactory.forProperty(parent, property));
    }

    /**
     * Create a new Path expression
     *
     * @param type
     * @param metadata
     * @param <T>
     * @return
     */
    public static <T extends Comparable<?>> TimePath<T> timePath(Class<? extends T> type, PathMetadata metadata) {
        return new TimePath<T>(type, metadata);
    }

    /**
     * Create a new Path expression
     *
     * @param type
     * @param variable
     * @return
     */
    public static <T extends Number & Comparable<?>> NumberPath<T> numberPath(Class<? extends T> type,
            String variable) {
        return new NumberPath<T>(type, PathMetadataFactory.forVariable(variable));
    }

    /**
     * Create a new Path expression
     *
     * @param type
     * @param parent
     * @param property
     * @return
     */
    public static <T extends Number & Comparable<?>> NumberPath<T> numberPath(Class<? extends T> type,
            Path<?> parent, String property) {
        return new NumberPath<T>(type, PathMetadataFactory.forProperty(parent, property));
    }

    /**
     * Create new Path expression
     *
     * @param type
     * @param metadata
     * @param <T>
     * @return
     */
    public static <T extends Number & Comparable<?>> NumberPath<T> numberPath(Class<? extends T> type, PathMetadata metadata) {
        return new NumberPath<T>(type, metadata);
    }

    /**
     * Create a new Path expression
     *
     * @param variable
     * @return
     */
    public static StringPath stringPath(String variable) {
        return new StringPath(PathMetadataFactory.forVariable(variable));
    }

    /**
     * Create a new Path expression
     *
     * @param parent
     * @param property
     * @return
     */
    public static StringPath stringPath(Path<?> parent, String property) {
        return new StringPath(PathMetadataFactory.forProperty(parent, property));
    }

    /**
     * Create a new Path expression
     *
     * @param metadata
     * @return
     */
    public static StringPath stringPath(PathMetadata metadata) {
        return new StringPath(metadata);
    }

    /**
     * Create a new Path expression
     *
     * @param variable
     * @return
     */
    public static BooleanPath booleanPath(String variable) {
        return new BooleanPath(PathMetadataFactory.forVariable(variable));
    }

    /**
     * Create a new Path expression
     *
     * @param parent
     * @param property
     * @return
     */
    public static BooleanPath booleanPath(Path<?> parent, String property) {
        return new BooleanPath(PathMetadataFactory.forProperty(parent, property));
    }

    /**
     * Create a new Path expression
     *
     * @param metadata
     * @return
     */
    public static BooleanPath booleanPath(PathMetadata metadata) {
        return new BooleanPath(metadata);
    }

    /**
     * Get a builder for a case expression
     *
     * @return
     */
    public static CaseBuilder cases() {
        return new CaseBuilder();
    }

    /**
     * Combine the given expressions into a list expression
     *
     * @param exprs
     * @return
     */
    public static SimpleExpression<Tuple> list(SimpleExpression<?>... exprs) {
        return list(Tuple.class, exprs);
    }

    /**
     * Combine the given expressions into a list expression
     *
     * @param clazz
     * @param exprs
     * @return
     */
    public static <T> SimpleExpression<T> list(Class<T> clazz, SimpleExpression<?>... exprs) {
        SimpleExpression<T> rv = (SimpleExpression<T>)exprs[0];
        for (int i = 1; i < exprs.length; i++) {
            rv = new SimpleOperation<T>(clazz, Ops.LIST, rv, exprs[i]);
        }
        return rv;
    }

    /**
     * Get a null expression for the specified type
     *
     * @return
     */
    @SuppressWarnings("unchecked")//does not produce non-null instances of T
    public static <T> NullExpression<T> nullExpression() {
        return (NullExpression<T>) NullExpression.DEFAULT;
    }

    /**
     * Get a null expression for the specified type
     *
     * @param type
     * @param <T>
     * @return
     */
    public static <T> NullExpression<T> nullExpression(Class<T> type) {
        return nullExpression();
    }

    /**
     * Get a null expression for the specified path
     *
     * @param path
     * @param <T>
     * @return
     */
    public static <T> NullExpression<T> nullExpression(Path<T> path) {
        return nullExpression();
    }

    /**
     * Create a new Enum operation expression
     *
     * @param type
     * @param operator
     * @param args
     * @param <T>
     * @return
     */
    public static <T extends Enum<T>> EnumOperation<T> enumOperation(Class<? extends T> type, Operator operator,
                                                                      Expression<?>... args) {
        return new EnumOperation<T>(type, operator, args);
    }

    /**
     * Create a new Path expression
     *
     * @param type
     * @param variable
     * @return
     */
    public static <T extends Enum<T>> EnumPath<T> enumPath(Class<? extends T> type, String variable) {
        return new EnumPath<T>(type, PathMetadataFactory.forVariable(variable));
    }

    /**
     * Create a new Path expression
     *
     * @param type
     * @param parent
     * @param property
     * @return
     */
    public static <T extends Enum<T>> EnumPath<T> enumPath(Class<? extends T> type, Path<?> parent, String property) {
        return new EnumPath<T>(type, PathMetadataFactory.forProperty(parent, property));
    }

    /**
     * Create a new Path expression
     *
     * @param type
     * @param metadata
     * @param <T>
     * @return
     */
    public static <T extends Enum<T>> EnumPath<T> enumPath(Class<? extends T> type, PathMetadata metadata) {
        return new EnumPath<T>(type, metadata);
    }

    /**
     * Create a new Collection operation expression
     *
     * @param elementType
     * @param operator
     * @param args
     * @param <T>
     * @return
     */
    public static <T> CollectionExpression<Collection<T>, T> collectionOperation(Class<T> elementType, Operator operator,
                                                                  Expression<?>... args) {
        return new CollectionOperation<T>(elementType, operator, args);
    }

    /**
     * Create a new Path expression
     *
     * @param type
     * @param queryType
     * @param metadata
     * @param <E>
     * @param <Q>
     * @return
     */
    public static <E, Q extends SimpleExpression<? super E>> CollectionPath<E, Q> collectionPath(Class<E> type, Class<Q> queryType, PathMetadata metadata) {
        return new CollectionPath<E, Q>(type, queryType, metadata);
    }

    /**
     * Create a new Path expression
     *
     * @param type
     * @param queryType
     * @param metadata
     * @param <E>
     * @param <Q>
     * @return
     */
    public static <E, Q extends SimpleExpression<? super E>> ListPath<E, Q> listPath(Class<E> type, Class<Q> queryType, PathMetadata metadata) {
        return new ListPath<E, Q>(type, queryType, metadata);
    }

    /**
     * Create a new Path expression
     *
     * @param type
     * @param queryType
     * @param metadata
     * @param <E>
     * @param <Q>
     * @return
     */
    public static <E, Q extends SimpleExpression<? super E>> SetPath<E, Q> setPath(Class<E> type, Class<Q> queryType, PathMetadata metadata) {
        return new SetPath<E, Q>(type, queryType, metadata);
    }

    /**
     * Create a new Path expression
     *
     * @param keyType
     * @param valueType
     * @param queryType
     * @param metadata
     * @param <K>
     * @param <V>
     * @param <E>
     * @return
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
     * @param arrayType
     * @param metadata
     * @param <A>
     * @param <E>
     * @return
     */
    public static <A, E> ArrayPath<A, E> arrayPath(Class<A> arrayType, PathMetadata metadata) {
        return new ArrayPath<A, E>(arrayType, metadata);
    }

    private static Template createTemplate(String template) {
        return TemplateFactory.DEFAULT.create(template);
    }

}
