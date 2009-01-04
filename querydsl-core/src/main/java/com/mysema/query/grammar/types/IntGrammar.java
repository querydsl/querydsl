/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import static com.mysema.query.grammar.types.Factory.*;

import com.mysema.query.grammar.Ops;
import com.mysema.query.grammar.Order;
import com.mysema.query.grammar.OrderSpecifier;
import com.mysema.query.grammar.types.Expr.EBoolean;
import com.mysema.query.grammar.types.Expr.EComparable;

/**
 * InternalGrammar is the internal grammar for Expr instances
 *
 * @author tiwe
 * @version $Id$
 */
class IntGrammar{
    
    static <A extends Comparable<A>> Expr.EBoolean after(Expr<A> left,
            A right) {
        // NOTE : signature is for Comparables to support other than Java's date types
        return createBoolean(Ops.AFTER, left, createConstant(right));
    }
    
    static <A extends Comparable<A>> Expr.EBoolean after(Expr<A> left,
            Expr<A> right) {
        // NOTE : signature is for Comparables to support other than Java's date types
        return createBoolean(Ops.AFTER, left, right);
    }

    static Expr.EBoolean and(Expr.EBoolean left, Expr.EBoolean right) {
        return createBoolean(Ops.AND, left, right);
    }

    static <D> Alias.ASimple<D> as(Expr.ESimple<D> from, String to) {
        checkArg("from",from);
        checkArg("to",to);
        return new Alias.ASimple<D>(from, to);
    }

    static <D> Alias.AEntity<D> as(Path.PEntity<D> from, Path.PEntity<D> to) {
        checkArg("from",from);
        checkArg("to",to);
        return new Alias.AEntity<D>(from, to);
    }
    
    static <D> Alias.AEntityCollection<D> as(Path.PEntityCollection<D> from, Path.PEntity<D> to) {
        checkArg("from",from);
        checkArg("to",to);
        return new Alias.AEntityCollection<D>(from, to);
    }
    
    static <A extends Comparable<A>> OrderSpecifier<A> asc(Expr<A> target) {
        checkArg("target",target);
        OrderSpecifier<A> os = new OrderSpecifier<A>();
        os.order = Order.ASC;
        os.target = target;
        return os;
    }

    static <A extends Comparable<A>> Expr.EBoolean before(Expr<A> left,
            A right) {
        // NOTE : signature is for Comparables to support other than Java's date types
        // NOTE : basically same as lt
        return createBoolean(Ops.BEFORE, left, createConstant(right));
    }

    static <A extends Comparable<A>> Expr.EBoolean before(Expr<A> left,
            Expr<A> right) {
        // NOTE : signature is for Comparables to support other than Java's date types
        // NOTE : basically same as lt
        return createBoolean(Ops.BEFORE, left, right);
    }
    
    static <A extends Comparable<A>> Expr.EBoolean between(Expr<A> left,
            A start, A end) {
        return createBoolean(Ops.BETWEEN, left, createConstant(start), createConstant(end));
    }
    
    static <A extends Comparable<A>> Expr.EBoolean between(Expr<A> left,
            Expr<A> start, Expr<A> end) {
        return createBoolean(Ops.BETWEEN, left, start, end);
    }

    static Expr<Character> charAt(Expr<String> left, Expr<Integer> right) {
        return createComparable(Ops.CHAR_AT, left, right);
    }

    static Expr<Character> charAt(Expr<String> left, int right) {
        return createComparable(Ops.CHAR_AT, left, createConstant(right));
    }
    
    static Expr.EString concat(Expr<String> left, Expr<String> right) {
        return createString(Ops.CONCAT, left, right);
    }
    
    static Expr.EString concat(Expr<String> left, String right) {
        return createString(Ops.CONCAT, left, createConstant(right));
    }
        
    static EBoolean contains(Expr<String> left, Expr<String> right) {
        return createBoolean(Ops.CONTAINS, left, right);
    }
    
    static EBoolean contains(Expr<String> left, String right) {
        return createBoolean(Ops.CONTAINS, left, createConstant(right));
    }

    static <A extends Comparable<A>> OrderSpecifier<A> desc(
            Expr<A> target) {
        checkArg("target",target);
        OrderSpecifier<A> os = new OrderSpecifier<A>();
        os.order = Order.DESC;
        os.target = target;
        return os;
    }

    static EBoolean endsWith(Expr<String> left, Expr<String> right) {
        return createBoolean(Ops.ENDSWITH, left, right);
    }

    static EBoolean endsWith(Expr<String> left, String right) {
        return createBoolean(Ops.ENDSWITH, left, createConstant(right));
    }
                
    static <A> Expr.EBoolean eq(Expr<A> left, A right) {
        if (isPrimitive(left.getType())){
            return createBoolean(Ops.EQ_PRIMITIVE, left, createConstant(right));
        }else{
            return createBoolean(Ops.EQ_OBJECT, left, createConstant(right));
        }        
    }
    
    static <A> Expr.EBoolean eq(Expr<A> left, Expr<? super A> right) {
        if (isPrimitive(left.getType())){
            return createBoolean(Ops.EQ_PRIMITIVE, left, right);
        }else{
            return createBoolean(Ops.EQ_OBJECT, left, right);
        }    
    }
        
    static EBoolean equalsIgnoreCase(Expr<String> left, Expr<String> right) {
        return createBoolean(Ops.EQ_IGNORECASE, left, right);
    }
        
    static EBoolean equalsIgnoreCase(Expr<String> left, String right) {
        return createBoolean(Ops.EQ_IGNORECASE, left, createConstant(right));
    }

    static <A extends Comparable<A>> Expr.EBoolean goe(Expr<A> left,
            A right) {
        return createBoolean(Ops.GOE, left, createConstant(right));
    }

    static <A extends Comparable<A>> Expr.EBoolean goe(Expr<A> left,
            Expr<A> right) {
        return createBoolean(Ops.GOE, left, right);
    }

    static <A extends Comparable<A>> Expr.EBoolean gt(Expr<A> left, A right) {
        return createBoolean(Ops.GT, left, createConstant(right));
    }

    static <A extends Comparable<A>> Expr.EBoolean gt(Expr<A> left,
            Expr<A> right) {
        return createBoolean(Ops.GT, left, right);
    }

    static <A> Expr.EBoolean in(Expr<A> left,
            A... rest) {
        return createBoolean(Ops.IN, left, createConstant(rest));
    }

    static <A> Expr.EBoolean in(Expr<A> left, CollectionType<A> right){
        return createBoolean(Ops.IN, left, (Expr<?>)right);
    }

    static EComparable<Integer> indexOf(Expr<String> left, Expr<String> right) {
        return createNumber(Ops.INDEXOF, left, right);
    }
        
    static EComparable<Integer> indexOf(Expr<String> left, String right) {
        return createNumber(Ops.INDEXOF, left, createConstant(right));
    }
    
    static EComparable<Integer> indexOf(Expr<String> left, String right, int i) {
        return createNumber(Ops.INDEXOF_2ARGS, left, createConstant(right), createConstant(i));
    } 
    
    static EBoolean isEmpty(Expr<String> left) {
        return createBoolean(Ops.ISEMPTY, left);
    }

    static <A> Expr.EBoolean isnotnull(Expr<A> left) {
        return createBoolean(Ops.ISNOTNULL, left);
    }
    
    static <A> Expr.EBoolean isnull(Expr<A> left) {
        return createBoolean(Ops.ISNULL, left);
    }

    private static boolean isPrimitive(Class<? extends Object> class1) {
        if (class1 == null) return false;
        return class1.isPrimitive() || Number.class.isAssignableFrom(class1)
            || Boolean.class.equals(class1);
    }
    
    public static EComparable<Integer> lastIndex(Expr<String> left, String right, int third) {
        return createNumber(Ops.LAST_INDEX_2ARGS, left, createConstant(right), createConstant(third));
    }
    
    public static EComparable<Integer> lastIndexOf(Expr<String> left, Expr<String> right) {
        return createNumber(Ops.LAST_INDEX, left, right);
    }
      
    public static EComparable<Integer> lastIndexOf(Expr<String> left, String right) {
        return createNumber(Ops.LAST_INDEX, left, createConstant(right));
    }

    public static EComparable<Integer> length(Expr<String> left) {
        return createNumber(Ops.STRING_LENGTH, left);
    }

    static Expr.EBoolean like(Expr<String> left, String right) {
        return createBoolean(Ops.LIKE, left, createConstant(right));
    }
    
    static <A extends Comparable<A>> Expr.EBoolean loe(Expr<A> left,
            A right) {
        return createBoolean(Ops.LOE, left, createConstant(right));
    }

    static <A extends Comparable<A>> Expr.EBoolean loe(Expr<A> left,
            Expr<A> right) {
        return createBoolean(Ops.LOE, left, right);
    }

    static Expr.EString lower(Expr<String> left) {
        return createString(Ops.LOWER, left);
    }

    static <A extends Comparable<A>> Expr.EBoolean lt(Expr<A> left, A right) {
        return createBoolean(Ops.LT, left, createConstant(right));
    }
    
    static <A extends Comparable<A>> Expr.EBoolean lt(Expr<A> left,
            Expr<A> right) {
        return createBoolean(Ops.LT, left, right);
    }
    
    public static EBoolean matches(Expr<String> left, Expr<String> right) {
        return createBoolean(Ops.MATCHES, left, right);
    }

    public static EBoolean matches(Expr<String> left, String right) {
        return createBoolean(Ops.MATCHES, left, createConstant(right));
    }
    
    static <A> Expr.EBoolean ne(Expr<A> left, A right) {
        if (isPrimitive(left.getType())){
            return createBoolean(Ops.NE_PRIMITIVE, left, createConstant(right));
        }else{
            return createBoolean(Ops.NE_OBJECT, left, createConstant(right));
        }   
    }

    static <A> Expr.EBoolean ne(Expr<A> left, Expr<? super A> right) {
        if (isPrimitive(left.getType())){
            return createBoolean(Ops.NE_PRIMITIVE, left, right);
        }else{
            return createBoolean(Ops.NE_OBJECT, left, right);
        }    
    }
    
    static <A extends Comparable<A>> Expr.EBoolean notBetween(Expr<A> left,
            A start, A end) {
        return createBoolean(Ops.NOTBETWEEN, left, createConstant(start), createConstant(end));
    }

    static <A extends Comparable<A>> Expr.EBoolean notBetween(Expr<A> left,
            Expr<A> start, Expr<A> end) {
        return createBoolean(Ops.NOTBETWEEN, left, start, end);
    }
    
    static <A extends Comparable<A>> Expr.EBoolean notIn(Expr<A> left,
            A... rest) {
        return createBoolean(Ops.NOTIN, left, createConstant(rest));
    }

    static <A> Expr.EBoolean notIn(Expr<A> left, CollectionType<A> right){
        return createBoolean(Ops.NOTIN, left, (Expr<?>)right);
    }

    static Expr.EBoolean or(Expr.EBoolean left, Expr.EBoolean right) {
        return createBoolean(Ops.OR, left, right);
    }
    
    static Expr<String[]> split(Expr<String> left, String regex){
        return createStringArray(Ops.SPLIT, left, createConstant(regex));
    }

    static EBoolean startsWith(Expr<String> left, Expr<String> right) {
        return createBoolean(Ops.STARTSWITH, left, right);
    }

    static EBoolean startsWith(Expr<String> left, String right) {
        return createBoolean(Ops.STARTSWITH, left, createConstant(right));
    }
    
    static Expr.EString substring(Expr<String> left, int beginIndex) {
        return createString(Ops.SUBSTR1ARG, left, createConstant(beginIndex));
    }

    static Expr.EString substring(Expr<String> left, int beginIndex, int endIndex) {
        return createString(Ops.SUBSTR2ARGS, left, createConstant(beginIndex), createConstant(endIndex));
    }
    
    static Expr.EString trim(Expr<String> left) {
        return createString(Ops.TRIM, left);
    }

    static <A, B extends A> Expr.EBoolean typeOf(Expr<A> left, Class<B> right) {
        return createBoolean(Ops.ISTYPEOF, left, createConstant(right));
    }

    static Expr.EString upper(Expr<String> left) {
        return createString(Ops.UPPER, left);
    }
}
