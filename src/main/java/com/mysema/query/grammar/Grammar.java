package com.mysema.query.grammar;


import com.mysema.query.grammar.GrammarTypes.BooleanExpr;
import com.mysema.query.grammar.GrammarTypes.BooleanExprType;
import com.mysema.query.grammar.GrammarTypes.Expr;
import com.mysema.query.grammar.GrammarTypes.Order;
import com.mysema.query.grammar.GrammarTypes.OrderSpecifier;

import static com.mysema.query.grammar.InternalGrammar.*;

/**
 * Grammar provides
 *
 * @author tiwe
 * @version $Id$
 */
public class Grammar {
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
    
}
