/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import com.mysema.query.grammar.Types.*;
import com.mysema.query.grammar.Ops.*;

/**
 * Grammar provides a fluent grammar for operation and order specifier creation
 * 
 * @author tiwe
 * @version $Id$
 */
public class Grammar {

    static <L, R> ExprBoolean _binOp(Op<Boolean> operator, Expr<L> left,
            Expr<R> right) {
        OperationBinaryBoolean<L, R> op = new OperationBinaryBoolean<L, R>();
        op.operator = operator;
        op.left = left;
        op.right = right;
        return op;
    }

    static <OP, RT extends OP, L, R> Operation<RT> _binOp(Op<OP> operator,
            Expr<L> left, Expr<R> right) {
        OperationBinary<OP, RT, L, R> op = new OperationBinary<OP, RT, L, R>();
        op.operator = operator;
        op.left = left;
        op.right = right;
        return op;
    }

    @SuppressWarnings("unchecked")
    static <A> Expr<A> _const(A obj) {
        if (obj instanceof Expr)
            return (Expr<A>) obj;
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

    static <F, S, T> ExprBoolean _terOp(Op<Boolean> type, Expr<F> fst,
            Expr<S> snd, Expr<T> trd) {
        OperationTertiaryBoolean<F, S, T> op = new OperationTertiaryBoolean<F, S, T>();
        op.operator = type;
        op.first = fst;
        op.second = snd;
        op.third = trd;
        return op;
    }

    static <OP, RT extends OP, F, S, T> Operation<RT> _terOp(Op<OP> type,
            Expr<F> fst, Expr<S> snd, Expr<T> trd) {
        OperationTertiary<OP, RT, F, S, T> op = new OperationTertiary<OP, RT, F, S, T>();
        op.operator = type;
        op.first = fst;
        op.second = snd;
        op.third = trd;
        return op;
    }

    static <A> ExprBoolean _unOp(Op<Boolean> type, Expr<A> left) {
        OperationUnaryBoolean<A> op = new OperationUnaryBoolean<A>();
        op.operator = type;
        op.left = left;
        return op;
    }

    static <OP, RT extends OP, A> Operation<RT> _unOp(Op<OP> type, Expr<A> left) {
        OperationUnary<OP, RT, A> op = new OperationUnary<OP, RT, A>();
        op.operator = type;
        op.left = left;
        return op;
    }

    public static <A extends Number> Expr<A> add(Expr<A> left, A right) {
        return _binOp(OpNumber.ADD, left, _const(right));
    }

    public static <A extends Number> Expr<A> add(Expr<A> left, Expr<A> right) {
        return _binOp(OpNumber.ADD, left, right);
    }

    public static <A extends Comparable<A>> ExprBoolean after(Expr<A> left,
            A right) {
        // NOTE : signature is for Comparables to support other than Java's date
        // types
        // NOTE : basically same as gt
        return _binOp(OpDate.AFTER, left, _const(right));
    }

    public static <A extends Comparable<A>> ExprBoolean after(Expr<A> left,
            Expr<A> right) {
        // NOTE : signature is for Comparables to support other than Java's date
        // types
        // NOTE : basically same as gt
        return _binOp(OpDate.AFTER, left, right);
    }

    public static ExprBoolean and(ExprBoolean left, ExprBoolean right) {
        return _binOp(OpBoolean.AND, left, right);
    }

    public static <D> AliasForCollection<D> as(PathCollection<D> from, PathDomainType<D> to) {
        return new AliasForCollection<D>(from, to);
    }
    
    public static <D> AliasForEntity<D> as(PathDomainType<D> from, PathDomainType<D> to) {
        return new AliasForEntity<D>(from, to);
    }
    
    public static <D> AliasForEntity<D> as(PathDomainType<D> from, String to) {
        return new AliasForEntity<D>(from, to);
    }
    
    public static <D> AliasForAnything<D> as(Path<D> from, String to) {
        // NOTE : maybe this needs to be possible for all expressions
        return new AliasForAnything<D>(from, to);
    }

    public static <A extends Comparable<A>> OrderSpecifier<A> asc(Expr<A> target) {
        return _orderAsc(target);
    }

    public static <A extends Comparable<A>> ExprBoolean before(Expr<A> left,
            A right) {
        // NOTE : signature is for Comparables to support other than Java's date
        // types
        // NOTE : basically same as lt
        return _binOp(OpDate.BEFORE, left, _const(right));
    }

    public static <A extends Comparable<A>> ExprBoolean before(Expr<A> left,
            Expr<A> right) {
        // NOTE : signature is for Comparables to support other than Java's date
        // types
        // NOTE : basically same as lt
        return _binOp(OpDate.BEFORE, left, right);
    }
    
    public static <A extends Comparable<A>> ExprBoolean between(Expr<A> left,
            A start, A end) {
        return _terOp(OpComparable.BETWEEN, left, _const(start), _const(end));
    }

    public static <A extends Comparable<A>> ExprBoolean between(Expr<A> left,
            Expr<A> start, Expr<A> end) {
        return _terOp(OpComparable.BETWEEN, left, start, end);
    }

    public static Expr<String> concat(Expr<String> left, Expr<String> right) {
        return _binOp(OpString.CONCAT, left, right);
    }

    public static Expr<Long> count(){
        return new CountExpr<Long>();
    }
    
    public static <A extends Comparable<A>> OrderSpecifier<A> desc(
            Expr<A> target) {
        return _orderDesc(target);
    }

    public static <A extends Number> Expr<A> div(Expr<A> left, A right) {
        return _binOp(OpNumber.DIV, left, _const(right));
    }
    
    public static <A extends Number> Expr<A> div(Expr<A> left, Expr<A> right) {
        return _binOp(OpNumber.DIV, left, right);
    }

    public static <A, B extends A> ExprBoolean eq(Expr<A> left, B right) {
        return _binOp(Op.EQ, left, _const(right));
    }

    public static <A, B extends A> ExprBoolean eq(Expr<A> left, Expr<B> right) {
        return _binOp(Op.EQ, left, right);
    }

    public static <A extends Comparable<A>> ExprBoolean goe(Expr<A> left,
            A right) {
        return _binOp(OpNumber.GOE, left, _const(right));
    }

    public static <A extends Comparable<A>> ExprBoolean goe(Expr<A> left,
            Expr<A> right) {
        return _binOp(OpNumber.GOE, left, right);
    }

    public static <A extends Comparable<A>> ExprBoolean gt(Expr<A> left, A right) {
        return _binOp(OpNumber.GT, left, _const(right));
    }

    public static <A extends Comparable<A>> ExprBoolean gt(Expr<A> left,
            Expr<A> right) {
        return _binOp(OpNumber.GT, left, right);
    }

    public static <A extends Comparable<A>> ExprBoolean in(Expr<A> left,
            A... rest) {
        return _binOp(Op.IN, left, _const(rest));
    }

    public static <A> ExprBoolean isnotnull(Expr<A> left) {
        return _unOp(Op.ISNOTNULL, left);
    }

    public static <A> ExprBoolean isnull(Expr<A> left) {
        return _unOp(Op.ISNULL, left);
    }

    public static ExprBoolean like(Expr<String> left, String right) {
        return _binOp(OpString.LIKE, left, _const(right));
    }

    public static <A extends Comparable<A>> ExprBoolean loe(Expr<A> left,
            A right) {
        return _binOp(OpNumber.LOE, left, _const(right));
    }

    public static <A extends Comparable<A>> ExprBoolean loe(Expr<A> left,
            Expr<A> right) {
        return _binOp(OpNumber.LOE, left, right);
    }

    public static Expr<String> lower(Expr<String> left) {
        return _unOp(OpString.LOWER, left);
    }

    public static <A extends Comparable<A>> ExprBoolean lt(Expr<A> left, A right) {
        return _binOp(OpNumber.LT, left, _const(right));
    }

    public static <A extends Comparable<A>> ExprBoolean lt(Expr<A> left,
            Expr<A> right) {
        return _binOp(OpNumber.LT, left, right);
    }

    public static <A extends Number> Expr<A> mult(Expr<A> left, A right) {
        return _binOp(OpNumber.MULT, left, _const(right));
    }

    public static <A extends Number> Expr<A> mult(Expr<A> left, Expr<A> right) {
        return _binOp(OpNumber.MULT, left, right);
    }

    public static <A, B extends A> ExprBoolean ne(Expr<A> left, B right) {
        return _binOp(Op.NE, left, _const(right));
    }

    public static <A, B extends A> ExprBoolean ne(Expr<A> left, Expr<B> right) {
        return _binOp(Op.NE, left, right);
    }

    public static ExprBoolean not(ExprBoolean left) {
        return _unOp(OpBoolean.NOT, left);
    }

    public static ExprBoolean or(ExprBoolean left, ExprBoolean right) {
        return _binOp(OpBoolean.OR, left, right);
    }

    public static <A extends Number> Expr<A> sub(Expr<A> left, A right) {
        return _binOp(OpNumber.SUB, left, _const(right));
    }

    public static <A extends Number> Expr<A> sub(Expr<A> left, Expr<A> right) {
        return _binOp(OpNumber.SUB, left, right);
    }

    public static Expr<String> substr(Expr<String> left, int start) {
        return _binOp(OpString.SUBSTR, left, _const(start));
    }

    public static Expr<String> substr(Expr<String> left, int start, int offset) {
        return _terOp(OpString.SUBSTR, left, _const(start), _const(offset));
    }

    public static <A, B extends A> ExprBoolean typeOf(Expr<A> left,
            Class<B> right) {
        return _binOp(Op.ISTYPEOF, left, _const(right));
    }

    public static Expr<String> upper(Expr<String> left) {
        return _unOp(OpString.UPPER, left);
    }
}
