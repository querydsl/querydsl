package com.mysema.query.grammar;


import com.mysema.query.grammar.Types.*;

/**
 * Grammar provides
 *
 * @author tiwe
 * @version $Id$
 */
public class Grammar {
    
    static <A> OrderSpecifier<A> _orderAsc(Expr<A> target) {
        OrderSpecifier<A> os = new OrderSpecifier<A>();
        os.order = Order.ASC;
        os.target = target;
        return os;
    }
    
    static <A> Expr<A> _asConst(A obj){
        ConstantExpr<A> e = new ConstantExpr<A>();
        e.constant = obj;
        return e;
    }   
    
    static <A> OrderSpecifier<A> _orderDesc(Expr<A> target) {
        OrderSpecifier<A> os = new OrderSpecifier<A>();
        os.order = Order.DESC;
        os.target = target;
        return os;
    }
    
    public static Expr<Boolean> and(Expr<Boolean> left, Expr<Boolean> right){
        return _bbe(BoOp.AND, left, right);
    }
    
    public static <A> OrderSpecifier<A> asc(Expr<A> target){
        return _orderAsc(target);
    }
    
    public static <A> OrderSpecifier<A> desc(Expr<A> target){
        return _orderDesc(target);
    }  
    
    public static Expr<Boolean> eq(Object left, Object right){
        return _bbe(NumOp.EQ, left, right);
    }    
    
    public static Expr<Boolean> goe(Object left, Object right){
        return _bbe(NumOp.GOE, left, right);
    }  
    
    public static Expr<Boolean> gt(Object left, Object right){
        return _bbe(NumOp.GT, left, right);
    }     
    
    public static Expr<Boolean> like(Expr<String> left, String right){
        return _bbe(StrOp.LIKE, left, right);
    }
    
    public static Expr<Boolean> loe(Object left, Object right){
        return _bbe(NumOp.LOE, left, right);
    }
    
    public static Expr<String> lower(Expr<String> path){
        return null; 
    }
    
    public static Expr<Boolean> lt(Object left, Object right){
        return _bbe(NumOp.LT, left, right);
    }
    
    public static Expr<Boolean> ne(Object left, Object right){
        return _bbe(NumOp.NE, left, right);
    }    
    
    public static Expr<Boolean> not(Expr<Boolean> left){
        return _bue(BoOp.NE, left);
    }
    
    public static Expr<Boolean> or(Expr<Boolean> left, Expr<Boolean> right){
        return _bbe(BoOp.OR, left, right);
    }

    
}
