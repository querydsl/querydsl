/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import java.util.Collection;

import com.mysema.query.grammar.Ops.OpNumberAgg;
import com.mysema.query.grammar.types.Alias;
import com.mysema.query.grammar.types.CollectionType;
import com.mysema.query.grammar.types.CountExpression;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Factory;
import com.mysema.query.grammar.types.Expr.EBoolean;
import com.mysema.query.grammar.types.Expr.EComparable;
import com.mysema.query.grammar.types.Expr.ENumber;
import com.mysema.query.grammar.types.Expr.ESimple;
import com.mysema.query.grammar.types.Expr.EString;
import com.mysema.query.grammar.types.Operation.OBoolean;
import com.mysema.query.grammar.types.Path.PEntity;
import com.mysema.query.grammar.types.Path.PEntityCollection;
import com.mysema.query.util.Assert;

/**
 * Grammar provides the factory methods for the fluent grammar.
 * 
 * @author tiwe
 * @version $Id$
 */
public class Grammar extends Factory{
    
    protected Grammar(){};
        
    /**
     * Expr : left > right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<? super A>> EBoolean after(Expr<A> left, A right) {
        // NOTE : signature is for Comparables to support other than Java's date types
        return createBoolean(Ops.AFTER, left, createConstant(right));
    }
    
    /**
     * Expr : left >= right (after or equals)
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<? super A>> EBoolean aoe(Expr<A> left, A right) {
        return createBoolean(Ops.AOE, left, createConstant(right));
    }

    /**
     * Expr : left > right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    
    public static <A extends Comparable<? super A>> EBoolean after(Expr<A> left, Expr<A> right) {
        // NOTE : signature is for Comparables to support other than Java's date types
        return createBoolean(Ops.AFTER, left, right);
    }
    
    /**
     * Expr : left >= right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<? super A>> EBoolean aoe(Expr<A> left, Expr<A> right) {
        return createBoolean(Ops.AOE, left, right);
    }

    /**
     * Expr : left and right
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean and(EBoolean left, EBoolean right) {
        return createBoolean(Ops.AND, left, right);
    }
    
    /**
     * Expr : from as to
     * 
     * @param <D>
     * @param from
     * @param to
     * @return
     */    
    public static <D> Alias.ASimple<D> as(ESimple<D> from, String to) {
        return new Alias.ASimple<D>(Assert.notNull(from), Assert.notNull(to));
    }
    
    /**
     * Expr : from as to
     * 
     * @param <D>
     * @param from
     * @param to
     * @return
     */
    public static <D> Alias.AEntity<D> as(PEntity<D> from, PEntity<D> to) {
        return new Alias.AEntity<D>(Assert.notNull(from), Assert.notNull(to));
    }

    /**
     * Expr : from as to
     * 
     * @param <D>
     * @param from
     * @param to
     * @return
     */
    public static <D> Alias.AEntityCollection<D> as(PEntityCollection<D> from, PEntity<D> to) {
        return new Alias.AEntityCollection<D>(Assert.notNull(from), Assert.notNull(to));
    }

    /**
     * OrderSpecificier : asc target
     * 
     * @param <A>
     * @param target
     * @return
     */
    public static <A extends Comparable<? super A>> OrderSpecifier<A> asc(Expr<A> target) {
        OrderSpecifier<A> os = new OrderSpecifier<A>();
        os.order = Order.ASC;
        os.target = Assert.notNull(target);
        return os;
    }
    
    /**
     * Expr : avg(left)
     * 
     * @param <A>
     * @param left
     * @return
     */    
    public static <A extends Number & Comparable<? super A>> ENumber<Double> avg(Expr<A> left){
        return createNumber(Double.class, OpNumberAgg.AVG, left);
    }
    
    /**
     * Expr : left < right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<? super A>> EBoolean before(Expr<A> left, A right) {
        // NOTE : signature is for Comparables to support other than Java's date types
        // NOTE : basically same as lt
        return createBoolean(Ops.BEFORE, left, createConstant(right));
    }
    
    /**
     * Expr : left <= right (before or equals)
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<? super A>> EBoolean boe(Expr<A> left, A right) {
        return createBoolean(Ops.BOE, left, createConstant(right));
    }

    /**
     * Expr : left < right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<? super A>> EBoolean before(Expr<A> left, Expr<A> right) {
        // NOTE : signature is for Comparables to support other than Java's date types
        // NOTE : basically same as lt
        return createBoolean(Ops.BEFORE, left, right);
    }
    
    /**
     * Expr : left <= right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<? super A>> EBoolean boe(Expr<A> left, Expr<A> right) {
        return createBoolean(Ops.BOE, left, right);
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
    public static <A extends Comparable<? super A>> EBoolean between(Expr<A> left, A start, A end) {
        return createBoolean(Ops.BETWEEN, left, createConstant(start), createConstant(end));
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
    public static <A extends Comparable<? super A>> EBoolean between(Expr<A> left, Expr<A> start, Expr<A> end) {
        return createBoolean(Ops.BETWEEN, left, start, end);
    }
        
    /**
     * Expr : left.chartAt(right) 
     * 
     * @param left
     * @param right
     * @return
     */
    public static Expr<Character> charAt(Expr<String> left, Expr<Integer> right) {
        return createComparable(Character.class, Ops.CHAR_AT, left, right);
    }
        
    /**
     * Expr : left.charAt(right)
     * 
     * @param left
     * @param right
     * @return
     */
    public static Expr<Character> charAt(Expr<String> left, int right) {
        return createComparable(Character.class, Ops.CHAR_AT, left, createConstant(right));
    }
    
    /**
     * Expr : left || right
     * 
     * @param left
     * @param right
     * @return
     */
    
    public static EString concat(Expr<String> left, Expr<String> right) {
        return createString(Ops.CONCAT, left, right);
    }

    /**
     * Expr : left || right
     * 
     * @param left
     * @param right
     * @return
     */
    public static EString concat(Expr<String> left, String right) {
        return createString(Ops.CONCAT, left, createConstant(right));
    }

    /**
     * Expr : left || right
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean contains(Expr<String> left, Expr<String> right) {
        return createBoolean(Ops.CONTAINS, left, right);
    }

    /**
     * Expr : left || right
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean contains(Expr<String> left, String right) {
        return createBoolean(Ops.CONTAINS, left, createConstant(right));
    }
                
    /**
     * Expr : count(*)
     * 
     * @return
     */
    public static EComparable<Long> count(){
        return new CountExpression(null);
    }
    
    /**
     * Expr : count(expr)
     * 
     * @param expr
     * @return
     */
    public static EComparable<Long> count(Expr<?> expr){
        return new CountExpression(expr);
    }
        
    /**
     * OrderSpecifier : desc target
     * 
     * @param <A>
     * @param target
     * @return
     */
    public static <A extends Comparable<? super A>> OrderSpecifier<A> desc(Expr<A> target) {
        OrderSpecifier<A> os = new OrderSpecifier<A>();
        os.order = Order.DESC;
        os.target = Assert.notNull(target);
        return os;
    }
        
    /**
     * Expr : left.endsWith(right)
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean endsWith(Expr<String> left, Expr<String> right) {
        return createBoolean(Ops.ENDSWITH, left, right);
    }

    /**
     * Expr : left.endsWith(right)
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean endsWith(Expr<String> left, String right) {
        return createBoolean(Ops.ENDSWITH, left, createConstant(right));
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
        if (isPrimitive(left.getType())){
            return createBoolean(Ops.EQ_PRIMITIVE, left, createConstant(right));
        }else{
            return createBoolean(Ops.EQ_OBJECT, left, createConstant(right));
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
        if (isPrimitive(left.getType())){
            return createBoolean(Ops.EQ_PRIMITIVE, left, right);
        }else{
            return createBoolean(Ops.EQ_OBJECT, left, right);
        }    
    }

    /**
     * Expr : left.lower() == right.lower()
     * 
     * @param left
     * @param right
     * @return
     */    
    public static EBoolean equalsIgnoreCase(Expr<String> left, Expr<String> right) {
        return createBoolean(Ops.EQ_IGNORECASE, left, right);
    }

    /**
     * Expr : left.lower() == right.lower()
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean equalsIgnoreCase(Expr<String> left, String right) {
        return createBoolean(Ops.EQ_IGNORECASE, left, createConstant(right));
    }
        
    /**
     * Expr : left >= right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<? super A>> EBoolean goe(Expr<A> left, A right) {
        return createBoolean(Ops.GOE, left, createConstant(right));
    }
    
    /**
     * Expr : left >= right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<? super A>> EBoolean goe(Expr<A> left, Expr<A> right) {
        return createBoolean(Ops.GOE, left, right);
    } 
    
    /**
     * Expr : left > right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<? super A>> EBoolean gt(Expr<A> left, A right) {
        return createBoolean(Ops.GT, left, createConstant(right));
    }
    
    /**
     * Expr : left > right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<? super A>> EBoolean gt(Expr<A> left, Expr<A> right) {
        return createBoolean(Ops.GT, left, right);
    }
    
    /**
     * Expr : left in right
     *         OR
     *        right contains left
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */    
    public static <A> EBoolean in(A left, CollectionType<? extends A> right){
        return createBoolean(Ops.IN, createConstant(left), (Expr<?>)right);
    }

    /**
     * Expr : left in right
     *         OR
     *        right contains left
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */    
    public static <A> EBoolean in(Expr<A> left, Collection<? extends A> right){
        return createBoolean(Ops.IN, left, createConstant(right));
    }
    
    /**
     * Expr : left in rest
     *         OR
     *        rest contains left 
     * 
     * @param <A>
     * @param left
     * @param rest
     * @return
     */    
    public static <A> EBoolean in(Expr<A> left, A... rest) {
        return createBoolean(Ops.IN, left, createConstant(rest));
    }
    
    /**
     * Expr : left in right
     *         OR
     *        right contains left
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A> EBoolean in(Expr<A> left, CollectionType<A> right){
        return createBoolean(Ops.IN, left, (Expr<?>)right);
    }
    
    /**
     * Expr : left.indexOf(right)
     * 
     * @param left
     * @param right
     * @return
     */
    public static EComparable<Integer> indexOf(Expr<String> left, Expr<String> right) {
        return createNumber(Integer.class,Ops.INDEXOF, left, right);
    }
     
    /**
     * Expr : left.indexOf(right)
     * 
     * @param left
     * @param right
     * @return
     */
    public static EComparable<Integer> indexOf(Expr<String> left, String right) {
        return createNumber(Integer.class,Ops.INDEXOF, left, createConstant(right));
    }

    /**
     * Expr : left.indexOf(right, i)
     * 
     * @param left
     * @param right
     * @param i
     * @return
     */    
    public static EComparable<Integer> indexOf(Expr<String> left, String right, int i) {
        return createNumber(Integer.class,Ops.INDEXOF_2ARGS, left, createConstant(right), createConstant(i));
    }

    /**
     * Expr : left.isEmpty()
     * 
     * @param left
     * @return
     */
    public static EBoolean isEmpty(Expr<String> left) {
        return createBoolean(Ops.ISEMPTY, left);
    }
    
    /**
     * Expr : left is not null 
     * 
     * @param <A>
     * @param left
     * @return
     */
    public static <A> EBoolean isnotnull(Expr<A> left) {
        return createBoolean(Ops.ISNOTNULL, left);
    }

    /**
     * Expr : left is null
     * 
     * @param <A>
     * @param left
     * @return
     */
    public static <A> EBoolean isnull(Expr<A> left) {
        return createBoolean(Ops.ISNULL, left);
    }

    private static boolean isPrimitive(Class<? extends Object> class1) {
        if (class1 == null) return false;
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
    public static EComparable<Integer> lastIndex(Expr<String> left, String right, int third) {
        return createNumber(Integer.class,Ops.LAST_INDEX_2ARGS, left, createConstant(right), createConstant(third));
    }
    
    /**
     * Expr : left.lastIndexOf(right)
     * 
     * @param left
     * @param right
     * @return
     */
    public static EComparable<Integer> lastIndexOf(Expr<String> left, Expr<String> right) {
        return createNumber(Integer.class,Ops.LAST_INDEX, left, right);
    }
    
    /**
     * Expr : left.lastIndexOf(right)
     * 
     * @param left
     * @param right
     * @return
     */
    public static EComparable<Integer> lastIndexOf(Expr<String> left, String right) {
        return createNumber(Integer.class,Ops.LAST_INDEX, left, createConstant(right));
    }

    /**
     * Expr : left.length()
     * 
     * @param left
     * @return
     */
    public static EComparable<Integer> length(Expr<String> left) {
        return createNumber(Integer.class,Ops.STRING_LENGTH, left);
    }
    
    /**
     * Expr : left like right
     * 
     * @param left
     * @param right
     * @return
     */    
    public static EBoolean like(Expr<String> left, String right) {                
        return createBoolean(Ops.LIKE, left, createConstant(right));
    }

    /**
     * Expr : left <= right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<? super A>> EBoolean loe(Expr<A> left, A right) {
        return createBoolean(Ops.LOE, left, createConstant(right));
    }
    
    /**
     * Expr : left <= right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<? super A>> EBoolean loe(Expr<A> left, Expr<A> right) {
        return createBoolean(Ops.LOE, left, right);
    }

    /**
     * Expr : left.toLowerCase()
     * 
     * @param left
     * @return
     */    
    public static EString lower(Expr<String> left) {
        return createString(Ops.LOWER, left);
    }
    
    /**
     * Expr : left < right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<? super A>> EBoolean lt(Expr<A> left, A right) {
        return createBoolean(Ops.LT, left, createConstant(right));
    }

    /**
     * Expr : left < right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */    
    public static <A extends Comparable<? super A>> EBoolean lt(Expr<A> left, Expr<A> right) {
        return createBoolean(Ops.LT, left, right);
    }

    /** 
     * Returns true if the left term matches the regex of the right term
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean matches(Expr<String> left, Expr<String> right) {
        return createBoolean(Ops.MATCHES, left, right);
    }
    
    /** 
     * Returns true if the left term matches the regex of the right term
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean matches(Expr<String> left, String right) {
        return createBoolean(Ops.MATCHES, left, createConstant(right));
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
        if (isPrimitive(left.getType())){
            return createBoolean(Ops.NE_PRIMITIVE, left, createConstant(right));
        }else{
            return createBoolean(Ops.NE_OBJECT, left, createConstant(right));
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
        if (isPrimitive(left.getType())){
            return createBoolean(Ops.NE_PRIMITIVE, left, right);
        }else{
            return createBoolean(Ops.NE_OBJECT, left, right);
        }    
    }

    /**
     * Expr !left
     * 
     * @param left
     * @return
     */
    public static EBoolean not(EBoolean left) {
        if (left instanceof OBoolean){
            OBoolean o = (OBoolean)left;
            if (o.getOperator() == Ops.NOT) return (EBoolean) o.getArg(0);
        }        
        return createBoolean(Ops.NOT, left);
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
    public static <A extends Comparable<? super A>> EBoolean notBetween(Expr<A> left, A start, A end) {
        return createBoolean(Ops.NOTBETWEEN, left, createConstant(start), createConstant(end));
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
    public static <A extends Comparable<? super A>> EBoolean notBetween(Expr<A> left, Expr<A> start, Expr<A> end) {
        return createBoolean(Ops.NOTBETWEEN, left, start, end);
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
        return createBoolean(Ops.NOTIN, left, createConstant(rest));
    }
    
    /**
     * Expr : left not in right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A> EBoolean notIn(Expr<A> left, CollectionType<? extends A> right){
        return createBoolean(Ops.NOTIN, left, (Expr<?>)right);
    }

    /**
     * Expr : cast(source as targetType)
     * 
     * @param <A>
     * @param source
     * @param targetType
     * @return
     */
    public static <A extends Number & Comparable<? super A>> ENumber<A> numericCast(EComparable<?> source, Class<A> targetType){
        if (targetType.isAssignableFrom(source.getType()) && ENumber.class.isAssignableFrom(source.getClass())){
            return (ENumber)source;
        }else{
            return createNumber(targetType, Ops.NUMCAST, source, createConstant(targetType));
        }
    }
    
    /**
     * Expr : cast(source as String)
     * 
     * @param source
     * @return
     */
    public static EString stringCast(EComparable<?> source){
        return createString(Ops.STRING_CAST, source);
    }
    
    /**
     * Expr : left or right
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean or(EBoolean left, EBoolean right) {
        return createBoolean(Ops.OR, left, right);
    }
    
    /**
     * Split the given String left with refex as the matcher for the separator
     * 
     * @param left
     * @param regex
     * @return
     */    
    public static Expr<String[]> split(Expr<String> left, String regex){
        return createStringArray(Ops.SPLIT, left, createConstant(regex));
    }
    
    /**
     * Expr : left.startsWith(right)
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean startsWith(Expr<String> left, Expr<String> right) {
        return createBoolean(Ops.STARTSWITH, left, right);
    }
    
    /**
     * Expr : left.startsWith(right)
     * 
     * @param left
     * @param right
     * @return
     */    
    public static EBoolean startsWith(Expr<String> left, String right) {
        return createBoolean(Ops.STARTSWITH, left, createConstant(right));
    }
    
    /**
     * Expr : left.substring(right)
     * 
     * @param left
     * @param right
     * @return
     */
    public static EString substring(Expr<String> left, int right) {
        return createString(Ops.SUBSTR1ARG, left, createConstant(right));
    }

    /**
     * Expr : left.substring(beginIndex, endIndex)
     * 
     * @param left
     * @param beginIndex
     * @param endIndex
     * @return
     */    
    public static EString substring(Expr<String> left, int beginIndex, int endIndex) {
        return createString(Ops.SUBSTR2ARGS, left, createConstant(beginIndex), createConstant(endIndex));
    }
    
    /**
     * Expr : left.trim()
     * 
     * @param left
     * @return
     */
    public static EString trim(Expr<String> left) {
        return createString(Ops.TRIM, left);
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
    public static <A, B extends A> EBoolean typeOf(Expr<A> left, Class<B> right) {
        return createBoolean(Ops.ISTYPEOF, left, createConstant(right));
    }    
    
    /**
     * Expr : left.toUpperCase()
     * 
     * @param left
     * @return
     */
    public static EString upper(Expr<String> left) {
        return createString(Ops.UPPER, left);
    }
}
