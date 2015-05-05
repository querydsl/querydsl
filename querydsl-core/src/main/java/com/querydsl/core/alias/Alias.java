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
package com.querydsl.core.alias;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.base.CaseFormat;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.*;

/**
 * {@code Alias} provides alias factory methods
 *
 * <p>Example:</p>
 *
 * <pre>{@code
 * Empoyee e = alias(Employee.class, "e");
 * for (String name : query.from($(e),employees)
 *     .where($(e.getDepartment().getId()).eq(1001))
 *     .list($(e.getName()))) {
 *     System.out.println(name);
 * }}</pre>
 *
 * <p>using the following static imports</p>
 *
 * <pre>{@code
 * import static com.mysema.query.alias.Alias.$;
 * import static com.mysema.query.alias.Alias.alias;
 * }</pre>
 *
 * @author tiwe
 */
@SuppressWarnings("PMD")
public final class Alias {

    private static final AliasFactory aliasFactory = new AliasFactory(new DefaultPathFactory(), new DefaultTypeSystem());

    private static final SimplePath<Object> it = Expressions.path(Object.class, "it");

    // exclude $-methods from Checkstyle checks
    //CHECKSTYLE:OFF
    /**
     * Convert the given alias to an expression
     *
     * @param <D>
     * @return expression
     */
    public static <D extends Expression<?>> D $() {
        return aliasFactory.getCurrentAndReset();
    }

    /**
     * Convert the given alias to an expression
     *
     * @param arg alias
     * @param <D>
     * @return expression
     */
    public static <D> ArrayPath<D[], D> $(D[] arg) {
        return aliasFactory.getCurrentAndReset();
    }

    /**
     * Convert the given alias to an expression
     *
     * @param arg alias
     * @return expression
     */
    public static NumberPath<BigDecimal> $(BigDecimal arg) {
        return aliasFactory.getCurrentAndReset();
    }

    /**
     * Convert the given alias to an expression
     *
     * @param arg alias
     * @return expression
     */
    public static NumberPath<BigInteger> $(BigInteger arg) {
        return aliasFactory.getCurrentAndReset();
    }

    /**
     * Convert the given alias to an expression
     *
     * @param arg alias
     * @return expression
     */
    public static BooleanPath $(Boolean arg) {
        return aliasFactory.getCurrentAndReset();
    }

    /**
     * Convert the given alias to an expression
     *
     * @param arg alias
     * @return expression
     */
    public static NumberPath<Byte> $(Byte arg) {
        return aliasFactory.getCurrentAndReset();
    }

    /**
     * Convert the given alias to an expression
     *
     * @param arg alias
     * @param <T>
     * @return expression
     */
    public static <T extends Enum<T>> EnumPath<T> $(T arg) {
        return aliasFactory.getCurrentAndReset();
    }

    /**
     * Convert the given alias to an expression
     *
     * @param arg alias
     * @param <D>
     * @return expression
     */
    public static <D> CollectionPath<D, SimpleExpression<D>> $(Collection<D> arg) {
        return aliasFactory.getCurrentAndReset();
    }

    /**
     * Convert the given alias to an expression
     *
     * @param arg alias
     * @param <D>
     * @return expression
     */
    public static <D extends Comparable<?>> ComparablePath<D> $(D arg) {
        return Alias.getPath(arg);
    }

    /**
     * Convert the given alias to an expression
     *
     * @param arg alias
     * @return expression
     */
    public static NumberPath<Double> $(Double arg) {
        return aliasFactory.getCurrentAndReset();
    }

    /**
     * Convert the given alias to an expression
     *
     * @param arg alias
     * @return expression
     */
    public static NumberPath<Float> $(Float arg) {
        return aliasFactory.getCurrentAndReset();
    }

    /**
     * Convert the given alias to an expression
     *
     * @param arg alias
     * @return expression
     */
    public static NumberPath<Integer> $(Integer arg) {
        return aliasFactory.getCurrentAndReset();
    }

    /**
     * Convert the given alias to an expression
     *
     * @param arg alias
     * @return expression
     */
    public static DatePath<java.sql.Date> $(java.sql.Date arg) {
        return aliasFactory.getCurrentAndReset();
    }

    /**
     * Convert the given alias to an expression
     *
     * @param arg alias
     * @return expression
     */
    public static DateTimePath<java.util.Date> $(java.util.Date arg) {
        return aliasFactory.getCurrentAndReset();
    }

    /**
     * Convert the given alias to an expression
     *
     * @param arg alias
     * @param <D>
     * @return expression
     */
    public static <D> ListPath<D, SimpleExpression<D>> $(List<D> arg) {
        return aliasFactory.getCurrentAndReset();
    }

    /**
     * Convert the given alias to an expression
     *
     * @param arg alias
     * @return expression
     */
    public static NumberPath<Long> $(Long arg) {
        return aliasFactory.getCurrentAndReset();
    }

    /**
     * Convert the given alias to an expression
     *
     * @param arg alias
     * @param <K>
     * @param <V>
     * @return expression
     */
    public static <K, V> MapPath<K, V, SimpleExpression<V>> $(Map<K, V> arg) {
        return aliasFactory.getCurrentAndReset();
    }

    /**
     * Convert the given alias to an expression
     *
     * @param arg alias
     * @param <D>
     * @return expression
     */
    public static <D> SetPath<D, SimpleExpression<D>> $(Set<D> arg) {
        return aliasFactory.getCurrentAndReset();
    }

    /**
     * Convert the given alias to an expression
     *
     * @param arg alias
     * @return expression
     */
    public static NumberPath<Short> $(Short arg) {
        return aliasFactory.getCurrentAndReset();
    }

    /**
     * Convert the given alias to an expression
     *
     * @param arg alias
     * @return expression
     */
    public static StringPath $(String arg) {
        return aliasFactory.getCurrentAndReset();
    }

    /**
     * Convert the given alias to an expression
     *
     * @param arg alias
     * @return expression
     */
    public static TimePath<Time> $(Time arg) {
        return aliasFactory.getCurrentAndReset();
    }

    /**
     * Convert the given alias to an expression
     *
     * @param arg alias
     * @return expression
     */
    public static DateTimePath<Timestamp> $(Timestamp arg) {
        return aliasFactory.getCurrentAndReset();
    }

    /**
     * Convert the given alias to an expression
     *
     * @param arg alias
     * @param <D>
     * @return expression
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public static <D> EntityPathBase<D> $(D arg) {
        EntityPathBase<D> rv = aliasFactory.getCurrentAndReset();
        if (rv != null) {
            return rv;
        } else if (arg instanceof EntityPath<?>) {    
            return (EntityPathBase<D>)arg; //NOSONAR
        } else if (arg instanceof ManagedObject) {
            return (EntityPathBase<D>) ((ManagedObject) arg).__mappedPath();
        } else {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    @Nullable
    private static <D, P extends Path<D>> P getPath(D arg) {
        P rv = aliasFactory.getCurrentAndReset();
        if (rv != null) {
            return rv;
        } else if (arg instanceof Path<?>) {    
            return (P)arg;
        } else if (arg instanceof ManagedObject) {
            return (P) ((ManagedObject) arg).__mappedPath();
        } else {
            return null;
        }
    }
    
    //CHECKSTYLE:ON

    /**
     * Create a new alias proxy of the given type
     * 
     * @param cl type of the alias
     * @return alias instance
     */
    public static <A> A alias(Class<A> cl) {
        return alias(cl, CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, cl.getSimpleName()));
    }

    /**
     * Create a new alias proxy of the given type for the given expression
     * 
     * @param cl type of the alias
     * @param expr underlying expression
     * @return alias instance
     */
    public static <A> A alias(Class<A> cl, Expression<? extends A> expr) {
        return aliasFactory.createAliasForExpr(cl, expr);
    }

    /**
     * Create a new alias proxy of the given type for the given variable
     * 
     * @param cl type of the alias
     * @param var variable name for the underlying expression
     * @return alias instance
     */
    public static <A> A alias(Class<A> cl, String var) {
        return aliasFactory.createAliasForVariable(cl, var);
    }

    /**
     * Convert the given alias to an expression
     *
     * @param <D>
     * @param arg alias instance
     * @return underlying expression
     */
    @SuppressWarnings("unchecked")
    public static <D> Expression<D> getAny(D arg) {
        Expression<D> current = aliasFactory.getCurrentAndReset();
        if (current != null) {
            return current;
        } else if (arg instanceof ManagedObject) {
            return (Expression<D>) ((ManagedObject) arg).__mappedPath();
        } else {
            throw new IllegalArgumentException("No path mapped to " + arg);
        }
    }

    /**
     * Reset the alias
     */
    public static void resetAlias() {
        aliasFactory.reset();
    }

    /**
     * Return the default variable
     *
     * @param <D>
     * @return expression
     */
    @SuppressWarnings("unchecked")
    public static <D> SimplePath<D> var() {
        return (SimplePath<D>) it;
    }

    /**
     * Create a new variable path
     *
     * @param arg alias
     * @param <D>
     * @return expression
     */
    @SuppressWarnings("unchecked")
    public static <D extends Comparable<?>> ComparablePath<D> var(D arg) {
        return Expressions.comparablePath((Class<D>)arg.getClass(), "var"+arg);
    }

    /**
     * Create a new variable path
     *
     * @param arg alias
     * @param <D>
     * @return expression
     */
    @SuppressWarnings("unchecked")
    public static <D extends Number & Comparable<D>> NumberPath<D> var(D arg) {
        return Expressions.numberPath((Class<D>)arg.getClass(), "var" + arg.getClass().getSimpleName() + arg);
    }

    /**
     * Create a new varibale path
     *
     * @param arg alias
     * @param <D>
     * @return expression
     */
    @SuppressWarnings("unchecked")
    public static <D> EntityPathBase<D> var(D arg) {
        String var = "var"+ arg.getClass().getSimpleName() + "_" + arg.toString().replace(' ', '_');
        return new PathBuilder<D>((Class)arg.getClass(), var);
    }

    /**
     * Create a new variable path
     *
     * @param arg alias
     * @return expression
     */
    public static StringPath var(String arg) {
        return Expressions.stringPath(arg.replace(' ', '_'));
    }

    private Alias() {}

}
