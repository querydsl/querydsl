/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import java.util.Arrays;
import java.util.Collection;

import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ECollection;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.EMap;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.path.PArray;

/**
 * Grammar provides the factory methods for the fluent grammar.
 * 
 * @author tiwe
 * @version $Id$
 */
public class Grammar {

    protected static final ExprFactory exprFactory = SimpleExprFactory.getInstance();

    protected static final OperationFactory operationFactory = SimpleOperationFactory.getInstance();

    /**
     * Expr : <code>left > right</code>
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean after(Expr<A> left, A right) {
        // NOTE : signature is for Comparables to support other than Java's date types
        return operationFactory.createBoolean(Ops.AFTER, left, exprFactory
                .createConstant(right));
    };
    
    /**
     * Expr : <code>left > right</code>
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */

    public static <A extends Comparable<?>> EBoolean after(Expr<A> left, Expr<A> right) {
        // NOTE : signature is for Comparables to support other than Java's date types
        return operationFactory.createBoolean(Ops.AFTER, left, right);
    }

    /**
     * Expr : <code>left and right</code>
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean and(EBoolean left, EBoolean right) {
        return operationFactory.createBoolean(Ops.AND, left, right);
    }
    
    /**
     * Expr : <code>left >= right (after or equals)</code>
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean aoe(Expr<A> left, A right) {
        return operationFactory.createBoolean(Ops.AOE, left, exprFactory.createConstant(right));
    }
    
    /**
     * Expr : <code>left >= right</code>
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean aoe(Expr<A> left, Expr<A> right) {
        return operationFactory.createBoolean(Ops.AOE, left, right);
    }
    
    /**
     * OrderSpecifier : asc target
     * 
     * @param <A>
     * @param target
     * @return
     */
    public static <A extends Comparable<?>> OrderSpecifier<A> asc(Expr<A> target) {
        return new OrderSpecifier<A>(Order.ASC, target);
    }

    /**
     * Expr : <code>avg(left)</code>
     * 
     * @param <A>
     * @param left
     * @return
     */
    public static <A extends Number & Comparable<?>> ENumber<Double> avg( Expr<A> left) {
        return operationFactory.createNumber(Double.class, Ops.AVG_AGG, left);
    }
    
    /**
     * Expr : <code>left < right</code>
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean before(Expr<A> left, A right) {
        // NOTE : signature is for Comparables to support other than Java's date types
        // NOTE : basically same as lt
        return operationFactory.createBoolean(Ops.BEFORE, left, exprFactory.createConstant(right));
    }

    /**
     * Expr : <code>left < right</code>
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean before(Expr<A> left, Expr<A> right) {
        // NOTE : signature is for Comparables to support other than Java's date types
        // NOTE : basically same as lt
        return operationFactory.createBoolean(Ops.BEFORE, left, right);
    }

    /**
     * Expr : <code>left between start and end</code>
     * 
     * @param <A>
     * @param left
     * @param start
     * @param end
     * @return
     */
    public static <A extends Comparable<?>> EBoolean between(Expr<A> left, A start, A end) {
        return operationFactory.createBoolean(Ops.BETWEEN, left, exprFactory.createConstant(start), exprFactory.createConstant(end));
    }

    /**
     * Expr : <code>left between start and end</code>
     * 
     * @param <A>
     * @param left
     * @param start
     * @param end
     * @return
     */
    public static <A extends Comparable<?>> EBoolean between(Expr<A> left, Expr<A> start, Expr<A> end) {
        return operationFactory.createBoolean(Ops.BETWEEN, left, start, end);
    }

    /**
     * Expr : <code>left <= right (before or equals)</code>
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean boe(Expr<A> left, A right) {
        return operationFactory.createBoolean(Ops.BOE, left, exprFactory.createConstant(right));
    }

    /**
     * Expr : <code>left <= right</code>
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean boe(Expr<A> left, Expr<A> right) {
        return operationFactory.createBoolean(Ops.BOE, left, right);
    }

    /**
     * Expr : <code>left.charAt(right)</code>
     * 
     * @param left
     * @param right
     * @return
     */
    public static Expr<Character> charAt(Expr<String> left, Expr<Integer> right) {
        return operationFactory.createComparable(Character.class, Ops.CHAR_AT,left, right);
    }

    /**
     * Expr : <code>left.charAt(right)</code>
     * 
     * @param left
     * @param right
     * @return
     */
    public static Expr<Character> charAt(Expr<String> left, int right) {
        return operationFactory.createComparable(Character.class, Ops.CHAR_AT, left, exprFactory.createConstant(right));
    }

    /**
     * Expr : <code>left || right</code>
     * 
     * @param left
     * @param right
     * @return
     */

    public static EString concat(Expr<String> left, Expr<String> right) {
        return operationFactory.createString(Ops.CONCAT, left, right);
    }

    /**
     * Expr : <code>left || right</code>
     * 
     * @param left
     * @param right
     * @return
     */
    public static EString concat(Expr<String> left, String right) {
        return operationFactory.createString(Ops.CONCAT, left, exprFactory.createConstant(right));
    }

    /**
     * Expr : <code>left.contains(right)</code>
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean contains(Expr<String> left, Expr<String> right) {
        return operationFactory.createBoolean(Ops.STRING_CONTAINS, left, right);
    }

    /**
     * Expr : <code>left.contains(right)</code>
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean contains(Expr<String> left, String right) {
        return operationFactory.createBoolean(Ops.STRING_CONTAINS, left, exprFactory.createConstant(right));
    }

    /**
     * Expr: <code>left.containsKey(right)</code>
     * 
     * @param <K>
     * @param left
     * @param right
     * @return
     */
    public static <K> EBoolean containsKey(EMap<K,?> left, Expr<K> right){
        return operationFactory.createBoolean(Ops.CONTAINS_KEY, (Expr<?>)left, right);
    }

    /**
     * Expr: <code>left.containsKey(right)</code>
     * 
     * @param <K>
     * @param left
     * @param right
     * @return
     */
    public static <K> EBoolean containsKey(EMap<K,?> left, K right){
        return operationFactory.createBoolean(Ops.CONTAINS_KEY, (Expr<?>)left, exprFactory.createConstant(right));
    }

    /**
     * Expr: <code>left.containsValue(right)</code>
     * 
     * @param <V>
     * @param left
     * @param right
     * @return
     */
    public static <V> EBoolean containsValue(EMap<?,V> left, Expr<V> right){
        return operationFactory.createBoolean(Ops.CONTAINS_VALUE, (Expr<?>)left, right);
    }

    /**
     * Expr: <code>left.containsValue(right)</code>
     * 
     * @param <V>
     * @param path
     * @param right
     * @return
     */
    public static <V> EBoolean containsValue(EMap<?,V> left, V right){
        return operationFactory.createBoolean(Ops.CONTAINS_VALUE, (Expr<?>)left, exprFactory.createConstant(right));
    }

    /**
     * Expr : <code>count(*)</code>
     * 
     * @return
     */
    public static ENumber<Long> count() {
        return operationFactory.createNumber(Long.class, Ops.COUNT_ALL_AGG);
        
    }

    /**
     * Expr : <code>count(expr)</code>
     * 
     * @param expr
     * @return
     */
    public static ENumber<Long> count(Expr<?> expr) {
        return operationFactory.createNumber(Long.class, Ops.COUNT_AGG, expr);
    }

    /**
     * OrderSpecifier : <code>desc target</code>
     * 
     * @param <A>
     * @param target
     * @return
     */
    public static <A extends Comparable<?>> OrderSpecifier<A> desc(Expr<A> target) {
        return new OrderSpecifier<A>(Order.DESC, target);
    }

    /**
     * Expr : <code>left.endsWith(right)</code>
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean endsWith(Expr<String> left, Expr<String> right) {
        return operationFactory.createBoolean(Ops.ENDSWITH, left, right);
    }

    /**
     * Expr : <code>left.endsWith(right) (ignore case)</code>
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
     * Expr : <code>left.endsWith(right)</code>
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean endsWith(Expr<String> left, String right) {
        return operationFactory.createBoolean(Ops.ENDSWITH, left, exprFactory.createConstant(right));
    }

    /**
     * Expr : <code>left.endsWith(right) (ignore case)</code>
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
            return operationFactory.createBoolean(Ops.ENDSWITH_IC, left, exprFactory.createConstant(right));
        }
    }
    
    /**
     * Expr : <code>left == right</code>
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A> EBoolean eq(Expr<A> left, A right) {
        if (isPrimitive(left.getType())) {
            return operationFactory.createBoolean(Ops.EQ_PRIMITIVE, left, exprFactory.createConstant(right));
        } else {
            return operationFactory.createBoolean(Ops.EQ_OBJECT, left, exprFactory.createConstant(right));
        }
    }

    /**
     * Expr : <code>left == right</code>
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A> EBoolean eq(Expr<A> left, Expr<? super A> right) {
        if (isPrimitive(left.getType())) {
            return operationFactory.createBoolean(Ops.EQ_PRIMITIVE, left, right);
        } else {
            return operationFactory.createBoolean(Ops.EQ_OBJECT, left, right);
        }
    }

    /**
     * Expr : <code>left.lower() == right.lower()</code>
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean equalsIgnoreCase(Expr<String> left, Expr<String> right) {
        return operationFactory.createBoolean(Ops.EQ_IGNORECASE, left, right);
    }

    /**
     * Expr : <code>left.lower() == right.lower()</code>
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean equalsIgnoreCase(Expr<String> left, String right) {
        return operationFactory.createBoolean(Ops.EQ_IGNORECASE, left,exprFactory.createConstant(right));
    }

    /**
     * Expr : <code>left >= right</code>
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean goe(Expr<A> left, A right) {
        return operationFactory.createBoolean(Ops.GOE, left, exprFactory.createConstant(right));
    }

    /**
     * Expr : <code>left >= right</code>
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean goe(Expr<A> left, Expr<A> right) {
        return operationFactory.createBoolean(Ops.GOE, left, right);
    }

    /**
     * Expr : <code>left > right</code>
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean gt(Expr<A> left, A right) {
        return operationFactory.createBoolean(Ops.GT, left, exprFactory.createConstant(right));
    }

    /**
     * Expr : <code>left > right</code>
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean gt(Expr<A> left, Expr<A> right) {
        return operationFactory.createBoolean(Ops.GT, left, right);
    }

    /**
     * Expr : <code>left in right OR right contains left</code>
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A> EBoolean in(A left, CollectionType<? extends A> right) {
        return operationFactory.createBoolean(Ops.IN, exprFactory.createConstant(left), (Expr<?>) right);
    }

    /**
     * Expr : <code>left in rest OR rest contains left</code>
     * 
     * @param <A>
     * @param left
     * @param rest
     * @return
     */
    public static <A> EBoolean in(Expr<A> left, A... rest) {
        return operationFactory.createBoolean(Ops.IN, left, exprFactory.createConstant(Arrays.asList(rest)));
    }

    /**
     * Expr : <code>left in right OR right contains left</code>
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A> EBoolean in(Expr<A> left, Collection<? extends A> right) {
        return operationFactory.createBoolean(Ops.IN, left, exprFactory.createConstant(right));
    }

    /**
     * Expr : <code>left in right OR right contains left</code>
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
     * Expr : <code>left.indexOf(right)</code>
     * 
     * @param left
     * @param right
     * @return
     */
    public static ENumber<Integer> indexOf(Expr<String> left, Expr<String> right) {
        return operationFactory.createNumber(Integer.class, Ops.INDEXOF, left, right);
    }

    /**
     * Expr : <code>left.indexOf(right)</code>
     * 
     * @param left
     * @param right
     * @return
     */
    public static ENumber<Integer> indexOf(Expr<String> left, String right) {
        return operationFactory.createNumber(Integer.class, Ops.INDEXOF, left, exprFactory.createConstant(right));
    }

    /**
     * Expr : <code>left.indexOf(right, i)</code>
     * 
     * @param left
     * @param right
     * @param i
     * @return
     */
    public static ENumber<Integer> indexOf(Expr<String> left, String right, int i) {
        return operationFactory.createNumber(Integer.class, Ops.INDEXOF_2ARGS,left, 
                exprFactory.createConstant(right), exprFactory.createConstant(i));
    }

    /**
     * Expr : <code>left instanceOf right</code>
     * 
     * @param <A>
     * @param <B>
     * @param left
     * @param right
     * @return
     */
    public static <A, B extends A> EBoolean instanceOf(Expr<A> left, Class<B> right) {
        return operationFactory.createBoolean(Ops.INSTANCEOF, left, exprFactory.createConstant(right));
    }

    /**
     * Expr : <code>left.isEmpty()</code>
     * 
     * @param left
     * @return
     */
    public static EBoolean isEmpty(Expr<String> left) {
        return operationFactory.createBoolean(Ops.STRING_ISEMPTY, left);
    }
    
    public static EBoolean isNotEmpty(Expr<String> left) {
        return operationFactory.createBoolean(Ops.STRING_ISNOTEMPTY, left);
    }

    /**
     * 
     * @param collection
     * @return
     */
    public static EBoolean isEmpty(ECollection<?> collection) {
        return operationFactory.createBoolean(Ops.COL_ISEMPTY,(Expr<?>) collection);
    }

    /**
     * 
     * @param map
     * @return
     */
    public static EBoolean isEmpty(EMap<?,?> map) {
        return operationFactory.createBoolean(Ops.MAP_ISEMPTY,(Expr<?>) map);
    }

    /**
     * 
     * @param collection
     * @return
     */
    public static EBoolean isNotEmpty(ECollection<?> collection) {
        return operationFactory.createBoolean(Ops.COL_ISNOTEMPTY,(Expr<?>) collection);
    }

    /**
     * 
     * @param collection
     * @return
     */
    public static EBoolean isNotEmpty(EMap<?,?> map) {
        return operationFactory.createBoolean(Ops.MAP_ISNOTEMPTY,(Expr<?>) map);
    }

    /**
     * Expr : <code>left is not null</code>
     * 
     * @param <A>
     * @param left
     * @return
     */
    public static <A> EBoolean isNotNull(Expr<A> left) {
        return operationFactory.createBoolean(Ops.ISNOTNULL, left);
    }

    /**
     * Expr : <code>left is null</code>
     * 
     * @param <A>
     * @param left
     * @return
     */
    public static <A> EBoolean isNull(Expr<A> left) {
        return operationFactory.createBoolean(Ops.ISNULL, left);
    }

    private static boolean isPrimitive(Class<? extends Object> class1) {
        if (class1 == null){
            return false;
        }            
        return class1.isPrimitive() 
            || Number.class.isAssignableFrom(class1) 
            || Boolean.class.equals(class1) 
            || Character.class.equals(class1);
    }

    /**
     * Expr : <code>left.length()</code>
     * 
     * @param left
     * @return
     */
    public static ENumber<Long> length(Expr<String> left) {
        return operationFactory.createNumber(Long.class, Ops.STRING_LENGTH,left);
    }

    /**
     * Expr : <code>left like right</code>
     * 
     * @param left
     * @param right
     * @return
     */
//    public static EBoolean like(Expr<String> left, String right) {
//        return operationFactory.createBoolean(Ops.LIKE, left, exprFactory
//                .createConstant(right));
//    }

    /**
     * Expr : <code>left <= right</code>
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
     * Expr : <code>left <= right</code>
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean loe(Expr<A> left, Expr<A> right) {
        return operationFactory.createBoolean(Ops.LOE, left, right);
    }

    /**
     * Expr : <code>left.toLowerCase()</code>
     * 
     * @param left
     * @return
     */
    public static EString lower(Expr<String> left) {
        return operationFactory.createString(Ops.LOWER, left);
    }

    /**
     * Expr : <code>left < right</code>
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean lt(Expr<A> left, A right) {
        return operationFactory.createBoolean(Ops.LT, left, exprFactory.createConstant(right));
    }

    /**
     * Expr : <code>left < right</code>
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean lt(Expr<A> left, Expr<A> right) {
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
        return operationFactory.createBoolean(Ops.MATCHES, left, exprFactory.createConstant(right));
    }

    /**
     * 
     * @param <A>
     * @param left
     * @return
     */
    public static <A extends Number & Comparable<?>> ENumber<A> max( Expr<A> left) {
        return operationFactory.createNumber(left.getType(), Ops.MAX_AGG, left);
    }

    /**
     * 
     * @param <A>
     * @param left
     * @return
     */
    public static <A extends Number & Comparable<?>> ENumber<A> min( Expr<A> left) {
        return operationFactory.createNumber(left.getType(), Ops.MIN_AGG, left);
    }

    /**
     * Expr : <code>left != right</code>
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
     * Expr : <code>left != right</code>
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A> EBoolean ne(Expr<A> left, Expr<? super A> right) {
        if (isPrimitive(left.getType())) {
            return operationFactory.createBoolean(Ops.NE_PRIMITIVE, left, right);
        } else {
            return operationFactory.createBoolean(Ops.NE_OBJECT, left, right);
        }
    }

    /**
     * Expr <code>!left</code>
     * 
     * @param left
     * @return
     */
    public static EBoolean not(EBoolean left) {
        // TODO : use a known set of operation negations
        if (left instanceof OBoolean) {
            OBoolean o = (OBoolean) left;
            if (o.getOperator() == Ops.NOT){
                return (EBoolean) o.getArg(0);
            }                
        }
        return operationFactory.createBoolean(Ops.NOT, left);
    }

    /**
     * Expr : <code>left not between start and end</code>
     * 
     * @param <A>
     * @param left
     * @param start
     * @param end
     * @return
     */
    public static <A extends Comparable<?>> EBoolean notBetween(Expr<A> left, A start, A end) {
        return operationFactory.createBoolean(Ops.NOTBETWEEN, left, exprFactory
                .createConstant(start), exprFactory.createConstant(end));
    }

    /**
     * Expr : <code>left not between start and end</code>
     * 
     * @param <A>
     * @param left
     * @param start
     * @param end
     * @return
     */
    public static <A extends Comparable<?>> EBoolean notBetween(Expr<A> left, Expr<A> start, Expr<A> end) {
        return operationFactory.createBoolean(Ops.NOTBETWEEN, left, start, end);
    }

    /**
     * Expr : <code>left not in rest</code>
     * 
     * @param <A>
     * @param left
     * @param rest
     * @return
     */
    public static <A> EBoolean notIn(Expr<A> left, A... rest) {
        return operationFactory.createBoolean(Ops.NOTIN, left, exprFactory.createConstant(Arrays.asList(rest)));
    }

    
    /**
     * Expr : <code>left not in right</code>
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A> EBoolean notIn(Expr<A> left,CollectionType<? extends A> right) {
        return operationFactory.createBoolean(Ops.NOTIN, left, (Expr<?>) right);
    }

    /**
     * Expr : <code>cast(source as targetType)</code>
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
     * Expr : <code>left or right</code>
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean or(EBoolean left, EBoolean right) {
        return operationFactory.createBoolean(Ops.OR, left, right);
    }

    /**
     * 
     * @param path
     * @return
     */
    public static ENumber<Integer> size(PArray<?> path){
        return operationFactory.createNumber(Integer.class, Ops.COL_SIZE, (Expr<?>)path);
    }

    /**
     * 
     * @param path
     * @return
     */
    public static ENumber<Integer> size(ECollection<?> path){
        return operationFactory.createNumber(Integer.class, Ops.COL_SIZE, (Expr<?>)path);
    }

    public static ENumber<Integer> size(EMap<?,?> path){
        return operationFactory.createNumber(Integer.class, Ops.MAP_SIZE, (Expr<?>)path);
    }
    
    /**
     * Expr : <code>left.startsWith(right)</code>
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean startsWith(Expr<String> left, Expr<String> right) {
        return operationFactory.createBoolean(Ops.STARTSWITH, left, right);
    }

    /**
     * Expr : <code>left.startsWith(right) (ignore case)</code>
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
            return operationFactory.createBoolean(Ops.STARTSWITH_IC, left, right);
        }
    }

    /**
     * Expr : <code>left.startsWith(right)</code>
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean startsWith(Expr<String> left, String right) {
        return operationFactory.createBoolean(Ops.STARTSWITH, left, exprFactory.createConstant(right));
    }

    /**
     * Expr : <code>left.startsWith(right) (ignore case)</code>
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
            return operationFactory.createBoolean(Ops.STARTSWITH_IC, left,exprFactory.createConstant(right));
        }
    }

    /**
     * Expr : <code>cast(source as String)</code>
     * 
     * @param source
     * @return
     */
    public static EString stringCast(EComparable<?> source) {
        return operationFactory.createString(Ops.STRING_CAST, source);
    }

    /**
     * Expr : <code>left.substring(right)</code>
     * 
     * @param left
     * @param right
     * @return
     */
    public static EString substring(Expr<String> left, int right) {
        return operationFactory.createString(Ops.SUBSTR1ARG, left, exprFactory.createConstant(right));
    }

    /**
     * Expr : <code>left.substring(beginIndex, endIndex)</code>
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
     * Expr : <code>sum(left)</code>
     * 
     * @param <A>
     * @param left
     * @return
     */
    public static <A extends Number & Comparable<?>> ENumber<Double> sum( Expr<A> left) {
        return operationFactory.createNumber(Double.class, Ops.SUM_AGG, left);
    }
    
    /**
     * Expr : <code>left.trim()</code>
     * 
     * @param left
     * @return
     */
    public static EString trim(Expr<String> left) {
        return operationFactory.createString(Ops.TRIM, left);
    }

    /**
     * Expr : <code>left.toUpperCase()</code>
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
