package com.mysema.query;

import com.mysema.query.QueryDsl.BooleanBinaryExpr;
import com.mysema.query.QueryDsl.BooleanExpr;
import com.mysema.query.QueryDsl.BooleanExprType;
import com.mysema.query.QueryDsl.BooleanUnaryExpr;
import com.mysema.query.QueryDsl.ConstantExpr;
import com.mysema.query.QueryDsl.DomainType;
import com.mysema.query.QueryDsl.Expr;
import com.mysema.query.QueryDsl.Order;
import com.mysema.query.QueryDsl.OrderSpecifier;
import com.mysema.query.QueryDsl.Reference;

/**
 * Grammar provides
 *
 * @author tiwe
 * @version $Id$
 */
public class Grammar {
    
    // internal
    
    private static Expr co(Object str){
        ConstantExpr e = new ConstantExpr();
        e.constant = str;
        return e;
    }    
    private static BooleanBinaryExpr bbe(BooleanExprType type,Object left, Object right){
        BooleanBinaryExpr bbe = new BooleanBinaryExpr();
        bbe.type = type;
        bbe.left = left instanceof Expr ? (Expr)left : co(left);
        bbe.right = right instanceof Expr ? (Expr)right : co(left);
        return bbe;        
    }
    private static BooleanUnaryExpr bue(BooleanExprType type, BooleanExpr left){
        BooleanUnaryExpr bue = new BooleanUnaryExpr();
        bue.type = type;
        bue.left = left;
        return bue;
    }
        
    // order
    
    public static OrderSpecifier asc(Expr target){
        OrderSpecifier os = new OrderSpecifier();
        os.order = Order.ASC;
        os.target = target;
        return os;
    }    
    public static OrderSpecifier desc(Expr target){
        OrderSpecifier os = new OrderSpecifier();
        os.order = Order.DESC;
        os.target = target;
        return os;
    }
        
    // boolean
    
    public static BooleanExpr and(Object left, Object right){
        return bbe(BooleanExprType.AND, left, right);
    }    
    public static BooleanExpr eq(Object left, Object right){
        return bbe(BooleanExprType.EQ, left, right);
    }
    public static BooleanExpr goe(Object left, Object right){
        return bbe(BooleanExprType.GOE, left, right);
    }
    public static BooleanExpr gt(Object left, Object right){
        return bbe(BooleanExprType.GT, left, right);
    }    
    public static BooleanExpr loe(Object left, Object right){
        return bbe(BooleanExprType.LOE, left, right);
    }      
    public static BooleanExpr lt(Object left, Object right){
        return bbe(BooleanExprType.LT, left, right);
    }     
    public static BooleanExpr ne(Object left, Object right){
        return bbe(BooleanExprType.NE, left, right);
    }     
    public static BooleanExpr not(BooleanExpr left){
        return bue(BooleanExprType.NE, left);
    }
    
    // arithmetic
    
    // TODO
    
    // expressions
    
    public static <A> DomainType<A> as(DomainType<A> r1, DomainType<A> r2){
        // ?
        return r2;
    }
    
    public static <A> Reference<A> as(Reference<A> r1, Reference<A> r2){
        // ?
        return r2;
    }
}
