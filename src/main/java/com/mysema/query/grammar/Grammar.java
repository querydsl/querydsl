package com.mysema.query.grammar;


import com.mysema.query.grammar.Types.*;

/**
 * Grammar provides
 *
 * @author tiwe
 * @version $Id$
 */
public class Grammar {
    // order
    
    public static <A> OrderSpecifier<A> asc(Expr<A> target){
        return _asc(target);
    }
    public static <A> OrderSpecifier<A> desc(Expr<A> target){
        return _desc(target);
    }
        
    // boolean
    
    public static Expr<Boolean> and(Expr<Boolean> left, Expr<Boolean> right){
        return _bbe(BoOp.AND, left, right);
    }    
    public static Expr<Boolean> not(Expr<Boolean> left){
        return _bue(BoOp.NE, left);
    }    
    public static Expr<Boolean> or(Expr<Boolean> left, Expr<Boolean> right){
        return _bbe(BoOp.OR, left, right);
    }
    
    // number compariosn
    
    public static Expr<Boolean> eq(Object left, Object right){
        return _bbe(BoOp.EQ, left, right);
    }
    public static Expr<Boolean> goe(Object left, Object right){
        return _bbe(BoOp.GOE, left, right);
    }
    public static Expr<Boolean> gt(Object left, Object right){
        return _bbe(BoOp.GT, left, right);
    }    
    public static Expr<Boolean> loe(Object left, Object right){
        return _bbe(BoOp.LOE, left, right);
    }      
    public static Expr<Boolean> lt(Object left, Object right){
        return _bbe(BoOp.LT, left, right);
    }     
    public static Expr<Boolean> ne(Object left, Object right){
        return _bbe(BoOp.NE, left, right);
    }     

    // string comparison
    
    public static Expr<Boolean> like(Expr<String> left, String right){
        return _bbe(BoOp.LIKE, left, right);
    }
    
    public static Expr<String> lower(Expr<String> path){
        return null; 
    }
    
    // arithmetic operations
    
    // TODO : +,-,*,/,mod,div
    
    // order
    
    static <A> OrderSpecifier<A> _asc(Expr<A> target) {
        OrderSpecifier<A> os = new OrderSpecifier<A>();
        os.order = Order.ASC;
        os.target = target;
        return os;
    }
    
    static <A> OrderSpecifier<A> _desc(Expr<A> target) {
        OrderSpecifier<A> os = new OrderSpecifier<A>();
        os.order = Order.DESC;
        os.target = target;
        return os;
    }
    
    // constants
    
    static <A> Expr<A> _co(A obj){
        ConstantExpr<A> e = new ConstantExpr<A>();
        e.constant = obj;
        return e;
    }    
    
    // boolean
    
    static BooleanBinaryExpr _bbe(BoOp type,Object left, Object right){
        BooleanBinaryExpr bbe = new BooleanBinaryExpr();
        bbe.type = type;
        bbe.left = left instanceof Expr ? (Expr)left : _co(left);
        bbe.right = right instanceof Expr ? (Expr)right : _co(left);
        return bbe;        
    }
    static BooleanUnaryExpr _bue(BoOp type, Expr<Boolean> left){
        BooleanUnaryExpr bue = new BooleanUnaryExpr();
        bue.type = type;
        bue.left = left;
        return bue;
    }

    
}
