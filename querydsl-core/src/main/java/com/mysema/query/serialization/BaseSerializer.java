/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.serialization;

import java.util.ArrayList;
import java.util.List;

import com.mysema.query.grammar.Ops.Op;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Operation;
import com.mysema.query.grammar.types.VisitorAdapter;

/**
 * BaseSerializer provides
 *
 * @author tiwe
 * @version $Id$
 */
public abstract class BaseSerializer<A extends BaseSerializer<A>> extends VisitorAdapter<A>{
    
    protected StringBuilder builder = new StringBuilder();
    
    protected final List<Object> constants = new ArrayList<Object>();
    
    protected final A _append(String str) {
        builder.append(str);
        return (A)this;
    }
    
    protected final String _toString(Expr<?> expr, boolean wrap) {
        StringBuilder old = builder;
        builder = new StringBuilder();
        if (wrap) builder.append("(");
        handle(expr);
        if (wrap) builder.append(")");
        String ret = builder.toString();
        builder = old;
        return ret;
    }
    
    public List<Object> getConstants(){
        return constants;
    }
        
    public String toString(){ return builder.toString(); }

    @Override
    protected void visit(Expr.Constant<?> expr) {
        _append("a");
        if (!constants.contains(expr.getConstant())){
            constants.add(expr.getConstant());
            _append(Integer.toString(constants.size()));
        }else{
            _append(Integer.toString(constants.indexOf(expr.getConstant())+1));
        }     
    }
        
    @Override
    protected final void visit(Operation<?,?> expr) {
        visitOperation(expr.getOperator(), expr.getArgs());
    }
        
    protected abstract  void visitOperation(Op<?> operator, Expr<?>... args);

}
