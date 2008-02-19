package com.mysema.query.grammar;

import com.mysema.query.grammar.GrammarTypes.BooleanBinaryExpr;
import com.mysema.query.grammar.GrammarTypes.BooleanExpr;
import com.mysema.query.grammar.GrammarTypes.BooleanExprType;
import com.mysema.query.grammar.GrammarTypes.BooleanUnaryExpr;
import com.mysema.query.grammar.GrammarTypes.ConstantExpr;
import com.mysema.query.grammar.GrammarTypes.Expr;


/**
 * InternalGrammar provides
 *
 * @author tiwe
 * @version $Id$
 */
class InternalGrammar {
    static Expr co(Object str){
        ConstantExpr e = new ConstantExpr();
        e.constant = str;
        return e;
    }    
    static BooleanBinaryExpr bbe(BooleanExprType type,Object left, Object right){
        BooleanBinaryExpr bbe = new BooleanBinaryExpr();
        bbe.type = type;
        bbe.left = left instanceof Expr ? (Expr)left : co(left);
        bbe.right = right instanceof Expr ? (Expr)right : co(left);
        return bbe;        
    }
    static BooleanUnaryExpr bue(BooleanExprType type, BooleanExpr left){
        BooleanUnaryExpr bue = new BooleanUnaryExpr();
        bue.type = type;
        bue.left = left;
        return bue;
    }

}
