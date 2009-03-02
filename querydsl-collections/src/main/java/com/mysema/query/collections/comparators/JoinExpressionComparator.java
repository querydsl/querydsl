/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.comparators;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.mutable.MutableInt;

import com.mysema.query.JoinExpression;
import com.mysema.query.grammar.Ops;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Operation;
import com.mysema.query.grammar.types.Path;
import com.mysema.query.grammar.types.Expr.EBoolean;
import com.mysema.query.grammar.types.Expr.EConstant;

/**
 * JoinExpressionComparator is a comparator for Join expressions
 *
 * @author tiwe
 * @version $Id$
 */
public class JoinExpressionComparator implements Comparator<JoinExpression<?>>{
    
    private Map<Expr<?>,MutableInt> priorities = new HashMap<Expr<?>,MutableInt>();
    
    private boolean invert = false;
    
    public JoinExpressionComparator(EBoolean where, Map<Expr<?>, Iterable<?>> exprToIterable) {
        for (Expr<?> expr : exprToIterable.keySet()){
            priorities.put(expr, new MutableInt());
        }        
        if (where instanceof Operation){
            visitOperation((Operation<?,?>)where);    
        }        
    }
    
    public int comparePrioritiesOnly(JoinExpression<?> o1, JoinExpression<?> o2) {
        MutableInt p1 = priorities.get(o1.getTarget());
        MutableInt p2 = priorities.get(o2.getTarget());
        return p2.intValue() - p1.intValue();
    }    

    public int compare(JoinExpression<?> o1, JoinExpression<?> o2) {
        int rv = comparePrioritiesOnly(o1,o2);        
        if (rv == 0){
            rv = o1.hashCode() - o2.hashCode();
        }       
        return invert ? - rv : rv;
    }

    protected void visitOperation(Operation<?, ?> op) {         
        List<Expr<?>> involved = new ArrayList<Expr<?>>();
        boolean constantInvolved = false;
        
        // extract involved root paths
        for (Expr<?> expr : op.getArgs()){
            if (expr instanceof Path){
                Path<?> path = (Path<?>)expr;
                involved.add((Expr<?>)path.getRoot());
            }else if (expr instanceof EConstant){
                constantInvolved = true;
            }else if (expr instanceof Operation){
                visitOperation((Operation<?,?>)expr);
            }
        }        
        
        if (!involved.isEmpty()){
            int addition = 10;
            if (op.getOperator() == Ops.EQ_PRIMITIVE || op.getOperator() == Ops.EQ_OBJECT){
                addition *= 10;
            }
            if (constantInvolved){
                addition *= 10;
            }    
            
            // update priorities
            for (Expr<?> expr : involved){
                priorities.get(expr).add(addition);
            }    
        }                                    
    }

    public void setInvert(boolean invert) {
        this.invert = invert;        
    }
}
