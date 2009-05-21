/*
 * Copyright (c) 2008 Mysema Ltd.
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
import com.mysema.query.types.expr.ExprFactory;
import com.mysema.query.types.expr.SimpleExprFactory;
import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.operation.Ops.OpNumberAgg;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PEntityCollection;

/**
 * Grammar provides the factory methods for the fluent grammar.
 * 
 * @author tiwe
 * @version $Id$
 */
public class Grammar{
    
    protected static final ExprFactory factory = SimpleExprFactory.getInstance();
    
    protected Grammar(){};
        
    /**
     * Expr : left > right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean after(Expr<A> left, A right) {
        // NOTE : signature is for Comparables to support other than Java's date types
        return factory.createBoolean(Ops.AFTER, left, factory.createConstant(right));
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
        return factory.createBoolean(Ops.AOE, left, factory.createConstant(right));
    }

    /**
     * Expr : left > right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    
    public static <A extends Comparable<?>> EBoolean after(Expr<A> left, Expr<A> right) {
        // NOTE : signature is for Comparables to support other than Java's date types
        return factory.createBoolean(Ops.AFTER, left, right);
    }
    
    /**
     * Expr : left >= right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean aoe(Expr<A> left, Expr<A> right) {
        return factory.createBoolean(Ops.AOE, left, right);
    }

    /**
     * Expr : left and right
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean and(EBoolean left, EBoolean right) {
        return factory.createBoolean(Ops.AND, left, right);
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
    public static <D> AEntityCollection<D> as(PEntityCollection<D> from, PEntity<D> to) {
        return new AEntityCollection<D>(Assert.notNull(from), Assert.notNull(to));
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
    public static <A extends Number & Comparable<?>> ENumber<Double> avg(Expr<A> left){
        return factory.createNumber(Double.class, OpNumberAgg.AVG_AGG, left);
    }
    
    /**
     * Expr : left < right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean before(Expr<A> left, A right) {
        // NOTE : signature is for Comparables to support other than Java's date types
        // NOTE : basically same as lt
        return factory.createBoolean(Ops.BEFORE, left, factory.createConstant(right));
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
        return factory.createBoolean(Ops.BOE, left, factory.createConstant(right));
    }

    /**
     * Expr : left < right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean before(Expr<A> left, Expr<A> right) {
        // NOTE : signature is for Comparables to support other than Java's date types
        // NOTE : basically same as lt
        return factory.createBoolean(Ops.BEFORE, left, right);
    }
    
    /**
     * Expr : left <= right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean boe(Expr<A> left, Expr<A> right) {
        return factory.createBoolean(Ops.BOE, left, right);
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
    public static <A extends Comparable<?>> EBoolean between(Expr<A> left, A start, A end) {
        return factory.createBoolean(Ops.BETWEEN, left, factory.createConstant(start), factory.createConstant(end));
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
    public static <A extends Comparable<?>> EBoolean between(Expr<A> left, Expr<A> start, Expr<A> end) {
        return factory.createBoolean(Ops.BETWEEN, left, start, end);
    }
        
    /**
     * Expr : left.chartAt(right) 
     * 
     * @param left
     * @param right
     * @return
     */
    public static Expr<Character> charAt(Expr<String> left, Expr<Integer> right) {
        return factory.createComparable(Character.class, Ops.CHAR_AT, left, right);
    }
        
    /**
     * Expr : left.charAt(right)
     * 
     * @param left
     * @param right
     * @return
     */
    public static Expr<Character> charAt(Expr<String> left, int right) {
        return factory.createComparable(Character.class, Ops.CHAR_AT, left, factory.createConstant(right));
    }
    
    /**
     * Expr : left || right
     * 
     * @param left
     * @param right
     * @return
     */
    
    public static EString concat(Expr<String> left, Expr<String> right) {
        return factory.createString(Ops.CONCAT, left, right);
    }

    /**
     * Expr : left || right
     * 
     * @param left
     * @param right
     * @return
     */
    public static EString concat(Expr<String> left, String right) {
        return factory.createString(Ops.CONCAT, left, factory.createConstant(right));
    }

    /**
     * Expr : left || right
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean contains(Expr<String> left, Expr<String> right) {
        return factory.createBoolean(Ops.CONTAINS, left, right);
    }

    /**
     * Expr : left || right
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean contains(Expr<String> left, String right) {
        return factory.createBoolean(Ops.CONTAINS, left, factory.createConstant(right));
    }
                
    /**
     * Expr : count(*)
     * 
     * @return
     */
    public static ENumber<Long> count(){
        return new CountExpression(null);
    }
    
    /**
     * Expr : count(expr)
     * 
     * @param expr
     * @return
     */
    public static ENumber<Long> count(Expr<?> expr){
        return new CountExpression(expr);
    }
        
    /**
     * OrderSpecifier : desc target
     * 
     * @param <A>
     * @param target
     * @return
     */
    public static <A extends Comparable<?>> OrderSpecifier<A> desc(Expr<A> target) {
        return new OrderSpecifier<A>(Order.DESC, target);
    }
        
    /**
     * Expr : left.endsWith(right)
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean endsWith(Expr<String> left, Expr<String> right) {
        return factory.createBoolean(Ops.ENDSWITH, left, right);
    }
    
    /**
     * Expr : left.endsWith(right) (ignore case)
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean endsWith(Expr<String> left, Expr<String> right, boolean caseSensitive) {
        if (caseSensitive){
            return endsWith(left, right);            
        }else{
            return factory.createBoolean(Ops.ENDSWITH_IC, left, right);
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
        return factory.createBoolean(Ops.ENDSWITH, left, factory.createConstant(right));
    }

    /**
     * Expr : left.endsWith(right) (ignore case)
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean endsWith(Expr<String> left, String right, boolean caseSensitive) {
        if (caseSensitive){
            return endsWith(left, right);            
        }else{
            return factory.createBoolean(Ops.ENDSWITH_IC, left, factory.createConstant(right));
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
        if (isPrimitive(left.getType())){
            return factory.createBoolean(Ops.EQ_PRIMITIVE, left, factory.createConstant(right));
        }else{
            return factory.createBoolean(Ops.EQ_OBJECT, left, factory.createConstant(right));
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
            return factory.createBoolean(Ops.EQ_PRIMITIVE, left, right);
        }else{
            return factory.createBoolean(Ops.EQ_OBJECT, left, right);
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
        return factory.createBoolean(Ops.EQ_IGNORECASE, left, right);
    }

    /**
     * Expr : left.lower() == right.lower()
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean equalsIgnoreCase(Expr<String> left, String right) {
        return factory.createBoolean(Ops.EQ_IGNORECASE, left, factory.createConstant(right));
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
        return factory.createBoolean(Ops.GOE, left, factory.createConstant(right));
    }
    
    /**
     * Expr : left >= right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean goe(Expr<A> left, Expr<A> right) {
        return factory.createBoolean(Ops.GOE, left, right);
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
        return factory.createBoolean(Ops.GT, left, factory.createConstant(right));
    }
    
    /**
     * Expr : left > right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean gt(Expr<A> left, Expr<A> right) {
        return factory.createBoolean(Ops.GT, left, right);
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
        return factory.createBoolean(Ops.IN, factory.createConstant(left), (Expr<?>)right);
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
        return factory.createBoolean(Ops.IN, left, factory.createConstant(right));
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
        return factory.createBoolean(Ops.IN, left, factory.createConstant(rest));
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
        return factory.createBoolean(Ops.IN, left, (Expr<?>)right);
    }
    
    /**
     * Expr : left.indexOf(right)
     * 
     * @param left
     * @param right
     * @return
     */
    public static ENumber<Integer> indexOf(Expr<String> left, Expr<String> right) {
        return factory.createNumber(Integer.class,Ops.INDEXOF, left, right);
    }
     
    /**
     * Expr : left.indexOf(right)
     * 
     * @param left
     * @param right
     * @return
     */
    public static ENumber<Integer> indexOf(Expr<String> left, String right) {
        return factory.createNumber(Integer.class,Ops.INDEXOF, left, factory.createConstant(right));
    }

    /**
     * Expr : left.indexOf(right, i)
     * 
     * @param left
     * @param right
     * @param i
     * @return
     */    
    public static ENumber<Integer> indexOf(Expr<String> left, String right, int i) {
        return factory.createNumber(Integer.class,Ops.INDEXOF_2ARGS, left, factory.createConstant(right), factory.createConstant(i));
    }

    /**
     * Expr : left.isEmpty()
     * 
     * @param left
     * @return
     */
    public static EBoolean isEmpty(Expr<String> left) {
        return factory.createBoolean(Ops.ISEMPTY, left);
    }
    
    /**
     * Expr : left is not null 
     * 
     * @param <A>
     * @param left
     * @return
     */
    public static <A> EBoolean isnotnull(Expr<A> left) {
        return factory.createBoolean(Ops.ISNOTNULL, left);
    }

    /**
     * Expr : left is null
     * 
     * @param <A>
     * @param left
     * @return
     */
    public static <A> EBoolean isnull(Expr<A> left) {
        return factory.createBoolean(Ops.ISNULL, left);
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
    public static ENumber<Integer> lastIndex(Expr<String> left, String right, int third) {
        return factory.createNumber(Integer.class,Ops.LAST_INDEX_2ARGS, left, factory.createConstant(right), factory.createConstant(third));
    }
    
    /**
     * Expr : left.lastIndexOf(right)
     * 
     * @param left
     * @param right
     * @return
     */
    public static ENumber<Integer> lastIndexOf(Expr<String> left, Expr<String> right) {
        return factory.createNumber(Integer.class,Ops.LAST_INDEX, left, right);
    }
    
    /**
     * Expr : left.lastIndexOf(right)
     * 
     * @param left
     * @param right
     * @return
     */
    public static ENumber<Integer> lastIndexOf(Expr<String> left, String right) {
        return factory.createNumber(Integer.class,Ops.LAST_INDEX, left, factory.createConstant(right));
    }

    /**
     * Expr : left.length()
     * 
     * @param left
     * @return
     */
    public static ENumber<Integer> length(Expr<String> left) {
        return factory.createNumber(Integer.class,Ops.STRING_LENGTH, left);
    }
    
    /**
     * Expr : left like right
     * 
     * @param left
     * @param right
     * @return
     */    
    public static EBoolean like(Expr<String> left, String right) {                
        return factory.createBoolean(Ops.LIKE, left, factory.createConstant(right));
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
        return factory.createBoolean(Ops.LOE, left, factory.createConstant(right));
    }
    
    /**
     * Expr : left <= right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */
    public static <A extends Comparable<?>> EBoolean loe(Expr<A> left, Expr<A> right) {
        return factory.createBoolean(Ops.LOE, left, right);
    }

    /**
     * Expr : left.toLowerCase()
     * 
     * @param left
     * @return
     */    
    public static EString lower(Expr<String> left) {
        return factory.createString(Ops.LOWER, left);
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
        return factory.createBoolean(Ops.LT, left, factory.createConstant(right));
    }

    /**
     * Expr : left < right
     * 
     * @param <A>
     * @param left
     * @param right
     * @return
     */    
    public static <A extends Comparable<?>> EBoolean lt(Expr<A> left, Expr<A> right) {
        return factory.createBoolean(Ops.LT, left, right);
    }

    /** 
     * Returns true if the left term matches the regex of the right term
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean matches(Expr<String> left, Expr<String> right) {
        return factory.createBoolean(Ops.MATCHES, left, right);
    }
    
    /** 
     * Returns true if the left term matches the regex of the right term
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean matches(Expr<String> left, String right) {
        return factory.createBoolean(Ops.MATCHES, left, factory.createConstant(right));
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
            return factory.createBoolean(Ops.NE_PRIMITIVE, left, factory.createConstant(right));
        }else{
            return factory.createBoolean(Ops.NE_OBJECT, left, factory.createConstant(right));
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
            return factory.createBoolean(Ops.NE_PRIMITIVE, left, right);
        }else{
            return factory.createBoolean(Ops.NE_OBJECT, left, right);
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
        return factory.createBoolean(Ops.NOT, left);
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
    public static <A extends Comparable<?>> EBoolean notBetween(Expr<A> left, A start, A end) {
        return factory.createBoolean(Ops.NOTBETWEEN, left, factory.createConstant(start), factory.createConstant(end));
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
    public static <A extends Comparable<?>> EBoolean notBetween(Expr<A> left, Expr<A> start, Expr<A> end) {
        return factory.createBoolean(Ops.NOTBETWEEN, left, start, end);
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
        return factory.createBoolean(Ops.NOTIN, left, factory.createConstant(rest));
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
        return factory.createBoolean(Ops.NOTIN, left, (Expr<?>)right);
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
    public static <A extends Number & Comparable<?>> ENumber<A> numericCast(EComparable<?> source, Class<A> targetType){
        if (targetType.isAssignableFrom(source.getType()) && ENumber.class.isAssignableFrom(source.getClass())){
            return (ENumber)source;
        }else{
            return factory.createNumber(targetType, Ops.NUMCAST, source, factory.createConstant(targetType));
        }
    }
    
    /**
     * Expr : cast(source as String)
     * 
     * @param source
     * @return
     */
    public static EString stringCast(EComparable<?> source){
        return factory.createString(Ops.STRING_CAST, source);
    }
    
    /**
     * Expr : left or right
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean or(EBoolean left, EBoolean right) {
        return factory.createBoolean(Ops.OR, left, right);
    }
    
    /**
     * Split the given String left with refex as the matcher for the separator
     * 
     * @param left
     * @param regex
     * @return
     */    
    public static Expr<String[]> split(Expr<String> left, String regex){
        return factory.createStringArray(Ops.SPLIT, left, factory.createConstant(regex));
    }
    
    /**
     * Expr : left.startsWith(right)
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean startsWith(Expr<String> left, Expr<String> right) {
        return factory.createBoolean(Ops.STARTSWITH, left, right);
    }
    
    /**
     * Expr : left.startsWith(right) (ignore case)
     * 
     * @param left
     * @param right
     * @return
     */
    public static EBoolean startsWith(Expr<String> left, Expr<String> right, boolean caseSensitive) {
        if (caseSensitive){
            return startsWith(left, right);            
        }else{
            return factory.createBoolean(Ops.STARTSWITH_IC, left, right);
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
        return factory.createBoolean(Ops.STARTSWITH, left, factory.createConstant(right));
    }
    
    /**
     * Expr : left.startsWith(right) (ignore case)
     * 
     * @param left
     * @param right
     * @return
     */     
    public static EBoolean startsWith(Expr<String> left, String right, boolean caseSensitive) {
        if (caseSensitive){
            return startsWith(left, right);            
        }else{
            return factory.createBoolean(Ops.STARTSWITH_IC, left, factory.createConstant(right));    
        }        
    }
    
    /**
     * Expr : left.substring(right)
     * 
     * @param left
     * @param right
     * @return
     */
    public static EString substring(Expr<String> left, int right) {
        return factory.createString(Ops.SUBSTR1ARG, left, factory.createConstant(right));
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
        return factory.createString(Ops.SUBSTR2ARGS, left, factory.createConstant(beginIndex), factory.createConstant(endIndex));
    }
    
    /**
     * Expr : left.trim()
     * 
     * @param left
     * @return
     */
    public static EString trim(Expr<String> left) {
        return factory.createString(Ops.TRIM, left);
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
        return factory.createBoolean(Ops.ISTYPEOF, left, factory.createConstant(right));
    }    
    
    /**
     * Expr : left.toUpperCase()
     * 
     * @param left
     * @return
     */
    public static EString upper(Expr<String> left) {
        return factory.createString(Ops.UPPER, left);
    }
}
