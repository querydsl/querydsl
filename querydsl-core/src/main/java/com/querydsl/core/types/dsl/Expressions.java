//CHECKSTYLERULE:OFF: FileLength
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.CollectionExpression;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.NullExpression;
import com.querydsl.core.types.OperationImpl;
import com.querydsl.core.types.Operator;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathImpl;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.PredicateOperation;
import com.querydsl.core.types.PredicateTemplate;
import com.querydsl.core.types.Template;
import com.querydsl.core.types.TemplateExpressionImpl;
import com.querydsl.core.types.TemplateFactory;
import com.querydsl.core.types.Visitor;

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
            return operation(alias.getType(), Ops.ALIAS, source, alias);
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
    public static <T> SimpleTemplate<T> template(Class<? extends T> cl, String template, List<?> args) {
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
    public static <T> SimpleTemplate<T> template(Class<? extends T> cl, Template template, List<?> args) {
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
        return simpleTemplate(cl, createTemplate(template), Arrays.asList(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T> SimpleTemplate<T> simpleTemplate(Class<? extends T> cl, String template, List<?> args) {
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
        return simpleTemplate(cl, template, Arrays.asList(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T> SimpleTemplate<T> simpleTemplate(Class<? extends T> cl, Template template, List<?> args) {
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
        return dslTemplate(cl, createTemplate(template), Arrays.asList(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T> DslTemplate<T> dslTemplate(Class<? extends T> cl, String template, List<?> args) {
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
        return dslTemplate(cl, template, Arrays.asList(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T> DslTemplate<T> dslTemplate(Class<? extends T> cl, Template template, List<?> args) {
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
        return comparableTemplate(cl, createTemplate(template), Arrays.asList(args));
    }


    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Comparable<?>> ComparableTemplate<T> comparableTemplate(Class<? extends T> cl, String template, List<?> args) {
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
        return comparableTemplate(cl, template, Arrays.asList(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Comparable<?>> ComparableTemplate<T> comparableTemplate(Class<? extends T> cl, Template template, List<?> args) {
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
        return dateTemplate(cl, createTemplate(template), Arrays.asList(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Comparable<?>> DateTemplate<T> dateTemplate(Class<? extends T> cl, String template, List<?> args) {
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
        return dateTemplate(cl, template, Arrays.asList(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Comparable<?>> DateTemplate<T> dateTemplate(Class<? extends T> cl, Template template, List<?> args) {
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
        return dateTimeTemplate(cl, createTemplate(template), Arrays.asList(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Comparable<?>> DateTimeTemplate<T> dateTimeTemplate(Class<? extends T> cl, String template, List<?> args) {
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
        return dateTimeTemplate(cl, template, Arrays.asList(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Comparable<?>> DateTimeTemplate<T> dateTimeTemplate(Class<? extends T> cl, Template template, List<?> args) {
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
        return timeTemplate(cl, createTemplate(template), Arrays.asList(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Comparable<?>> TimeTemplate<T> timeTemplate(Class<? extends T> cl, String template, List<?> args) {
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
        return timeTemplate(cl, template, Arrays.asList(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Comparable<?>> TimeTemplate<T> timeTemplate(Class<? extends T> cl, Template template, List<?> args) {
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
        return enumTemplate(cl, createTemplate(template), Arrays.asList(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Enum<T>> EnumTemplate<T> enumTemplate(Class<? extends T> cl, String template, List<?> args) {
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
        return enumTemplate(cl, template, Arrays.asList(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Enum<T>> EnumTemplate<T> enumTemplate(Class<? extends T> cl, Template template, List<?> args) {
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
        return numberTemplate(cl, createTemplate(template), Arrays.asList(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Number & Comparable<?>> NumberTemplate<T> numberTemplate(Class<? extends T> cl, String template, List<?> args) {
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
        return numberTemplate(cl, template, Arrays.asList(args));
    }

    /**
     * Create a new Template expression
     *
     * @param cl type of expression
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static <T extends Number & Comparable<?>> NumberTemplate<T> numberTemplate(Class<? extends T> cl, Template template, List<?> args) {
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
        return stringTemplate(createTemplate(template), Arrays.asList(args));
    }

    /**
     * Create a new Template expression
     *
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static StringTemplate stringTemplate(String template, List<?> args) {
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
        return stringTemplate(template, Arrays.asList(args));
    }

    /**
     * Create a new Template expression
     *
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static StringTemplate stringTemplate(Template template, List<?> args) {
        return new StringTemplate(template, args);
    }

    /**
     * Create a new Template expression
     *
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static BooleanTemplate booleanTemplate(String template, Object... args) {
        return booleanTemplate(createTemplate(template), Arrays.asList(args));
    }

    /**
     * Create a new Template expression
     *
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static BooleanTemplate booleanTemplate(String template, List<?> args) {
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
        return booleanTemplate(template, Arrays.asList(args));
    }

    /**
     * Create a new Template expression
     *
     * @param template template
     * @param args template parameters
     * @return template expression
     */
    public static BooleanTemplate booleanTemplate(Template template, List<?> args) {
        return new BooleanTemplate(template, args);
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
     * @return new path instance
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
    @SuppressWarnings("unchecked")
    public static <T> SimpleExpression<T> list(Class<T> clazz, SimpleExpression<?>... exprs) {
        SimpleExpression<T> rv = (SimpleExpression<T>) exprs[0];
        for (int i = 1; i < exprs.length; i++) {
            rv = new SimpleOperation<T>(clazz, Ops.LIST, rv, exprs[i]);
        }
        return rv;
    }


    /**
     * Combine the given expressions into a list expression
     *
     * @param clazz type of list expression
     * @param exprs list elements
     * @return list expression
     */
    @SuppressWarnings("unchecked")
    public static <T> Expression<T> list(Class<T> clazz, Expression<?>... exprs) {
        Expression<T> rv = (Expression<T>) exprs[0];
        for (int i = 1; i < exprs.length; i++) {
            rv = new SimpleOperation<T>(clazz, Ops.LIST, rv, exprs[i]);
        }
        return rv;
    }

    /**
     * Combine the given expressions into a set expression
     *
     * @param clazz type of list expression
     * @param exprs list elements
     * @return list expression
     */
    @SuppressWarnings("unchecked")
    public static <T> SimpleExpression<T> set(Class<T> clazz, SimpleExpression<?>... exprs) {
        SimpleExpression<T> rv = (SimpleExpression<T>) exprs[0];
        for (int i = 1; i < exprs.length; i++) {
            rv = new SimpleOperation<T>(clazz, Ops.SET, rv, exprs[i]);
        }
        return rv;
    }

    /**
     * Combine the given expressions into a set expression
     *
     * @param clazz type of list expression
     * @param exprs list elements
     * @return list expression
     */
    @SuppressWarnings("unchecked")
    public static <T> Expression<T> set(Class<T> clazz, Expression<?>... exprs) {
        Expression<T> rv = (Expression<T>) exprs[0];
        for (int i = 1; i < exprs.length; i++) {
            rv = new SimpleOperation<T>(clazz, Ops.SET, rv, exprs[i]);
        }
        return rv;
    }

    /**
     * Combine the given expressions into a list expression
     *
     * @param exprs list elements
     * @return list expression
     */
    public static Expression<Tuple> list(Expression<?>... exprs) {
        return list(Tuple.class, exprs);
    }

    /**
     * Combine the given expressions into a set expression
     *
     * @param exprs list elements
     * @return list expression
     */
    public static Expression<Tuple> set(Expression<?>... exprs) {
        return set(Tuple.class, exprs);
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
        return new NullExpression<T>(type);
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
     * @param variable variable name
     * @param <A> array type
     * @param <E> element type
     * @return path expression
     */
    public static <A, E> ArrayPath<A, E> arrayPath(Class<A> arrayType, String variable) {
        return new ArrayPath<A, E>(arrayType, variable);
    }

    /**
     * Create a new Path expression
     *
     * @param arrayType array type
     * @param parent path metadata
     * @param property property name
     * @param <A> array type
     * @param <E> element type
     * @return path expression
     */
    public static <A, E> ArrayPath<A, E> arrayPath(Class<A> arrayType, Path<?> parent, String property) {
        return new ArrayPath<A, E>(arrayType, parent, property);
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

    /**
     * Create a new BooleanExpression
     *
     * @param expr Expression of type Boolean
     * @return new BooleanExpression
     */
    public static BooleanExpression asBoolean(Expression<Boolean> expr) {
        Expression<Boolean> underlyingMixin = ExpressionUtils.extract(expr);
        if (underlyingMixin instanceof PathImpl) {
            return new BooleanPath((PathImpl<Boolean>) underlyingMixin);
        } else if (underlyingMixin instanceof PredicateOperation) {
            return new BooleanOperation((PredicateOperation) underlyingMixin);
        } else if (underlyingMixin instanceof PredicateTemplate) {
            return new BooleanTemplate((PredicateTemplate) underlyingMixin);
        } else {
            return new BooleanExpression(underlyingMixin) {

                private static final long serialVersionUID = -8712299418891960222L;

                @Override
                public <R, C> R accept(Visitor<R, C> v, C context) {
                    return this.mixin.accept(v, context);
                }

                @Override
                public boolean equals(Object o) {
                    if (o == this) {
                        return true;
                    } else if (o instanceof BooleanExpression) {
                        BooleanExpression other = (BooleanExpression) o;
                        return (other.mixin.equals(this.mixin));
                    } else {
                        return false;
                    }
                }

            };
        }
    }

    /**
     * Create a new BooleanExpression
     *
     * @param value boolean
     * @return new BooleanExpression
     */
    public static BooleanExpression asBoolean(boolean value) {
        return asBoolean(constant(value));
    }

    /**
     * Create a new ComparableExpression
     *
     * @param expr Expression of type Comparable
     * @return new ComparableExpression
     */
    public static <T extends Comparable<?>> ComparableExpression<T> asComparable(Expression<T> expr) {
        Expression<T> underlyingMixin = ExpressionUtils.extract(expr);
        if (underlyingMixin instanceof PathImpl) {
            return new ComparablePath<T>((PathImpl<T>) underlyingMixin);
        } else if (underlyingMixin instanceof OperationImpl) {
            return new ComparableOperation<T>((OperationImpl<T>) underlyingMixin);
        } else if (underlyingMixin instanceof TemplateExpressionImpl) {
            return new ComparableTemplate<T>((TemplateExpressionImpl<T>) underlyingMixin);
        } else {
            return new ComparableExpression<T>(underlyingMixin) {

                private static final long serialVersionUID = 389920618099394430L;

                @Override
                public <R, C> R accept(Visitor<R, C> v, C context) {
                    return this.mixin.accept(v, context);
                }

                @Override
                public boolean equals(Object o) {
                    if (o == this) {
                        return true;
                    } else if (o instanceof ComparableExpression) {
                        ComparableExpression other = (ComparableExpression) o;
                        return (other.mixin.equals(this.mixin));
                    } else {
                        return false;
                    }
                }

            };
        }

    }

    /**
     * Create a new ComparableExpression
     *
     * @param value Comparable
     * @return new ComparableExpression
     */
    public static <T extends Comparable<?>> ComparableExpression<T> asComparable(T value) {
        return asComparable(constant(value));
    }

    /**
     * Create a new DateExpression
     *
     * @param expr the date Expression
     * @return new DateExpression
     */
    public static <T extends Comparable<?>> DateExpression<T> asDate(Expression<T> expr) {
        Expression<T> underlyingMixin = ExpressionUtils.extract(expr);
        if (underlyingMixin instanceof PathImpl) {
            return new DatePath<T>((PathImpl<T>) underlyingMixin);
        } else if (underlyingMixin instanceof OperationImpl) {
            return new DateOperation<T>((OperationImpl<T>) underlyingMixin);
        } else if (underlyingMixin instanceof TemplateExpressionImpl) {
            return new DateTemplate<T>((TemplateExpressionImpl<T>) underlyingMixin);
        } else {
            return new DateExpression<T>(underlyingMixin) {

                private static final long serialVersionUID = 389920618099394430L;

                @Override
                public <R, C> R accept(Visitor<R, C> v, C context) {
                    return this.mixin.accept(v, context);
                }

            };
        }

    }

    /**
     * Create a new DateExpression
     *
     * @param value the date
     * @return new DateExpression
     */
    public static <T extends Comparable<?>> DateExpression<T> asDate(T value) {
        return asDate(constant(value));
    }

    /**
     * Create a new DateTimeExpression
     *
     * @param expr the date time Expression
     * @return new DateTimeExpression
     */
    public static <T extends Comparable<?>> DateTimeExpression<T> asDateTime(Expression<T> expr) {
        Expression<T> underlyingMixin = ExpressionUtils.extract(expr);
        if (underlyingMixin instanceof PathImpl) {
            return new DateTimePath<T>((PathImpl<T>) underlyingMixin);
        } else if (underlyingMixin instanceof OperationImpl) {
            return new DateTimeOperation<T>((OperationImpl<T>) underlyingMixin);
        } else if (underlyingMixin instanceof TemplateExpressionImpl) {
            return new DateTimeTemplate<T>((TemplateExpressionImpl<T>) underlyingMixin);
        } else {
            return new DateTimeExpression<T>(underlyingMixin) {

                private static final long serialVersionUID = 8007203530480765244L;

                @Override
                public <R, C> R accept(Visitor<R, C> v, C context) {
                    return this.mixin.accept(v, context);
                }

            };
        }

    }

    /**
     * Create a new DateTimeExpression
     *
     * @param value the date time
     * @return new DateTimeExpression
     */
    public static <T extends Comparable<?>> DateTimeExpression<T> asDateTime(T value) {
        return asDateTime(constant(value));
    }

    /**
     * Create a new TimeExpression
     *
     * @param expr the time Expression
     * @return new TimeExpression
     */
    public static <T extends Comparable<?>> TimeExpression<T> asTime(Expression<T> expr) {
        Expression<T> underlyingMixin = ExpressionUtils.extract(expr);
        if (underlyingMixin instanceof PathImpl) {
            return new TimePath<T>((PathImpl<T>) underlyingMixin);
        } else if (underlyingMixin instanceof OperationImpl) {
            return new TimeOperation<T>((OperationImpl<T>) underlyingMixin);
        } else if (underlyingMixin instanceof TemplateExpressionImpl) {
            return new TimeTemplate<T>((TemplateExpressionImpl<T>) underlyingMixin);
        } else {
            return new TimeExpression<T>(underlyingMixin) {

                private static final long serialVersionUID = -2402288239000668173L;

                @Override
                public <R, C> R accept(Visitor<R, C> v, C context) {
                    return this.mixin.accept(v, context);
                }

            };
        }
    }

    /**
     * Create a new TimeExpression
     *
     * @param value the time
     * @return new TimeExpression
     */
    public static <T extends Comparable<?>> TimeExpression<T> asTime(T value) {
        return asTime(constant(value));
    }

    /**
     * Create a new EnumExpression
     *
     * @param expr Expression of type Enum
     * @return new EnumExpression
     */
    public static <T extends Enum<T>> EnumExpression<T> asEnum(Expression<T> expr) {
        Expression<T> underlyingMixin = ExpressionUtils.extract(expr);
        if (underlyingMixin instanceof PathImpl) {
            return new EnumPath<T>((PathImpl<T>) underlyingMixin);
        } else if (underlyingMixin instanceof OperationImpl) {
            return new EnumOperation<T>((OperationImpl<T>) underlyingMixin);
        } else if (underlyingMixin instanceof TemplateExpressionImpl) {
            return new EnumTemplate<T>((TemplateExpressionImpl<T>) underlyingMixin);
        } else {
            return new EnumExpression<T>(underlyingMixin) {

                private static final long serialVersionUID = 949681836002045152L;

                @Override
                public <R, C> R accept(Visitor<R, C> v, C context) {
                    return this.mixin.accept(v, context);
                }

                @Override
                public boolean equals(Object o) {
                    if (o == this) {
                        return true;
                    } else if (o instanceof EnumExpression) {
                        EnumExpression other = (EnumExpression) o;
                        return (other.mixin.equals(this.mixin));
                    } else {
                        return false;
                    }
                }

            };
        }

    }

    /**
     * Create a new EnumExpression
     *
     * @param value enum
     * @return new EnumExpression
     */
    public static <T extends Enum<T>> EnumExpression<T> asEnum(T value) {
        return asEnum(constant(value));
    }

    /**
     * Create a new NumberExpression
     *
     * @param expr Expression of type Number
     * @return new NumberExpression
     */
    public static <T extends Number & Comparable<?>> NumberExpression<T> asNumber(Expression<T> expr) {
        Expression<T> underlyingMixin = ExpressionUtils.extract(expr);
        if (underlyingMixin instanceof PathImpl) {
            return new NumberPath<T>((PathImpl<T>) underlyingMixin);
        } else if (underlyingMixin instanceof OperationImpl) {
            return new NumberOperation<T>((OperationImpl<T>) underlyingMixin);
        } else if (underlyingMixin instanceof TemplateExpressionImpl) {
            return new NumberTemplate<T>((TemplateExpressionImpl<T>) underlyingMixin);
        } else {
            return new NumberExpression<T>(underlyingMixin) {

                private static final long serialVersionUID = -8712299418891960222L;

                @Override
                public <R, C> R accept(Visitor<R, C> v, C context) {
                    return this.mixin.accept(v, context);
                }

                @Override
                public boolean equals(Object o) {
                    if (o == this) {
                        return true;
                    } else if (o instanceof NumberExpression) {
                        NumberExpression other = (NumberExpression) o;
                        return (other.mixin.equals(this.mixin));
                    } else {
                        return false;
                    }
                }

            };
        }
    }

    /**
     * Create a new NumberExpression
     *
     * @param value Number
     * @return new NumberExpression
     */
    public static <T extends Number & Comparable<?>> NumberExpression<T> asNumber(T value) {
        return asNumber(constant(value));
    }

    /**
     * Create a new StringExpression
     *
     * @param expr Expression of type String
     * @return new StringExpression
     */
    public static StringExpression asString(Expression<String> expr) {
        Expression<String> underlyingMixin = ExpressionUtils.extract(expr);
        if (underlyingMixin instanceof PathImpl) {
            return new StringPath((PathImpl<String>) underlyingMixin);
        } else if (underlyingMixin instanceof OperationImpl) {
            return new StringOperation((OperationImpl<String>) underlyingMixin);
        } else if (underlyingMixin instanceof TemplateExpressionImpl) {
            return new StringTemplate((TemplateExpressionImpl<String>) underlyingMixin);
        } else {
            return new StringExpression(underlyingMixin) {

                private static final long serialVersionUID = 8007203530480765244L;

                @Override
                public <R, C> R accept(Visitor<R, C> v, C context) {
                    return this.mixin.accept(v, context);
                }

                @Override
                public boolean equals(Object o) {
                    if (o == this) {
                        return true;
                    } else if (o instanceof StringExpression) {
                        StringExpression other = (StringExpression) o;
                        return (other.mixin.equals(this.mixin));
                    } else {
                        return false;
                    }
                }
            };
        }
    }

    /**
     * Create a new StringExpression
     *
     * @param value String
     * @return new StringExpression
     */
    public static StringExpression asString(String value) {
        return asString(constant(value));
    }

    /**
     * Create a new SimpleExpression
     *
     * @param expr expression
     * @return new SimpleExpression
     */
    public static <T> SimpleExpression<T> asSimple(Expression<T> expr) {
        Expression<T> underlyingMixin = ExpressionUtils.extract(expr);
        if (underlyingMixin instanceof PathImpl) {
            return new SimplePath<T>((PathImpl<T>) underlyingMixin);
        } else if (underlyingMixin instanceof OperationImpl) {
            return new SimpleOperation<T>((OperationImpl<T>) underlyingMixin);
        } else if (underlyingMixin instanceof TemplateExpressionImpl) {
            return new SimpleTemplate<T>((TemplateExpressionImpl<T>) underlyingMixin);
        } else {
            return new SimpleExpression<T>(underlyingMixin) {

                private static final long serialVersionUID = -8712299418891960222L;

                @Override
                public <R, C> R accept(Visitor<R, C> v, C context) {
                    return this.mixin.accept(v, context);
                }

            };
        }
    }

    /**
     * Create a new SimpleExpression
     *
     * @param value constant
     * @return new SimpleExpression
     */
    public static <T> SimpleExpression<T> asSimple(T value) {
        return asSimple(constant(value));
    }

}
//CHECKSTYLERULE:ON: FileLength
