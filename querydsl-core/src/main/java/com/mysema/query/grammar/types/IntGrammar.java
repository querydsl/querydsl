/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import static com.mysema.query.grammar.types.Factory.checkArg;
import static com.mysema.query.grammar.types.Factory.createBoolean;
import static com.mysema.query.grammar.types.Factory.createConstant;
import static com.mysema.query.grammar.types.Factory.createString;

import com.mysema.query.grammar.Ops;
import com.mysema.query.grammar.Order;
import com.mysema.query.grammar.OrderSpecifier;

/**
 * InternalGrammar is the internal grammar for Expr instances
 *
 * @author tiwe
 * @version $Id$
 */
class IntGrammar{
   
    static <A extends Comparable<A>> Expr.Boolean after(Expr<A> left,
            A right) {
        // NOTE : signature is for Comparables to support other than Java's date types
        return createBoolean(Ops.AFTER, left, createConstant(right));
    }

    static <A extends Comparable<A>> Expr.Boolean after(Expr<A> left,
            Expr<A> right) {
        // NOTE : signature is for Comparables to support other than Java's date types
        return createBoolean(Ops.AFTER, left, right);
    }

    static Expr.Boolean and(Expr.Boolean left, Expr.Boolean right) {
        return createBoolean(Ops.AND, left, right);
    }

    static <D> Alias.Simple<D> as(Expr.Simple<D> from, String to) {
        checkArg("from",from);
        checkArg("to",to);
        return new Alias.Simple<D>(from, to);
    }
    
    static <D> Alias.Entity<D> as(Path.Entity<D> from, Path.Entity<D> to) {
        checkArg("from",from);
        checkArg("to",to);
        return new Alias.Entity<D>(from, to);
    }
    
    static <D> Alias.EntityCollection<D> as(Path.EntityCollection<D> from, Path.Entity<D> to) {
        checkArg("from",from);
        checkArg("to",to);
        return new Alias.EntityCollection<D>(from, to);
    }

    static <A extends Comparable<A>> OrderSpecifier<A> asc(Expr<A> target) {
        checkArg("target",target);
        OrderSpecifier<A> os = new OrderSpecifier<A>();
        os.order = Order.ASC;
        os.target = target;
        return os;
    }

    static <A extends Comparable<A>> Expr.Boolean before(Expr<A> left,
            A right) {
        // NOTE : signature is for Comparables to support other than Java's date types
        // NOTE : basically same as lt
        return createBoolean(Ops.BEFORE, left, createConstant(right));
    }
    
    static <A extends Comparable<A>> Expr.Boolean before(Expr<A> left,
            Expr<A> right) {
        // NOTE : signature is for Comparables to support other than Java's date types
        // NOTE : basically same as lt
        return createBoolean(Ops.BEFORE, left, right);
    }
    
    static <A extends Comparable<A>> Expr.Boolean between(Expr<A> left,
            A start, A end) {
        return createBoolean(Ops.BETWEEN, left, createConstant(start), createConstant(end));
    }

    static <A extends Comparable<A>> Expr.Boolean between(Expr<A> left,
            Expr<A> start, Expr<A> end) {
        return createBoolean(Ops.BETWEEN, left, start, end);
    }

    static Expr.String concat(Expr<String> left, Expr<String> right) {
        return createString(Ops.CONCAT, left, right);
    }
    
    static Expr.String concat(Expr<String> left, String right) {
        return createString(Ops.CONCAT, left, createConstant(right));
    }
    
    static <A extends Comparable<A>> OrderSpecifier<A> desc(
            Expr<A> target) {
        checkArg("target",target);
        OrderSpecifier<A> os = new OrderSpecifier<A>();
        os.order = Order.DESC;
        os.target = target;
        return os;
    }
        
    static <A> Expr.Boolean eq(Expr<A> left, A right) {
        return createBoolean(Ops.EQ, left, createConstant(right));
    }

    static <A> Expr.Boolean eq(Expr<A> left, Expr<? super A> right) {
        return createBoolean(Ops.EQ, left, right);
    }

    static <A extends Comparable<A>> Expr.Boolean goe(Expr<A> left,
            A right) {
        return createBoolean(Ops.GOE, left, createConstant(right));
    }

    static <A extends Comparable<A>> Expr.Boolean goe(Expr<A> left,
            Expr<A> right) {
        return createBoolean(Ops.GOE, left, right);
    }

    static <A extends Comparable<A>> Expr.Boolean gt(Expr<A> left, A right) {
        return createBoolean(Ops.GT, left, createConstant(right));
    }
                
    static <A extends Comparable<A>> Expr.Boolean gt(Expr<A> left,
            Expr<A> right) {
        return createBoolean(Ops.GT, left, right);
    }
    
    static <A> Expr.Boolean in(Expr<A> left,
            A... rest) {
        return createBoolean(Ops.IN, left, createConstant(rest));
    }
        
    static <A> Expr.Boolean in(Expr<A> left, CollectionType<A> right){
        return createBoolean(Ops.IN, left, (Expr<?>)right);
    }
        
    static <A> Expr.Boolean isnotnull(Expr<A> left) {
        return createBoolean(Ops.ISNOTNULL, left);
    }

    static <A> Expr.Boolean isnull(Expr<A> left) {
        return createBoolean(Ops.ISNULL, left);
    }

    static Expr.Boolean like(Expr<String> left, String right) {
        return createBoolean(Ops.LIKE, left, createConstant(right));
    }

    static <A extends Comparable<A>> Expr.Boolean loe(Expr<A> left,
            A right) {
        return createBoolean(Ops.LOE, left, createConstant(right));
    }

    static <A extends Comparable<A>> Expr.Boolean loe(Expr<A> left,
            Expr<A> right) {
        return createBoolean(Ops.LOE, left, right);
    }

    static Expr.String lower(Expr<String> left) {
        return createString(Ops.LOWER, left);
    }

    static <A extends Comparable<A>> Expr.Boolean lt(Expr<A> left, A right) {
        return createBoolean(Ops.LT, left, createConstant(right));
    }

    static <A extends Comparable<A>> Expr.Boolean lt(Expr<A> left,
            Expr<A> right) {
        return createBoolean(Ops.LT, left, right);
    }
        
    static <A> Expr.Boolean ne(Expr<A> left, A right) {
        return createBoolean(Ops.NE, left, createConstant(right));
    }
    
    static <A> Expr.Boolean ne(Expr<A> left, Expr<? super A> right) {
        return createBoolean(Ops.NE, left, right);
    } 
    
    static <A extends Comparable<A>> Expr.Boolean notBetween(Expr<A> left,
            A start, A end) {
        return createBoolean(Ops.NOTBETWEEN, left, createConstant(start), createConstant(end));
    }

    static <A extends Comparable<A>> Expr.Boolean notBetween(Expr<A> left,
            Expr<A> start, Expr<A> end) {
        return createBoolean(Ops.NOTBETWEEN, left, start, end);
    }
    
    static <A extends Comparable<A>> Expr.Boolean notIn(Expr<A> left,
            A... rest) {
        return createBoolean(Ops.NOTIN, left, createConstant(rest));
    }

    static <A> Expr.Boolean notIn(Expr<A> left, CollectionType<A> right){
        return createBoolean(Ops.NOTIN, left, (Expr<?>)right);
    }
    
    static Expr.Boolean or(Expr.Boolean left, Expr.Boolean right) {
        return createBoolean(Ops.OR, left, right);
    }
      
    static Expr.String substring(Expr<String> left, int beginIndex) {
        return createString(Ops.SUBSTR1ARG, left, createConstant(beginIndex));
    }

    static Expr.String substring(Expr<String> left, int beginIndex, int endIndex) {
        return createString(Ops.SUBSTR2ARGS, left, createConstant(beginIndex), createConstant(endIndex));
    }

    static Expr.String trim(Expr<String> left) {
        return createString(Ops.TRIM, left);
    }
    
    static <A, B extends A> Expr.Boolean typeOf(Expr<A> left, Class<B> right) {
        return createBoolean(Ops.ISTYPEOF, left, createConstant(right));
    }

    static Expr.String upper(Expr<String> left) {
        return createString(Ops.UPPER, left);
    }

}
