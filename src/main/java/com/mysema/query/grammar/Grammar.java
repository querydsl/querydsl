package com.mysema.query.grammar;


import com.mysema.query.grammar.GrammarTypes.BooleanExpr;
import com.mysema.query.grammar.GrammarTypes.Op;
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
    
    public static BooleanExpr and(BooleanExpr left, BooleanExpr right){
        return bbe(Op.AND, left, right);
    }    
    public static BooleanExpr not(BooleanExpr left){
        return bue(Op.NE, left);
    }    
    public static BooleanExpr or(BooleanExpr left, BooleanExpr right){
        return bbe(Op.OR, left, right);
    }
    
    // number compariosn
    
    public static BooleanExpr eq(Object left, Object right){
        return bbe(Op.EQ, left, right);
    }
    public static BooleanExpr goe(Object left, Object right){
        return bbe(Op.GOE, left, right);
    }
    public static BooleanExpr gt(Object left, Object right){
        return bbe(Op.GT, left, right);
    }    
    public static BooleanExpr loe(Object left, Object right){
        return bbe(Op.LOE, left, right);
    }      
    public static BooleanExpr lt(Object left, Object right){
        return bbe(Op.LT, left, right);
    }     
    public static BooleanExpr ne(Object left, Object right){
        return bbe(Op.NE, left, right);
    }     

    // string comparison
    
    public static BooleanExpr like(Object left, String right){
        return bbe(Op.LIKE, left, right);
    }
    
    // arithmetic operations
    
    // TODO
    
}
