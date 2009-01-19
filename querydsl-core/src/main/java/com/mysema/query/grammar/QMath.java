package com.mysema.query.grammar;

import com.mysema.query.grammar.Ops.OpMath;
import com.mysema.query.grammar.types.Expr;

import static com.mysema.query.grammar.types.Factory.*;

/**
 * QMath provides
 *
 * @author tiwe
 * @version $Id$
 */
public class QMath {

    public static <A extends Number & Comparable<A>> Expr.EComparable<A> abs(Expr<A> left){
        return createNumber(OpMath.ABS, left);
    }
    
    public static <A extends Number & Comparable<A>> Expr.EComparable<A> acos(Expr<A> left){
        return createNumber(OpMath.ACOS, left);
    }
    
    public static <A extends Number & Comparable<A>> Expr.EComparable<A> asin(Expr<A> left){
        return createNumber(OpMath.ASIN, left);
    }
    
    public static <A extends Number & Comparable<A>> Expr.EComparable<A> atan(Expr<A> left){
        return createNumber(OpMath.ATAN, left);
    }
    
    public static <A extends Number & Comparable<A>> Expr.EComparable<A> ceil(Expr<A> left){
        return createNumber(OpMath.CEIL, left);
    }
    
    public static <A extends Number & Comparable<A>> Expr.EComparable<A> cos(Expr<A> left){
        return createNumber(OpMath.COS, left);
    }
    
    public static <A extends Number & Comparable<A>> Expr.EComparable<A> exp(Expr<A> left){
        return createNumber(OpMath.EXP, left);
    }
    
    public static <A extends Number & Comparable<A>> Expr.EComparable<A> floor(Expr<A> left){
        return createNumber(OpMath.FLOOR, left);
    }
    
    public static <A extends Number & Comparable<A>> Expr.EComparable<A> log(Expr<A> left){
        return createNumber(OpMath.LOG, left);
    }
    
    public static <A extends Number & Comparable<A>> Expr.EComparable<A> log10(Expr<A> left){
        return createNumber(OpMath.LOG10, left);
    }
    
    public static <A extends Number & Comparable<A>> Expr.EComparable<A> max(Expr<A> left, Expr<A> right){
        return createNumber(OpMath.MAX, left, right);
    }
    
    public static <A extends Number & Comparable<A>> Expr.EComparable<A> min(Expr<A> left, Expr<A> right){
        return createNumber(OpMath.MIN, left, right);
    }
    
    public static <A extends Number & Comparable<A>> Expr.EComparable<A> mod(Expr<A> left, Expr<A> right){
        return createNumber(OpMath.MOD, left, right);
    }
    
    public static <A extends Number & Comparable<A>> Expr.EComparable<A> pow(Expr<A> left, Expr<A> right){
        return createNumber(OpMath.POWER, left, right);
    }
    
    public static <A extends Number & Comparable<A>> Expr.EComparable<A> random(){
        return createNumber(OpMath.RANDOM);
    }
    
    public static <A extends Number & Comparable<A>> Expr.EComparable<A> round(Expr<A> left){
        return createNumber(OpMath.ROUND, left);
    }
    
    public static <A extends Number & Comparable<A>> Expr.EComparable<A> sin(Expr<A> left){
        return createNumber(OpMath.SIN, left);
    }
    
    public static <A extends Number & Comparable<A>> Expr.EComparable<A> sqrt(Expr<A> left){
        return createNumber(OpMath.SQRT, left);
    }
    
    public static <A extends Number & Comparable<A>> Expr.EComparable<A> tan(Expr<A> left){
        return createNumber(OpMath.TAN, left);
    }
}
