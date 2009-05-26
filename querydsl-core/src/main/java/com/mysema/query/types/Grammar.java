/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import java.util.Collection;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.alias.AEntity;
import com.mysema.query.types.alias.AEntityCollection;
import com.mysema.query.types.alias.ASimple;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.ESimple;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.operation.Ops.OpNumberAgg;
import com.mysema.query.types.path.PCollection;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PEntityCollection;
import com.mysema.query.types.path.PMap;

/**
 * Grammar provides the factory methods for the fluent grammar.
 * 
 * @author tiwe
 * @version $Id$
 */
public class Grammar {

    protected static final ExprFactory exprFactory = SimpleExprFactory
            .getInstance();

    protected static final OperationFactory operationFactory = SimpleOperationFactory
            .getInstance();

    /**
     * Expr : left > right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean after(Expr<A> left, A right) {
        // NOTE : signature is for Comparables to support other than Java's date
        // types
        return operationFactory.createBoolean(Ops.AFTER, left, exprFactory
                .createConstant(right));
    };

    /**
     * Expr : left > right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */

    public static <A extends Comparable<?>> EBoolean after(Expr<A> left,
            Expr<A> right) {
        // NOTE : signature is for Comparables to support other than Java's date
        // types
        return operationFactory.createBoolean(Ops.AFTER, left, right);
    }
    
    /**
     * Expr : left and right
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean and(EBoolean left, EBoolean right) {
        return operationFactory.createBoolean(Ops.AND, left, right);
    }
    
    /**
     * Expr : left >= right (after or equals)
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean aoe(Expr<A> left, A right) {
        return operationFactory.createBoolean(Ops.AOE, left, exprFactory
                .createConstant(right));
    }
    
    /**
     * Expr : left >= right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean aoe(Expr<A> left,
            Expr<A> right) {
        return operationFactory.createBoolean(Ops.AOE, left, right);
    }
    
    /**
     * Expr : from as to
     * 
     * @param <D>
     * @param from
     * @param to
     * @return
     */
    public static <D> ASimple<D> as(ESimple<D> from, String to) {
        return new ASimple<D>(Assert.notNull(from), Assert.notNull(to));
    }

    /**
     * Expr : from as to
     * 
     * @param <D>
     * @param from
     * @param to
     * @return
     */
    public static <D> AEntity<D> as(PEntity<D> from, PEntity<D> to) {
        return new AEntity<D>(Assert.notNull(from), Assert.notNull(to));
    }

    /**
     * Expr : from as to
     * 
     * @param <D>
     * @param from
     * @param to
     * @return
     */
    public static <D> AEntityCollection<D> as(PEntityCollection<D> from,
            PEntity<D> to) {
        return new AEntityCollection<D>(Assert.notNull(from), Assert
                .notNull(to));
    }

    /**
     * OrderSpecificier : asc target
     * 
     * @param <A>
     * @param target
     * @return
     */
    public static <A extends Comparable<?>> OrderSpecifier<A> asc(Expr<A> target) {
        return new OrderSpecifier<A>(Order.ASC, target);
    }

    /**
     * Expr : avg(left)
     * 
     * @param <A>
     * @param left
     * @return
     */
    public static <A extends Number & Comparable<?>> ENumber<Double> avg(
            Expr<A> left) {
        return operationFactory.createNumber(Double.class, OpNumberAgg.AVG_AGG,
                left);
    }

    /**
     * Expr : left < right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean before(Expr<A> left,
            A right) {
        // NOTE : signature is for Comparables to support other than Java's date
        // types
        // NOTE : basically same as lt
        return operationFactory.createBoolean(Ops.BEFORE, left, exprFactory
                .createConstant(right));
    }

    /**
     * Expr : left < right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean before(Expr<A> left,
            Expr<A> right) {
        // NOTE : signature is for Comparables to support other than Java's date types
        // NOTE : basically same as lt
        return operationFactory.createBoolean(Ops.BEFORE, left, right);
    }

    /**
     * Expr : left between start and end
     * 
     * @param <A>
     * @param left
     * @param start
     * @param end
     * @return
     */
    public static <A extends Comparable<?>> EBoolean between(Expr<A> left,
            A start, A end) {
        return operationFactory.createBoolean(Ops.BETWEEN, left, exprFactory
                .createConstant(start), exprFactory.createConstant(end));
    }

    /**
     * Expr : left between start and end
     * 
     * @param <A>
     * @param left
     * @param start
     * @param end
     * @return
     */
    public static <A extends Comparable<?>> EBoolean between(Expr<A> left,
            Expr<A> start, Expr<A> end) {
        return operationFactory.createBoolean(Ops.BETWEEN, left, start, end);
    }

    /**
     * Expr : left <= right (before or equals)
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean boe(Expr<A> left, A right) {
        return operationFactory.createBoolean(Ops.BOE, left, exprFactory
                .createConstant(right));
    }

    /**
     * Expr : left <= right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean boe(Expr<A> left,
            Expr<A> right) {
        return operationFactory.createBoolean(Ops.BOE, left, right);
    }

    /**
     * Expr : left.chartAt(right)
     * 
     * @param left
     * @param right
     * @return
     */
    public static Expr<Character> charAt(Expr<String> left, Expr<Integer> right) {
        return operationFactory.createComparable(Character.class, Ops.CHAR_AT,
                left, right);
    }

    /**
     * Expr : left.charAt(right)
     * 
     * @param left
     * @param right
     * @return
     */
    public static Expr<Character> charAt(Expr<String> left, int right) {
        return operationFactory.createComparable(Character.class, Ops.CHAR_AT,
                left, exprFactory.createConstant(right));
    }

    /**
     * Expr : left || right
     * 
     * @param left
     * @param right
     * @return
     */

    public static EString concat(Expr<String> left, Expr<String> right) {
        return operationFactory.createString(Ops.CONCAT, left, right);
    }

    /**
     * Expr : left || right
     * 
     * @param left
     * @param right
     * @return
     */
    public static EString concat(Expr<String> left, String right) {
        return operationFactory.createString(Ops.CONCAT, left, exprFactory
                .createConstant(right));
    }

    /**
     * Expr : left || right
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean contains(Expr<String> left, Expr<String> right) {
        return operationFactory.createBoolean(Ops.CONTAINS, left, right);
    }

    /**
     * Expr : left || right
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean contains(Expr<String> left, String right) {
        return operationFactory.createBoolean(Ops.CONTAINS, left, exprFactory
                .createConstant(right));
    }

    /**
     * 
     * @param <K>
     * @param path
     * @param key
     * @return
     */
    public static <K> EBoolean containsKey(PMap<K,?> path, Expr<K> key){
        return operationFactory.createBoolean(Ops.CONTAINS_KEY, (Expr<?>)path, key);
    }

    /**
     * 
     * @param <K>
     * @param path
     * @param key
     * @return
     */
    public static <K> EBoolean containsKey(PMap<K,?> path, K key){
        return operationFactory.createBoolean(Ops.CONTAINS_KEY, (Expr<?>)path, exprFactory.createConstant(key));
    }

    /**
     * 
     * @param <V>
     * @param path
     * @param value
     * @return
     */
    public static <V> EBoolean containsValue(PMap<?,V> path, Expr<V> value){
        return operationFactory.createBoolean(Ops.CONTAINS_VALUE, (Expr<?>)path, value);
    }

    /**
     * 
     * @param <V>
     * @param path
     * @param value
     * @return
     */
    public static <V> EBoolean containsValue(PMap<?,V> path, V value){
        return operationFactory.createBoolean(Ops.CONTAINS_VALUE, (Expr<?>)path, exprFactory.createConstant(value));
    }

    /**
     * Expr : count(*)
     * 
     * @return
     */
    public static ENumber<Long> count() {
        return new CountExpression(null);
    }

    /**
     * Expr : count(expr)
     * 
     * @param expr
     * @return
     */
    public static ENumber<Long> count(Expr<?> expr) {
        return new CountExpression(expr);
    }

    /**
     * OrderSpecifier : desc target
     * 
     * @param <A>
     * @param target
     * @return
     */
    public static <A extends Comparable<?>> OrderSpecifier<A> desc(
            Expr<A> target) {
        return new OrderSpecifier<A>(Order.DESC, target);
    }

    /**
     * 
     * @param collection
     * @return
     */
    public static EBoolean empty(PCollection<?> collection) {
        return operationFactory.createBoolean(Ops.COL_ISEMPTY,
                (Expr<?>) collection);
    }

    /**
     * Expr : left.endsWith(right)
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean endsWith(Expr<String> left, Expr<String> right) {
        return operationFactory.createBoolean(Ops.ENDSWITH, left, right);
    }

    /**
     * Expr : left.endsWith(right) (ignore case)
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean endsWith(Expr<String> left, Expr<String> right,
            boolean caseSensitive) {
        if (caseSensitive) {
            return endsWith(left, right);
        } else {
            return operationFactory.createBoolean(Ops.ENDSWITH_IC, left, right);
        }
    }

    /**
     * Expr : left.endsWith(right)
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean endsWith(Expr<String> left, String right) {
        return operationFactory.createBoolean(Ops.ENDSWITH, left, exprFactory
                .createConstant(right));
    }

    /**
     * Expr : left.endsWith(right) (ignore case)
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean endsWith(Expr<String> left, String right,
            boolean caseSensitive) {
        if (caseSensitive) {
            return endsWith(left, right);
        } else {
            return operationFactory.createBoolean(Ops.ENDSWITH_IC, left,
                    exprFactory.createConstant(right));
        }
    }

    /**
     * Expr : left == right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A> EBoolean eq(Expr<A> left, A right) {
        if (isPrimitive(left.getType())) {
            return operationFactory.createBoolean(Ops.EQ_PRIMITIVE, left,
                    exprFactory.createConstant(right));
        } else {
            return operationFactory.createBoolean(Ops.EQ_OBJECT, left,
                    exprFactory.createConstant(right));
        }
    }

    /**
     * Expr : left == right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A> EBoolean eq(Expr<A> left, Expr<? super A> right) {
        if (isPrimitive(left.getType())) {
            return operationFactory
                    .createBoolean(Ops.EQ_PRIMITIVE, left, right);
        } else {
            return operationFactory.createBoolean(Ops.EQ_OBJECT, left, right);
        }
    }

    /**
     * Expr : left.lower() == right.lower()
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean equalsIgnoreCase(Expr<String> left,
            Expr<String> right) {
        return operationFactory.createBoolean(Ops.EQ_IGNORECASE, left, right);
    }

    /**
     * Expr : left.lower() == right.lower()
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean equalsIgnoreCase(Expr<String> left, String right) {
        return operationFactory.createBoolean(Ops.EQ_IGNORECASE, left,
                exprFactory.createConstant(right));
    }

    /**
     * Expr : left >= right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean goe(Expr<A> left, A right) {
        return operationFactory.createBoolean(Ops.GOE, left, exprFactory
                .createConstant(right));
    }

    /**
     * Expr : left >= right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean goe(Expr<A> left,
            Expr<A> right) {
        return operationFactory.createBoolean(Ops.GOE, left, right);
    }

    /**
     * Expr : left > right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean gt(Expr<A> left, A right) {
        return operationFactory.createBoolean(Ops.GT, left, exprFactory
                .createConstant(right));
    }

    /**
     * Expr : left > right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean gt(Expr<A> left,
            Expr<A> right) {
        return operationFactory.createBoolean(Ops.GT, left, right);
    }

    /**
     * Expr : left in right OR right contains left
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A> EBoolean in(A left, CollectionType<? extends A> right) {
        return operationFactory.createBoolean(Ops.IN, exprFactory
                .createConstant(left), (Expr<?>) right);
    }

    /**
     * Expr : left in rest OR rest contains left
     * 
     * @param <A>
     * @param left
     * @param rest
     * @return
     */
    public static <A> EBoolean in(Expr<A> left, A... rest) {
        return operationFactory.createBoolean(Ops.IN, left, exprFactory
                .createConstant(rest));
    }

    /**
     * Expr : left in right OR right contains left
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A> EBoolean in(Expr<A> left, Collection<? extends A> right) {
        return operationFactory.createBoolean(Ops.IN, left, exprFactory
                .createConstant(right));
    }

    /**
     * Expr : left in right OR right contains left
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A> EBoolean in(Expr<A> left, CollectionType<A> right) {
        return operationFactory.createBoolean(Ops.IN, left, (Expr<?>) right);
    }

    /**
     * Expr : left.indexOf(right)
     * 
     * @param left
     * @param right
     * @return
     */
    public static ENumber<Integer> indexOf(Expr<String> left, Expr<String> right) {
        return operationFactory.createNumber(Integer.class, Ops.INDEXOF, left,
                right);
    }

    /**
     * Expr : left.indexOf(right)
     * 
     * @param left
     * @param right
     * @return
     */
    public static ENumber<Integer> indexOf(Expr<String> left, String right) {
        return operationFactory.createNumber(Integer.class, Ops.INDEXOF, left,
                exprFactory.createConstant(right));
    }

    /**
     * Expr : left.indexOf(right, i)
     * 
     * @param left
     * @param right
     * @param i
     * @return
     */
    public static ENumber<Integer> indexOf(Expr<String> left, String right,
            int i) {
        return operationFactory.createNumber(Integer.class, Ops.INDEXOF_2ARGS,
                left, exprFactory.createConstant(right), exprFactory
                        .createConstant(i));
    }

    /**
     * Expr : left instanceOf right
     * 
     * @param <A>
     * @param <B>
     * @param left
     * @param right
     * @return
     */
    public static <A, B extends A> EBoolean instanceOf(Expr<A> left,
            Class<B> right) {
        return operationFactory.createBoolean(Ops.ISTYPEOF, left, exprFactory
                .createConstant(right));
    }

    /**
     * Expr : left.isEmpty()
     * 
     * @param left
     * @return
     */
    public static EBoolean isEmpty(Expr<String> left) {
        return operationFactory.createBoolean(Ops.STRING_ISEMPTY, left);
    }

    /**
     * Expr : left is not null
     * 
     * @param <A>
     * @param left
     * @return
     */
    public static <A> EBoolean isnotnull(Expr<A> left) {
        return operationFactory.createBoolean(Ops.ISNOTNULL, left);
    }

    /**
     * Expr : left is null
     * 
     * @param <A>
     * @param left
     * @return
     */
    public static <A> EBoolean isnull(Expr<A> left) {
        return operationFactory.createBoolean(Ops.ISNULL, left);
    }

    private static boolean isPrimitive(Class<? extends Object> class1) {
        if (class1 == null)
            return false;
        return class1.isPrimitive() || Number.class.isAssignableFrom(class1)
                || Boolean.class.equals(class1);
    }

    /**
     * Expr : left.lastIndexOf(right, third);
     * 
     * @param left
     * @param right
     * @param third
     * @return
     */
    public static ENumber<Integer> lastIndex(Expr<String> left, String right,
            int third) {
        return operationFactory.createNumber(Integer.class,
                Ops.LAST_INDEX_2ARGS, left, exprFactory.createConstant(right),
                exprFactory.createConstant(third));
    }

    /**
     * Expr : left.lastIndexOf(right)
     * 
     * @param left
     * @param right
     * @return
     */
    public static ENumber<Integer> lastIndexOf(Expr<String> left,
            Expr<String> right) {
        return operationFactory.createNumber(Integer.class, Ops.LAST_INDEX,
                left, right);
    }

    /**
     * Expr : left.lastIndexOf(right)
     * 
     * @param left
     * @param right
     * @return
     */
    public static ENumber<Integer> lastIndexOf(Expr<String> left, String right) {
        return operationFactory.createNumber(Integer.class, Ops.LAST_INDEX,
                left, exprFactory.createConstant(right));
    }

    /**
     * Expr : left.length()
     * 
     * @param left
     * @return
     */
    public static ENumber<Integer> length(Expr<String> left) {
        return operationFactory.createNumber(Integer.class, Ops.STRING_LENGTH,
                left);
    }

    /**
     * Expr : left like right
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean like(Expr<String> left, String right) {
        return operationFactory.createBoolean(Ops.LIKE, left, exprFactory
                .createConstant(right));
    }

    /**
     * Expr : left <= right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean loe(Expr<A> left, A right) {
        return operationFactory.createBoolean(Ops.LOE, left, exprFactory
                .createConstant(right));
    }

    /**
     * Expr : left <= right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean loe(Expr<A> left,
            Expr<A> right) {
        return operationFactory.createBoolean(Ops.LOE, left, right);
    }

    /**
     * Expr : left.toLowerCase()
     * 
     * @param left
     * @return
     */
    public static EString lower(Expr<String> left) {
        return operationFactory.createString(Ops.LOWER, left);
    }

    /**
     * Expr : left < right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean lt(Expr<A> left, A right) {
        return operationFactory.createBoolean(Ops.LT, left, exprFactory
                .createConstant(right));
    }

    /**
     * Expr : left < right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean lt(Expr<A> left,
            Expr<A> right) {
        return operationFactory.createBoolean(Ops.LT, left, right);
    }

    /**
     * Returns true if the left term matches the regex of the right term
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean matches(Expr<String> left, Expr<String> right) {
        return operationFactory.createBoolean(Ops.MATCHES, left, right);
    }

    /**
     * Returns true if the left term matches the regex of the right term
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean matches(Expr<String> left, String right) {
        return operationFactory.createBoolean(Ops.MATCHES, left, exprFactory
                .createConstant(right));
    }

    /**
     * Expr : left != right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A> EBoolean ne(Expr<A> left, A right) {
        if (isPrimitive(left.getType())) {
            return operationFactory.createBoolean(Ops.NE_PRIMITIVE, left,
                    exprFactory.createConstant(right));
        } else {
            return operationFactory.createBoolean(Ops.NE_OBJECT, left,
                    exprFactory.createConstant(right));
        }
    }

    /**
     * Expr : left != right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A> EBoolean ne(Expr<A> left, Expr<? super A> right) {
        if (isPrimitive(left.getType())) {
            return operationFactory
                    .createBoolean(Ops.NE_PRIMITIVE, left, right);
        } else {
            return operationFactory.createBoolean(Ops.NE_OBJECT, left, right);
        }
    }

    /**
     * Expr !left
     * 
     * @param left
     * @return
     */
    public static EBoolean not(EBoolean left) {
        if (left instanceof OBoolean) {
            OBoolean o = (OBoolean) left;
            if (o.getOperator() == Ops.NOT)
                return (EBoolean) o.getArg(0);
        }
        return operationFactory.createBoolean(Ops.NOT, left);
    }

    /**
     * Expr : left not between start and end
     * 
     * @param <A>
     * @param left
     * @param start
     * @param end
     * @return
     */
    public static <A extends Comparable<?>> EBoolean notBetween(Expr<A> left,
            A start, A end) {
        return operationFactory.createBoolean(Ops.NOTBETWEEN, left, exprFactory
                .createConstant(start), exprFactory.createConstant(end));
    }

    /**
     * Expr : left not between start and end
     * 
     * @param <A>
     * @param left
     * @param start
     * @param end
     * @return
     */
    public static <A extends Comparable<?>> EBoolean notBetween(Expr<A> left,
            Expr<A> start, Expr<A> end) {
        return operationFactory.createBoolean(Ops.NOTBETWEEN, left, start, end);
    }

    public static EBoolean notEmpty(PCollection<?> collection) {
        return operationFactory.createBoolean(Ops.COL_ISNOTEMPTY,
                (Expr<?>) collection);
    }

    /**
     * Expr : left not in rest
     * 
     * @param <A>
     * @param left
     * @param rest
     * @return
     */
    public static <A> EBoolean notIn(Expr<A> left, A... rest) {
        return operationFactory.createBoolean(Ops.NOTIN, left, exprFactory
                .createConstant(rest));
    }

    /**
     * Expr : left not in right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A> EBoolean notIn(Expr<A> left,
            CollectionType<? extends A> right) {
        return operationFactory.createBoolean(Ops.NOTIN, left, (Expr<?>) right);
    }

    /**
     * Expr : cast(source as targetType)
     * 
     * @param <A>
     * @param source
     * @param targetType
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <A extends Number & Comparable<?>> ENumber<A> numericCast(
            EComparable<?> source, Class<A> targetType) {
        if (targetType.isAssignableFrom(source.getType())
                && ENumber.class.isAssignableFrom(source.getClass())) {
            return (ENumber) source;
        } else {
            return operationFactory.createNumber(targetType, Ops.NUMCAST,
                    source, exprFactory.createConstant(targetType));
        }
    }

    /**
     * Expr : left or right
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean or(EBoolean left, EBoolean right) {
        return operationFactory.createBoolean(Ops.OR, left, right);
    }

    /**
     * Split the given String left with refex as the matcher for the separator
     * 
     * @param left
     * @param regex
     * @return
     */
    public static Expr<String[]> split(Expr<String> left, String regex) {
        return operationFactory.createStringArray(Ops.SPLIT, left, exprFactory
                .createConstant(regex));
    }

    /**
     * Expr : left.startsWith(right)
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean startsWith(Expr<String> left, Expr<String> right) {
        return operationFactory.createBoolean(Ops.STARTSWITH, left, right);
    }

    /**
     * Expr : left.startsWith(right) (ignore case)
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean startsWith(Expr<String> left, Expr<String> right,
            boolean caseSensitive) {
        if (caseSensitive) {
            return startsWith(left, right);
        } else {
            return operationFactory.createBoolean(Ops.STARTSWITH_IC, left,
                    right);
        }
    }

    /**
     * Expr : left.startsWith(right)
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean startsWith(Expr<String> left, String right) {
        return operationFactory.createBoolean(Ops.STARTSWITH, left, exprFactory
                .createConstant(right));
    }

    /**
     * Expr : left.startsWith(right) (ignore case)
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean startsWith(Expr<String> left, String right,
            boolean caseSensitive) {
        if (caseSensitive) {
            return startsWith(left, right);
        } else {
            return operationFactory.createBoolean(Ops.STARTSWITH_IC, left,
                    exprFactory.createConstant(right));
        }
    }

    /**
     * Expr : cast(source as String)
     * 
     * @param source
     * @return
     */
    public static EString stringCast(EComparable<?> source) {
        return operationFactory.createString(Ops.STRING_CAST, source);
    }

    /**
     * Expr : left.substring(right)
     * 
     * @param left
     * @param right
     * @return
     */
    public static EString substring(Expr<String> left, int right) {
        return operationFactory.createString(Ops.SUBSTR1ARG, left, exprFactory
                .createConstant(right));
    }

    /**
     * Expr : left.substring(beginIndex, endIndex)
     * 
     * @param left
     * @param beginIndex
     * @param endIndex
     * @return
     */
    public static EString substring(Expr<String> left, int beginIndex,
            int endIndex) {
        return operationFactory.createString(Ops.SUBSTR2ARGS, left, exprFactory
                .createConstant(beginIndex), exprFactory
                .createConstant(endIndex));
    }

    /**
     * Expr : left.trim()
     * 
     * @param left
     * @return
     */
    public static EString trim(Expr<String> left) {
        return operationFactory.createString(Ops.TRIM, left);
    }

    /**
     * Expr : left.toUpperCase()
     * 
     * @param left
     * @return
     */
    public static EString upper(Expr<String> left) {
        return operationFactory.createString(Ops.UPPER, left);
    }

    protected Grammar() {
    }
}
