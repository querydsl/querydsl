/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import java.util.Collection;

import com.mysema.query.grammar.Ops.*;
import com.mysema.query.grammar.Types.*;

/**
 * Grammar provides the factory methods for the fluent grammar
 * 
 * @author tiwe
 * @version $Id$
 */
public class Grammar {

    static ExprBoolean _op(Op<Boolean> operator, Expr<?>... args) {
        OperationBoolean op = new OperationBoolean();
        op.operator = operator;
        op.args = args;
        return op;
    }

    static <OP, RT extends OP> Operation<OP,RT> _op(Op<OP> operator, Expr<?>... args) {
        Operation<OP, RT> op = new Operation<OP, RT>();
        op.operator = operator;
        op.args = args;
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
        // TODO : should be cached if argument is path
        OrderSpecifier<A> os = new OrderSpecifier<A>();
        os.order = Order.ASC;
        os.target = target;
        return os;
    }

    static <A extends Comparable<A>> OrderSpecifier<A> _orderDesc(Expr<A> target) {
        // TODO : should be cached if argument is path
        OrderSpecifier<A> os = new OrderSpecifier<A>();
        os.order = Order.DESC;
        os.target = target;
        return os;
    }

    public static <A extends Number> ExprNoEntity<A> add(Expr<A> left, A right) {
        return _op(OpNumber.ADD, left, _const(right));
    }

    public static <A extends Number> ExprNoEntity<A> add(Expr<A> left, Expr<A> right) {
        return _op(OpNumber.ADD, left, right);
    }

    public static <A extends Comparable<A>> ExprBoolean after(Expr<A> left,
            A right) {
        // NOTE : signature is for Comparables to support other than Java's date types
        // NOTE : basically same as gt
        return _op(OpDate.AFTER, left, _const(right));
    }

    public static <A extends Comparable<A>> ExprBoolean after(Expr<A> left,
            Expr<A> right) {
        // NOTE : signature is for Comparables to support other than Java's date types
        // NOTE : basically same as gt
        return _op(OpDate.AFTER, left, right);
    }

    static ExprBoolean and(ExprBoolean left, ExprBoolean right) {
        return _op(OpBoolean.AND, left, right);
    }

    public static <D> AliasNoEntity<D> as(ExprNoEntity<D> from, String to) {
        // NOTE : maybe this needs to be possible for all expressions
        return new AliasNoEntity<D>(from, to);
    }
    
    static <D> AliasEntity<D> as(PathEntity<D> from, PathEntity<D> to) {
        return new AliasEntity<D>(from, to);
    }
    
    static <D> AliasCollection<D> as(PathEntityCollection<D> from, PathEntity<D> to) {
        return new AliasCollection<D>(from, to);
    }

    public static <A extends Comparable<A>> OrderSpecifier<A> asc(Expr<A> target) {
        return _orderAsc(target);
    }

    public static <A extends Comparable<A>> ExprBoolean before(Expr<A> left,
            A right) {
        // NOTE : signature is for Comparables to support other than Java's date types
        // NOTE : basically same as lt
        return _op(OpDate.BEFORE, left, _const(right));
    }

    public static <A extends Comparable<A>> ExprBoolean before(Expr<A> left,
            Expr<A> right) {
        // NOTE : signature is for Comparables to support other than Java's date types
        // NOTE : basically same as lt
        return _op(OpDate.BEFORE, left, right);
    }
    
    public static <A extends Comparable<A>> ExprBoolean between(Expr<A> left,
            A start, A end) {
        return _op(OpComparable.BETWEEN, left, _const(start), _const(end));
    }

    public static <A extends Comparable<A>> ExprBoolean between(Expr<A> left,
            Expr<A> start, Expr<A> end) {
        return _op(OpComparable.BETWEEN, left, start, end);
    }

    public static ExprNoEntity<String> concat(Expr<String> left, Expr<String> right) {
        return _op(OpString.CONCAT, left, right);
    }
    
    public static <A extends Comparable<A>> OrderSpecifier<A> desc(
            Expr<A> target) {
        return _orderDesc(target);
    }

    public static <A extends Number> ExprNoEntity<A> div(Expr<A> left, A right) {
        return _op(OpNumber.DIV, left, _const(right));
    }
    
    public static <A extends Number> ExprNoEntity<A> div(Expr<A> left, Expr<A> right) {
        return _op(OpNumber.DIV, left, right);
    }

    static <A, B extends A> ExprBoolean eq(Expr<A> left, B right) {
        return _op(Op.EQ, left, _const(right));
    }

    static <A, B extends A> ExprBoolean eq(Expr<A> left, Expr<B> right) {
        return _op(Op.EQ, left, right);
    }

    public static <A extends Comparable<A>> ExprBoolean goe(Expr<A> left,
            A right) {
        return _op(OpComparable.GOE, left, _const(right));
    }

    public static <A extends Comparable<A>> ExprBoolean goe(Expr<A> left,
            Expr<A> right) {
        return _op(OpComparable.GOE, left, right);
    }

    public static <A extends Comparable<A>> ExprBoolean gt(Expr<A> left, A right) {
        return _op(OpComparable.GT, left, _const(right));
    }

    public static <A extends Comparable<A>> ExprBoolean gt(Expr<A> left,
            Expr<A> right) {
        return _op(OpComparable.GT, left, right);
    }
                
    public static <A> ExprBoolean in(A left, ExprEntity<Collection<A>> right){
        return _op(Op.INELEMENTS, _const(left), right);
    }
    
    public static <A extends Comparable<A>> ExprBoolean in(Expr<A> left,
            A... rest) {
        return _op(Op.INARRAY, left, _const(rest));
    }
    
    public static <A> ExprBoolean in(ExprEntity<A> left, ExprEntity<Collection<A>> right){
        return _op(Op.INELEMENTS, left, right);
    }

    public static <A> ExprBoolean isnotnull(Expr<A> left) {
        return _op(Op.ISNOTNULL, left);
    }

    public static <A> ExprBoolean isnull(Expr<A> left) {
        return _op(Op.ISNULL, left);
    }

    public static ExprBoolean like(Expr<String> left, String right) {
        return _op(OpString.LIKE, left, _const(right));
    }

    public static <A extends Comparable<A>> ExprBoolean loe(Expr<A> left,
            A right) {
        return _op(OpComparable.LOE, left, _const(right));
    }

    public static <A extends Comparable<A>> ExprBoolean loe(Expr<A> left,
            Expr<A> right) {
        return _op(OpComparable.LOE, left, right);
    }

    public static ExprNoEntity<String> lower(Expr<String> left) {
        return _op(OpString.LOWER, left);
    }

    public static <A extends Comparable<A>> ExprBoolean lt(Expr<A> left, A right) {
        return _op(OpComparable.LT, left, _const(right));
    }

    public static <A extends Comparable<A>> ExprBoolean lt(Expr<A> left,
            Expr<A> right) {
        return _op(OpComparable.LT, left, right);
    }

    public static <A extends Number> ExprNoEntity<A> mult(Expr<A> left, A right) {
        return _op(OpNumber.MULT, left, _const(right));
    }

    public static <A extends Number> ExprNoEntity<A> mult(Expr<A> left, Expr<A> right) {
        return _op(OpNumber.MULT, left, right);
    }

    static <A, B extends A> ExprBoolean ne(Expr<A> left, B right) {
        return _op(Op.NE, left, _const(right));
    }

    static <A, B extends A> ExprBoolean ne(Expr<A> left, Expr<B> right) {
        return _op(Op.NE, left, right);
    }

    public static ExprBoolean not(ExprBoolean left) {
        return _op(OpBoolean.NOT, left);
    }

    static ExprBoolean or(ExprBoolean left, ExprBoolean right) {
        return _op(OpBoolean.OR, left, right);
    }

    public static <A extends Number> ExprNoEntity<A> sub(Expr<A> left, A right) {
        return _op(OpNumber.SUB, left, _const(right));
    }

    public static <A extends Number> ExprNoEntity<A> sub(Expr<A> left, Expr<A> right) {
        return _op(OpNumber.SUB, left, right);
    }

    public static ExprNoEntity<String> substr(Expr<String> left, int start) {
        return _op(OpString.SUBSTR, left, _const(start));
    }

    public static ExprNoEntity<String> substr(Expr<String> left, int start, int offset) {
        return _op(OpString.SUBSTR, left, _const(start), _const(offset));
    }

    public static <A, B extends A> ExprBoolean typeOf(Expr<A> left, Class<B> right) {
        return _op(Op.ISTYPEOF, left, _const(right));
    }

    public static ExprNoEntity<String> upper(Expr<String> left) {
        return _op(OpString.UPPER, left);
    }
}
