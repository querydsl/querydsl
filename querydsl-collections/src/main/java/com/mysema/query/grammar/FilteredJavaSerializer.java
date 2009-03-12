/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import java.util.List;

import com.mysema.query.grammar.Ops;
import com.mysema.query.grammar.Ops.Op;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Path;

/**
 * FilteredJavaSerializer provides
 *
 * @author tiwe
 * @version $Id$
 */
public class FilteredJavaSerializer extends JavaSerializer{
    
    private boolean skipPath = false;
    
    private List<Expr<?>> exprs;
    
    private Expr<?> last;
    
    private boolean inNotOperation = false;
    
    public FilteredJavaSerializer(JavaOps ops, List<Expr<?>> expressions) {
        super(ops);
        this.exprs = expressions;
        this.last = exprs.get(exprs.size()-1);
    }
    
    @Override
    protected void visitOperation(Class<?> type, Op<?> operator, Expr<?>... args) {
        if (!skipPath){
            boolean unknownPaths = false;
            boolean knownPaths = false;
            boolean targetIncluded = false;
            // iterate over arguments
            for (Expr<?> expr : args){
                if (expr instanceof Path){
                    Path<?> path = ((Path<?>)expr).getRoot();
                    if (!exprs.contains(path)){
                        unknownPaths = true;
                    }else if (path.equals(last)){
                        targetIncluded = true;
                    }else{
                        knownPaths = true;
                    }
                }
            }
            if (unknownPaths){
                skipPath = true;    
            }else if (!targetIncluded && knownPaths){
                skipPath = true;
            }else{
                boolean old = inNotOperation;
                inNotOperation = (operator == Ops.NOT) ? !old : old;
                super.visitOperation(type, operator, args);
                inNotOperation = old;
            }                
        }
        if (skipPath){        
            if (type.equals(Boolean.class)){
                append(inNotOperation ? "false" : "true");
                skipPath = false;
            }
        }        
    }
    
    @Override
    protected void appendOperationResult(Op<?> operator, String result){
        if (!skipPath){
            super.appendOperationResult(operator, result);
        }
    }
    
}
