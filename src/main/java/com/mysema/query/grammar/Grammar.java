/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import com.mysema.query.grammar.Types.*;

/**
 * Grammar provides a fluent grammar for operation and order specifier creation
 *
 * @author tiwe
 * @version $Id$
 */
public class Grammar {
    
    static <L,R> BooleanOperation _binOp(Op<Boolean> type, Expr<L> left, Expr<R> right) {
        BinaryBooleanOperation<L,R> op = new BinaryBooleanOperation<L,R>();
        op.type = type;
        op.left = left;
        op.right = right;
        return op;
    }
    
    static <RT,L,R> Operation<RT> _binOp(Op<RT> type, Expr<L> left, Expr<R> right) {
        BinaryOperation<RT,L,R> op = new BinaryOperation<RT,L,R>();
        op.type = type;
        op.left = left;
        op.right = right;
        return op;
    }
    
    @SuppressWarnings("unchecked")
    static <A> Expr<A> _const(A obj){
        if (obj instanceof Expr) return (Expr<A>)obj;
        ConstantExpr<A> e = new ConstantExpr<A>();
        e.constant = obj;
        return e;
    }
    
    static <A extends Comparable<A>> OrderSpecifier<A> _orderAsc(Expr<A> target) {
        OrderSpecifier<A> os = new OrderSpecifier<A>();
        os.order = Order.ASC;
        os.target = target;
        return os;
    }   
    
    static <A extends Comparable<A>> OrderSpecifier<A> _orderDesc(Expr<A> target) {
        OrderSpecifier<A> os = new OrderSpecifier<A>();
        os.order = Order.DESC;
        os.target = target;
        return os;
    }   
    
    static <F,S,T> BooleanOperation _terOp(Op<Boolean> type, Expr<F> fst, Expr<S> snd, Expr<T> trd){
        TertiaryBooleanOperation<F,S,T> op = new TertiaryBooleanOperation<F,S,T>();
        op.type = type;
        op.first = fst;
        op.second = snd;
        op.third = trd;
        return op;
    }
        
    static <RT,F,S,T> Operation<RT> _terOp(Op<RT> type, Expr<F> fst, Expr<S> snd, Expr<T> trd){
        TertiaryOperation<RT,F,S,T> op = new TertiaryOperation<RT,F,S,T>();
        op.type = type;
        op.first = fst;
        op.second = snd;
        op.third = trd;
        return op;
    }
    
    static <A> BooleanOperation _unOp(Op<Boolean> type, Expr<A> left) {
        UnaryBooleanOperation<A> op = new UnaryBooleanOperation<A>();
        op.type = type;
        op.left = left;
        return op;
    }
    
    static <RT,A> Operation<RT> _unOp(Op<RT> type, Expr<A> left) {
        UnaryOperation<RT,A> op = new UnaryOperation<RT,A>();
        op.type = type;
        op.left = left;
        return op;
    }
    
    public static <A extends Comparable<A>> BooleanExpr after(Expr<A> left, A right){
        // NOTE : signature is for Comparables to support other than Java's date types
        // NOTE : basically same as gt
        return _binOp(DateOp.AFTER, left, _const(right));
    }
    
    public static <A extends Comparable<A>> BooleanExpr after(Expr<A> left, Expr<A> right){
        // NOTE : signature is for Comparables to support other than Java's date types
        // NOTE : basically same as gt
        return _binOp(DateOp.AFTER, left, right);
    }

    public static BooleanExpr and(BooleanExpr left, BooleanExpr right){
        return _binOp(BoOp.AND, left, right);
    }
    
    public static <D> EntityExpr<D> as(DomainType<D> from, DomainType<D> to){
        return new Alias<D>(from, to);
    }    
    
    public static <A extends Comparable<A>> OrderSpecifier<A> asc(Expr<A> target){
        return _orderAsc(target);
    }  
    
    public static <A extends Comparable<A>> BooleanExpr before(Expr<A> left, A right){
        // NOTE : signature is for Comparables to support other than Java's date types
        // NOTE : basically same as lt
        return _binOp(DateOp.BEFORE, left, _const(right));
    }  
    
    public static <A extends Comparable<A>> BooleanExpr before(Expr<A> left, Expr<A> right){
        // NOTE : signature is for Comparables to support other than Java's date types
        // NOTE : basically same as lt
        return _binOp(DateOp.BEFORE, left, right);
    } 
    
    public static <A extends Comparable<A>> BooleanExpr between(Expr<A> left, A start, A end){
        return _terOp(CompOp.BETWEEN, left, _const(start), _const(end));
    }
    
    public static Expr<String> concat(Expr<String> left, Expr<String> right){
        return _binOp(StrOp.CONCAT, left, right);
    }    
    
    public static <A extends Comparable<A>> OrderSpecifier<A> desc(Expr<A> target){
        return _orderDesc(target);
    }
    
    public static <A,B extends A> BooleanExpr eq(Expr<A> left, B right){
        return _binOp(Op.EQ, left, _const(right));
    }
        
    public static <A,B extends A> BooleanExpr eq(Expr<A> left, Expr<B> right){
        return _binOp(Op.EQ, left, right);
    }
    
    public static <A extends Comparable<A>> BooleanExpr goe(Expr<A> left, A right){
        return _binOp(NumOp.GOE, left, _const(right));
    }
    
    public static <A extends Comparable<A>> BooleanExpr goe(Expr<A> left, Expr<A> right){
        return _binOp(NumOp.GOE, left, right);
    }
    
    public static <A extends Comparable<A>> BooleanExpr gt(Expr<A> left, A right){
        return _binOp(NumOp.GT, left, _const(right));
    }
    
    public static <A extends Comparable<A>> BooleanExpr gt(Expr<A> left, Expr<A> right){
        return _binOp(NumOp.GT, left, right);
    }   
    
    public static <A extends Comparable<A>> BooleanExpr in(Expr<A> left, A... rest){
        return _binOp(Op.IN, left, _const(rest));
    }
    
    public static BooleanExpr like(Expr<String> left, String right){
        return _binOp(StrOp.LIKE, left, _const(right));
    }
    
    public static <A extends Comparable<A>> BooleanExpr loe(Expr<A> left, A right){
        return _binOp(NumOp.LOE, left, _const(right));
    }
    
    public static <A extends Comparable<A>> BooleanExpr loe(Expr<A> left, Expr<A> right){
        return _binOp(NumOp.LOE, left, right);
    }
    
    public static Expr<String> lower(Expr<String> left){
        return _unOp(StrOp.LOWER, left); 
    }
    
    public static <A extends Comparable<A>> BooleanExpr lt(Expr<A> left, A right){
        return _binOp(NumOp.LT, left, _const(right));
    }    
    
    public static <A extends Comparable<A>> BooleanExpr lt(Expr<A> left, Expr<A> right){
        return _binOp(NumOp.LT, left, right);
    }
    
    public static <A,B extends A> BooleanExpr ne(Expr<A> left, B right){
        return _binOp(Op.NE, left, _const(right));
    }
    
    public static <A,B extends A> BooleanExpr ne(Expr<A> left, Expr<B> right){
        return _binOp(Op.NE, left, right);
    } 
    
    public static BooleanExpr not(BooleanExpr left){
        return _unOp(BoOp.NOT, left);
    }

    public static BooleanExpr or(BooleanExpr left, BooleanExpr right){
        return _binOp(BoOp.OR, left, right);
    }
    
    public static Expr<String> substr(Expr<String> left, int start){
        return _binOp(StrOp.SUBSTR, left, _const(start));
    }

    public static Expr<String> substr(Expr<String> left, int start, int offset){
        return _terOp(StrOp.SUBSTR, left, _const(start), _const(offset));
    }
    
    public static <A,B extends A> BooleanExpr typeOf(Expr<A> left, Class<B> right){
        return _binOp(Op.ISTYPEOF, left, _const(right));
    }
    
    public static Expr<String> upper(Expr<String> left){
        return _unOp(StrOp.UPPER, left); 
    }
}
