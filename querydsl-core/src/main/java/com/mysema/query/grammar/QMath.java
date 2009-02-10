/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import com.mysema.query.grammar.Ops.OpMath;
import com.mysema.query.grammar.Ops.OpNumberAgg;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Factory;
import com.mysema.query.grammar.types.Expr.ENumber;

/**
 * QMath provides math functions
 *
 * @author tiwe
 * @version $Id$
 */
public class QMath extends Factory{
        
    public static <A extends Number & Comparable<? super A>> ENumber<A> mult(Expr<A> left, A right) {
        return createNumber(left.getType(), Ops.MULT, left, createConstant(right));
    }

    public static <A extends Number & Comparable<? super A>> ENumber<A> mult(Expr<A> left, Expr<A> right) {
        return createNumber(left.getType(), Ops.MULT, left, right);
    }
        
    public static <A extends Number & Comparable<? super A>> ENumber<Double> div(ENumber<A> left, A right) {
        return createNumber(Double.class,Ops.DIV, left, createConstant(right));
    }

    public static <A extends Number & Comparable<? super A>> ENumber<Double> div(Expr<A> left, Expr<A> right) {
        return createNumber(Double.class,Ops.DIV, left, right);
    }
    
    public static <A extends Number & Comparable<? super A>> ENumber<A> add(Expr<A> left, A right) {
        return createNumber(left.getType(), Ops.ADD, left, createConstant(right));
    }
    
    public static <A extends Number & Comparable<? super A>> ENumber<A> add(Expr<A> left, Expr<A> right) {
        return createNumber(left.getType(), Ops.ADD, left, right);
    }

    public static <A extends Number & Comparable<? super A>> ENumber<A> sub(Expr<A> left, A right) {
        return createNumber(left.getType(), Ops.SUB, left, createConstant(right));
    }

    public static <A extends Number & Comparable<? super A>> ENumber<A> sub(Expr<A> left, Expr<A> right) {
        return createNumber(left.getType(), Ops.SUB, left, right);
    }
    
    public static <A extends Number & Comparable<? super A>> ENumber<A> abs(Expr<A> left){
        return createNumber(left.getType(), OpMath.ABS, left);
    }
    
    public static ENumber<Double> acos(Expr<Double> left){
        return createNumber(left.getType(), OpMath.ACOS, left);
    }
    
    public static ENumber<Double> asin(Expr<Double> left){
        return createNumber(left.getType(),OpMath.ASIN, left);
    }
    
    public static ENumber<Double> atan(Expr<Double> left){
        return createNumber(left.getType(),OpMath.ATAN, left);
    }
    
    public static ENumber<Double> ceil(Expr<Double> left){
        return createNumber(left.getType(),OpMath.CEIL, left);
    }
    
    public static ENumber<Double> cos(Expr<Double> left){
        return createNumber(left.getType(),OpMath.COS, left);
    }
    
    public static ENumber<Double> exp(Expr<Double> left){
        return createNumber(left.getType(),OpMath.EXP, left);
    }
    
    public static ENumber<Double> floor(Expr<Double> left){
        return createNumber(left.getType(),OpMath.FLOOR, left);
    }
    
    public static ENumber<Double> log(Expr<Double> left){
        return createNumber(left.getType(),OpMath.LOG, left);
    }
    
    public static ENumber<Double> log10(Expr<Double> left){
        return createNumber(left.getType(),OpMath.LOG10, left);
    }
    
    public static <A extends Number & Comparable<? super A>> ENumber<A> max(Expr<A> left, Expr<A> right){
        return createNumber(left.getType(),OpMath.MAX, left, right);
    }
    
    public static <A extends Number & Comparable<? super A>> ENumber<A> min(Expr<A> left, Expr<A> right){
        return createNumber(left.getType(),OpMath.MIN, left, right);
    }
    
    public static ENumber<Integer> mod(Expr<Integer> left, Expr<Integer> right){
        return createNumber(left.getType(),OpMath.MOD, left, right);
    }
    
    public static ENumber<Double> pow(Expr<Double> left, Expr<Double> right){
        return createNumber(left.getType(),OpMath.POWER, left, right);
    }
    
    public static ENumber<Double> random(){
        return createNumber(Double.class,OpMath.RANDOM);
    }
    
    public static ENumber<Double> round(Expr<Double> left){
        return createNumber(left.getType(),OpMath.ROUND, left);
    }
    
    public static ENumber<Double> sin(Expr<Double> left){
        return createNumber(left.getType(),OpMath.SIN, left);
    }
    
    public static <A extends Number & Comparable<? super A>> ENumber<Double> sqrt(Expr<A> left){
        return createNumber(Double.class,OpMath.SQRT, left);
    }
    
    public static ENumber<Double> tan(Expr<Double> left){
        return createNumber(left.getType(),OpMath.TAN, left);
    }
    
    public static <A extends Number & Comparable<? super A>> ENumber<A> max(Expr<A> left){
        return createNumber(left.getType(), OpNumberAgg.MAX, left);
    }

    public static <A extends Number & Comparable<? super A>> ENumber<A> min(Expr<A> left){
        return createNumber(left.getType(), OpNumberAgg.MIN, left);
    }
}
