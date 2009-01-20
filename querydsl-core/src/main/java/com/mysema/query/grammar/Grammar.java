/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;


import static com.mysema.query.grammar.types.Factory.*;

import com.mysema.query.grammar.Ops.OpNumberAgg;
import com.mysema.query.grammar.types.Alias;
import com.mysema.query.grammar.types.CollectionType;
import com.mysema.query.grammar.types.CountExpression;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Expr.EBoolean;
import com.mysema.query.grammar.types.Expr.EComparable;
import com.mysema.query.grammar.types.Expr.ENumber;
import com.mysema.query.grammar.types.Expr.ESimple;
import com.mysema.query.grammar.types.Expr.EString;
import com.mysema.query.grammar.types.Path.PEntity;
import com.mysema.query.grammar.types.Path.PEntityCollection;

/**
 * Grammar provides the factory methods for the fluent grammar.
 * 
 * @author tiwe
 * @version $Id$
 */
public class Grammar {
    
    protected Grammar(){};
    
    public static <A extends Comparable<A>> EBoolean after(Expr<A> left, A right) {
        // NOTE : signature is for Comparables to support other than Java's date types
        return createBoolean(Ops.AFTER, left, createConstant(right));
    }

    public static <A extends Comparable<A>> EBoolean after(Expr<A> left, Expr<A> right) {
        // NOTE : signature is for Comparables to support other than Java's date types
        return createBoolean(Ops.AFTER, left, right);
    }

    public static EBoolean and(EBoolean left, EBoolean right) {
        return createBoolean(Ops.AND, left, right);
    }
    
    public static <D> Alias.ASimple<D> as(ESimple<D> from, String to) {
        checkArg("from",from);
        checkArg("to",to);
        return new Alias.ASimple<D>(from, to);
    }
    
    public static <D> Alias.AEntity<D> as(PEntity<D> from, PEntity<D> to) {
        checkArg("from",from);
        checkArg("to",to);
        return new Alias.AEntity<D>(from, to);
    }

    public static <D> Alias.AEntityCollection<D> as(PEntityCollection<D> from, PEntity<D> to) {
        checkArg("from",from);
        checkArg("to",to);
        return new Alias.AEntityCollection<D>(from, to);
    }

    public static <A extends Comparable<A>> OrderSpecifier<A> asc(Expr<A> target) {
        checkArg("target",target);
        OrderSpecifier<A> os = new OrderSpecifier<A>();
        os.order = Order.ASC;
        os.target = target;
        return os;
    }
    
    public static <A extends Number & Comparable<A>> ENumber<Double> avg(Expr<A> left){
        return createNumber(Double.class, OpNumberAgg.AVG, left);
    }
    
    public static <A extends Comparable<A>> EBoolean before(Expr<A> left, A right) {
        // NOTE : signature is for Comparables to support other than Java's date types
        // NOTE : basically same as lt
        return createBoolean(Ops.BEFORE, left, createConstant(right));
    }

    public static <A extends Comparable<A>> EBoolean before(Expr<A> left, Expr<A> right) {
        // NOTE : signature is for Comparables to support other than Java's date types
        // NOTE : basically same as lt
        return createBoolean(Ops.BEFORE, left, right);
    }

    public static <A extends Comparable<A>> EBoolean between(Expr<A> left, A start, A end) {
        return createBoolean(Ops.BETWEEN, left, createConstant(start), createConstant(end));
    }
    
    public static <A extends Comparable<A>> EBoolean between(Expr<A> left, Expr<A> start, Expr<A> end) {
        return createBoolean(Ops.BETWEEN, left, start, end);
    }
    
    public static Expr<Character> charAt(Expr<String> left, Expr<Integer> right) {
        return createComparable(Character.class, Ops.CHAR_AT, left, right);
    }
        
    public static Expr<Character> charAt(Expr<String> left, int right) {
        return createComparable(Character.class, Ops.CHAR_AT, left, createConstant(right));
    }
    
    public static EString concat(Expr<String> left, Expr<String> right) {
        return createString(Ops.CONCAT, left, right);
    }

    public static EString concat(Expr<String> left, String right) {
        return createString(Ops.CONCAT, left, createConstant(right));
    }

    public static EBoolean contains(Expr<String> left, Expr<String> right) {
        return createBoolean(Ops.CONTAINS, left, right);
    }

    public static EBoolean contains(Expr<String> left, String right) {
        return createBoolean(Ops.CONTAINS, left, createConstant(right));
    }
                
    public static EComparable<Long> count(){
        return new CountExpression(null);
    }
    
    public static EComparable<Long> count(Expr<?> expr){
        return new CountExpression(expr);
    }
        
    public static <A extends Comparable<A>> OrderSpecifier<A> desc(Expr<A> target) {
        checkArg("target",target);
        OrderSpecifier<A> os = new OrderSpecifier<A>();
        os.order = Order.DESC;
        os.target = target;
        return os;
    }
        
    public static EBoolean endsWith(Expr<String> left, Expr<String> right) {
        return createBoolean(Ops.ENDSWITH, left, right);
    }

    public static EBoolean endsWith(Expr<String> left, String right) {
        return createBoolean(Ops.ENDSWITH, left, createConstant(right));
    }

    public static <A> EBoolean eq(Expr<A> left, A right) {
        if (isPrimitive(left.getType())){
            return createBoolean(Ops.EQ_PRIMITIVE, left, createConstant(right));
        }else{
            return createBoolean(Ops.EQ_OBJECT, left, createConstant(right));
        }        
    }

    public static <A> EBoolean eq(Expr<A> left, Expr<? super A> right) {
        if (isPrimitive(left.getType())){
            return createBoolean(Ops.EQ_PRIMITIVE, left, right);
        }else{
            return createBoolean(Ops.EQ_OBJECT, left, right);
        }    
    }

    public static EBoolean equalsIgnoreCase(Expr<String> left, Expr<String> right) {
        return createBoolean(Ops.EQ_IGNORECASE, left, right);
    }

    public static EBoolean equalsIgnoreCase(Expr<String> left, String right) {
        return createBoolean(Ops.EQ_IGNORECASE, left, createConstant(right));
    }
        
    public static <A extends Comparable<A>> EBoolean goe(Expr<A> left, A right) {
        return createBoolean(Ops.GOE, left, createConstant(right));
    }
    
    public static <A extends Comparable<A>> EBoolean goe(Expr<A> left, Expr<A> right) {
        return createBoolean(Ops.GOE, left, right);
    } 
    
    public static <A extends Comparable<A>> EBoolean gt(Expr<A> left, A right) {
        return createBoolean(Ops.GT, left, createConstant(right));
    }
    
    public static <A extends Comparable<A>> EBoolean gt(Expr<A> left, Expr<A> right) {
        return createBoolean(Ops.GT, left, right);
    }
    
    public static <A> EBoolean in(A left, CollectionType<A> right){
        return createBoolean(Ops.IN, createConstant(left), (Expr<?>)right);
    }

    public static <A> EBoolean in(Expr<A> left, A... rest) {
        return createBoolean(Ops.IN, left, createConstant(rest));
    }
    
    public static <A> EBoolean in(Expr<A> left, CollectionType<A> right){
        return createBoolean(Ops.IN, left, (Expr<?>)right);
    }
    
    public static EComparable<Integer> indexOf(Expr<String> left, Expr<String> right) {
        return createNumber(Integer.class,Ops.INDEXOF, left, right);
    }
      
    public static EComparable<Integer> indexOf(Expr<String> left, String right) {
        return createNumber(Integer.class,Ops.INDEXOF, left, createConstant(right));
    }

    public static EComparable<Integer> indexOf(Expr<String> left, String right, int i) {
        return createNumber(Integer.class,Ops.INDEXOF_2ARGS, left, createConstant(right), createConstant(i));
    }

    public static EBoolean isEmpty(Expr<String> left) {
        return createBoolean(Ops.ISEMPTY, left);
    }
    
    public static <A> EBoolean isnotnull(Expr<A> left) {
        return createBoolean(Ops.ISNOTNULL, left);
    }

    public static <A> EBoolean isnull(Expr<A> left) {
        return createBoolean(Ops.ISNULL, left);
    }

    private static boolean isPrimitive(Class<? extends Object> class1) {
        if (class1 == null) return false;
        return class1.isPrimitive() || Number.class.isAssignableFrom(class1)
            || Boolean.class.equals(class1);
    }

    public static EComparable<Integer> lastIndex(Expr<String> left, String right, int third) {
        return createNumber(Integer.class,Ops.LAST_INDEX_2ARGS, left, createConstant(right), createConstant(third));
    }
    
    public static EComparable<Integer> lastIndexOf(Expr<String> left, Expr<String> right) {
        return createNumber(Integer.class,Ops.LAST_INDEX, left, right);
    }
    
    public static EComparable<Integer> lastIndexOf(Expr<String> left, String right) {
        return createNumber(Integer.class,Ops.LAST_INDEX, left, createConstant(right));
    }

    public static EComparable<Integer> length(Expr<String> left) {
        return createNumber(Integer.class,Ops.STRING_LENGTH, left);
    }
    
    public static EBoolean like(Expr<String> left, String right) {
        return createBoolean(Ops.LIKE, left, createConstant(right));
    }

    public static <A extends Comparable<A>> EBoolean loe(Expr<A> left, A right) {
        return createBoolean(Ops.LOE, left, createConstant(right));
    }
    
    public static <A extends Comparable<A>> EBoolean loe(Expr<A> left, Expr<A> right) {
        return createBoolean(Ops.LOE, left, right);
    }

    public static EString lower(Expr<String> left) {
        return createString(Ops.LOWER, left);
    }
    
    public static <A extends Comparable<A>> EBoolean lt(Expr<A> left, A right) {
        return createBoolean(Ops.LT, left, createConstant(right));
    }

    public static <A extends Comparable<A>> EBoolean lt(Expr<A> left, Expr<A> right) {
        return createBoolean(Ops.LT, left, right);
    }

    public static EBoolean matches(Expr<String> left, Expr<String> right) {
        return createBoolean(Ops.MATCHES, left, right);
    }
    
    public static EBoolean matches(Expr<String> left, String right) {
        return createBoolean(Ops.MATCHES, left, createConstant(right));
    }
    
    public static <A> EBoolean ne(Expr<A> left, A right) {
        if (isPrimitive(left.getType())){
            return createBoolean(Ops.NE_PRIMITIVE, left, createConstant(right));
        }else{
            return createBoolean(Ops.NE_OBJECT, left, createConstant(right));
        }   
    }

    public static <A> EBoolean ne(Expr<A> left, Expr<? super A> right) {
        if (isPrimitive(left.getType())){
            return createBoolean(Ops.NE_PRIMITIVE, left, right);
        }else{
            return createBoolean(Ops.NE_OBJECT, left, right);
        }    
    }

    public static EBoolean not(EBoolean left) {
        return createBoolean(Ops.NOT, left);
    }
       
    public static <A extends Comparable<A>> EBoolean notBetween(Expr<A> left, A start, A end) {
        return createBoolean(Ops.NOTBETWEEN, left, createConstant(start), createConstant(end));
    }
    
    public static <A extends Comparable<A>> EBoolean notBetween(Expr<A> left, Expr<A> start, Expr<A> end) {
        return createBoolean(Ops.NOTBETWEEN, left, start, end);
    }
    
    public static <A extends Comparable<A>> EBoolean notIn(Expr<A> left, A... rest) {
        return createBoolean(Ops.NOTIN, left, createConstant(rest));
    }
    
    public static <A> EBoolean notIn(Expr<A> left, CollectionType<A> right){
        return createBoolean(Ops.NOTIN, left, (Expr<?>)right);
    }

    public static EBoolean or(EBoolean left, EBoolean right) {
        return createBoolean(Ops.OR, left, right);
    }
    
    public static Expr<String[]> split(Expr<String> left, String regex){
        return createStringArray(Ops.SPLIT, left, createConstant(regex));
    }
    
    public static EBoolean startsWith(Expr<String> left, Expr<String> right) {
        return createBoolean(Ops.STARTSWITH, left, right);
    }
    
    public static EBoolean startsWith(Expr<String> left, String right) {
        return createBoolean(Ops.STARTSWITH, left, createConstant(right));
    }
    
    public static EString substring(Expr<String> left, int beginIndex) {
        return createString(Ops.SUBSTR1ARG, left, createConstant(beginIndex));
    }

    public static EString substring(Expr<String> left, int beginIndex, int endIndex) {
        return createString(Ops.SUBSTR2ARGS, left, createConstant(beginIndex), createConstant(endIndex));
    }
    
    public static EString trim(Expr<String> left) {
        return createString(Ops.TRIM, left);
    }    

    public static <A, B extends A> EBoolean typeOf(Expr<A> left, Class<B> right) {
        return createBoolean(Ops.ISTYPEOF, left, createConstant(right));
    }    
    
    public static EString upper(Expr<String> left) {
        return createString(Ops.UPPER, left);
    }
}
