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

    static final ExprBoolean _boolean(Op<Boolean> operator, Expr<?>... args) {
        _check("operator",operator);
        _check("args",args);
        return new OperationBoolean(operator, args);
    }
    
    private static void _check(String name, Object obj) {
        if (obj == null) throw new IllegalArgumentException(name + " was null");        
    }
    
    static final <OP, RT extends Comparable<RT>> ExprComparable<RT> _comparable(Op<OP> operator, Expr<?>... args) {
        _check("operator",operator);
        _check("args",args);
        return new OperationComparable<OP,RT>(operator, args);
    }
    
    @SuppressWarnings("unchecked")
    static final <A> Expr<A> _const(A obj) {
        _check("constant",obj);
        if (obj instanceof Expr)
            return (Expr<A>) obj;
        return new ConstantExpr<A>(obj);
    }

    static final <D extends Comparable<D>> ExprComparable<D> _number(Op<Number> operator, Expr<?>... args) {
        _check("operator",operator);
        _check("args",args);
        return new OperationNumber<D>(operator, args);
    }
    
    static final <A extends Comparable<A>> OrderSpecifier<A> _orderAsc(Expr<A> target) {
        _check("target",target);
        OrderSpecifier<A> os = new OrderSpecifier<A>();
        os.order = Order.ASC;
        os.target = target;
        return os;
    }

    static final <A extends Comparable<A>> OrderSpecifier<A> _orderDesc(Expr<A> target) {
        _check("target",target);
        OrderSpecifier<A> os = new OrderSpecifier<A>();
        os.order = Order.DESC;
        os.target = target;
        return os;
    }

    static final ExprString _string(Op<String> operator, Expr<?>... args) {
        _check("operator",operator);
        _check("args",args);
        return new OperationString(operator, args);
    }

    public static <A extends Comparable<A>> ExprComparable<A> add(Expr<A> left, A right) {
        return _number(OpNumber.ADD, left, _const(right));
    }

    public static <A extends Comparable<A>> ExprComparable<A> add(Expr<A> left, Expr<A> right) {
        return _number(OpNumber.ADD, left, right);
    }

    static <A extends Comparable<A>> ExprBoolean after(Expr<A> left,
            A right) {
        // NOTE : signature is for Comparables to support other than Java's date types
        return _boolean(OpDate.AFTER, left, _const(right));
    }

    static <A extends Comparable<A>> ExprBoolean after(Expr<A> left,
            Expr<A> right) {
        // NOTE : signature is for Comparables to support other than Java's date types
        return _boolean(OpDate.AFTER, left, right);
    }

    static ExprBoolean and(ExprBoolean left, ExprBoolean right) {
        return _boolean(OpBoolean.AND, left, right);
    }

    static <D> AliasNoEntity<D> as(ExprNoEntity<D> from, String to) {
        _check("from",from);
        _check("to",to);
        return new AliasNoEntity<D>(from, to);
    }
    
    static <D> AliasEntity<D> as(PathEntity<D> from, PathEntity<D> to) {
        _check("from",from);
        _check("to",to);
        return new AliasEntity<D>(from, to);
    }
    
    static <D> AliasEntityCollection<D> as(PathEntityCollection<D> from, PathEntity<D> to) {
        _check("from",from);
        _check("to",to);
        return new AliasEntityCollection<D>(from, to);
    }

    static <A extends Comparable<A>> OrderSpecifier<A> asc(Expr<A> target) {
        return _orderAsc(target);
    }

    static <A extends Comparable<A>> ExprBoolean before(Expr<A> left,
            A right) {
        // NOTE : signature is for Comparables to support other than Java's date types
        // NOTE : basically same as lt
        return _boolean(OpDate.BEFORE, left, _const(right));
    }

    static <A extends Comparable<A>> ExprBoolean before(Expr<A> left,
            Expr<A> right) {
        // NOTE : signature is for Comparables to support other than Java's date types
        // NOTE : basically same as lt
        return _boolean(OpDate.BEFORE, left, right);
    }
    
    static <A extends Comparable<A>> ExprBoolean between(Expr<A> left,
            A start, A end) {
        return _boolean(OpComparable.BETWEEN, left, _const(start), _const(end));
    }

    static <A extends Comparable<A>> ExprBoolean between(Expr<A> left,
            Expr<A> start, Expr<A> end) {
        return _boolean(OpComparable.BETWEEN, left, start, end);
    }

    static ExprString concat(Expr<String> left, Expr<String> right) {
        return _string(OpString.CONCAT, left, right);
    }
    
    static ExprString concat(Expr<String> left, String right) {
        return _string(OpString.CONCAT, left, _const(right));
    }
    
    static <A extends Comparable<A>> OrderSpecifier<A> desc(
            Expr<A> target) {
        return _orderDesc(target);
    }

    public static <A extends Comparable<A>> ExprComparable<A> div(Expr<A> left, A right) {
        return _number(OpNumber.DIV, left, _const(right));
    }
    
    public static <A extends Comparable<A>> ExprComparable<A> div(Expr<A> left, Expr<A> right) {
        return _number(OpNumber.DIV, left, right);
    }

    static <A, B extends A> ExprBoolean eq(Expr<A> left, B right) {
        return _boolean(Op.EQ, left, _const(right));
    }

    static <A, B extends A> ExprBoolean eq(Expr<A> left, Expr<B> right) {
        return _boolean(Op.EQ, left, right);
    }

    static <A extends Comparable<A>> ExprBoolean goe(Expr<A> left,
            A right) {
        return _boolean(OpComparable.GOE, left, _const(right));
    }

    static <A extends Comparable<A>> ExprBoolean goe(Expr<A> left,
            Expr<A> right) {
        return _boolean(OpComparable.GOE, left, right);
    }

    static <A extends Comparable<A>> ExprBoolean gt(Expr<A> left, A right) {
        return _boolean(OpComparable.GT, left, _const(right));
    }

    static <A extends Comparable<A>> ExprBoolean gt(Expr<A> left,
            Expr<A> right) {
        return _boolean(OpComparable.GT, left, right);
    }
                
    public static <A> ExprBoolean in(A left, ExprEntity<Collection<A>> right){
        return _boolean(Op.INELEMENTS, _const(left), right);
    }
    
    static <A extends Comparable<A>> ExprBoolean in(Expr<A> left,
            A... rest) {
        return _boolean(Op.INARRAY, left, _const(rest));
    }
    
    static <A> ExprBoolean in(ExprEntity<A> left, ExprEntity<Collection<A>> right){
        return _boolean(Op.INELEMENTS, left, right);
    }

    static <A> ExprBoolean isnotnull(Expr<A> left) {
        return _boolean(Op.ISNOTNULL, left);
    }

    static <A> ExprBoolean isnull(Expr<A> left) {
        return _boolean(Op.ISNULL, left);
    }

    static ExprBoolean like(Expr<String> left, String right) {
        return _boolean(OpString.LIKE, left, _const(right));
    }

    static <A extends Comparable<A>> ExprBoolean loe(Expr<A> left,
            A right) {
        return _boolean(OpComparable.LOE, left, _const(right));
    }

    static <A extends Comparable<A>> ExprBoolean loe(Expr<A> left,
            Expr<A> right) {
        return _boolean(OpComparable.LOE, left, right);
    }

    static ExprString lower(Expr<String> left) {
        return _string(OpString.LOWER, left);
    }

    static <A extends Comparable<A>> ExprBoolean lt(Expr<A> left, A right) {
        return _boolean(OpComparable.LT, left, _const(right));
    }

    static <A extends Comparable<A>> ExprBoolean lt(Expr<A> left,
            Expr<A> right) {
        return _boolean(OpComparable.LT, left, right);
    }

    public static <A extends Comparable<A>> ExprComparable<A> mult(Expr<A> left, A right) {
        return _number(OpNumber.MULT, left, _const(right));
    }

    public static <A extends Comparable<A>> ExprComparable<A> mult(Expr<A> left, Expr<A> right) {
        return _number(OpNumber.MULT, left, right);
    }

    static <A, B extends A> ExprBoolean ne(Expr<A> left, B right) {
        return _boolean(Op.NE, left, _const(right));
    }

    static <A, B extends A> ExprBoolean ne(Expr<A> left, Expr<B> right) {
        return _boolean(Op.NE, left, right);
    }

    public static ExprBoolean not(ExprBoolean left) {
        return _boolean(OpBoolean.NOT, left);
    }

    static ExprBoolean or(ExprBoolean left, ExprBoolean right) {
        return _boolean(OpBoolean.OR, left, right);
    }
    
    public static <A> SubQuery<A> select(Expr<A> select){
        return new SubQuery<A>(select);
    }
    
    public static <A extends Comparable<A>> ExprComparable<A> sub(Expr<A> left, A right) {
        return _number(OpNumber.SUB, left, _const(right));
    }

    public static <A extends Comparable<A>> ExprComparable<A> sub(Expr<A> left, Expr<A> right) {
        return _number(OpNumber.SUB, left, right);
    }

    static ExprString substring(Expr<String> left, int beginIndex) {
        return _string(OpString.SUBSTR1ARG, left, _const(beginIndex));
    }

    static ExprString substring(Expr<String> left, int beginIndex, int endIndex) {
        return _string(OpString.SUBSTR2ARGS, left, _const(beginIndex), _const(endIndex));
    }

    static ExprString trim(Expr<String> left) {
        return _string(OpString.TRIM, left);
    }
    
    static <A, B extends A> ExprBoolean typeOf(Expr<A> left, Class<B> right) {
        return _boolean(Op.ISTYPEOF, left, _const(right));
    }

    static ExprString upper(Expr<String> left) {
        return _string(OpString.UPPER, left);
    }
}
