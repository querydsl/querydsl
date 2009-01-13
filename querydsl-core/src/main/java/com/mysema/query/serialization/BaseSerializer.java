/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.serialization;

import static com.mysema.query.grammar.types.PathMetadata.LISTVALUE_CONSTANT;
import static com.mysema.query.grammar.types.PathMetadata.PROPERTY;
import static com.mysema.query.grammar.types.PathMetadata.VARIABLE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mysema.query.grammar.Ops.Op;
import com.mysema.query.grammar.types.Constructor;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Operation;
import com.mysema.query.grammar.types.Path;
import com.mysema.query.grammar.types.VisitorAdapter;
import com.mysema.query.grammar.types.Alias.ASimple;
import com.mysema.query.grammar.types.Alias.AToPath;
import com.mysema.query.grammar.types.PathMetadata.PathType;

/**
 * BaseSerializer is a stub for Serializer implementations
 *
 * @author tiwe
 * @version $Id$
 */
public abstract class BaseSerializer<A extends BaseSerializer<A>> extends VisitorAdapter<A>{
    
    protected StringBuilder builder = new StringBuilder();
    
    protected final List<Object> constants = new ArrayList<Object>();
    
    protected final OperationPatterns ops;
    
    public BaseSerializer(OperationPatterns ops){
        if (ops == null) throw new IllegalArgumentException("ops was null");
        this.ops = ops;
    }
    
    @SuppressWarnings("unchecked")
    protected final A _append(String... str) {
        for (String s : str){
            builder.append(s);    
        }        
        return (A)this;
    }
    
    @SuppressWarnings("unchecked")
    protected final A _append(String sep, List<? extends Expr<?>> expressions) {
        boolean first = true;
        for (Expr<?> expr : expressions){
            if (!first) builder.append(sep);
            handle(expr); first = false;
        }
        return (A)this;
    }
    
    @Override
    protected void visit(Path<?> path) {
        PathType pathType = path.getMetadata().getPathType();
        String parentAsString = null, exprAsString = null;
        
        if (path.getMetadata().getParent() != null){
            parentAsString = _toString((Expr<?>)path.getMetadata().getParent(),false);    
        }        
        if (pathType == PROPERTY || pathType == VARIABLE ||
              pathType == LISTVALUE_CONSTANT){
            exprAsString = path.getMetadata().getExpression().toString();
        }else if (path.getMetadata().getExpression() != null){
            exprAsString = _toString(path.getMetadata().getExpression(),false);
        }
        
        String pattern = ops.getPattern(pathType);
        if (parentAsString != null){
            _append(String.format(pattern, parentAsString, exprAsString));    
        }else{
            _append(String.format(pattern, exprAsString));
        }
        
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

    protected void visit(Constructor<?> expr){
        _append("new ")._append(expr.getType().getName())._append("(");
        _append(", ",Arrays.asList(expr.getArgs()))._append(")");
    }
        
    @Override
    protected void visit(Expr.EConstant<?> expr) {
        _append("a");
        if (!constants.contains(expr.getConstant())){
            constants.add(expr.getConstant());
            _append(Integer.toString(constants.size()));
        }else{
            _append(Integer.toString(constants.indexOf(expr.getConstant())+1));
        }     
    }
    
    protected void visit(Constructor.CArray<?> oa) {
//        _append("new Object[]{");
        _append("new ")._append(oa.getElementType().getName())._append("[]{");
        _append(", ",Arrays.asList(oa.getArgs()))._append("}");
    }
        
    @Override
    protected final void visit(Operation<?,?> expr) {
        visitOperation(expr.getOperator(), expr.getArgs());
    }
    
    @Override
    protected void visit(ASimple<?> expr) {
        throw new UnsupportedOperationException("not implemented");        
    }

    @Override
    protected void visit(AToPath expr) {
        throw new UnsupportedOperationException("not implemented");        
    }
    
    protected void visitOperation(Op<?> operator, Expr<?>... args) {        
        String pattern = ops.getPattern(operator);
        if (pattern == null)
            throw new IllegalArgumentException("Got no operation pattern for " + operator);
        Object[] strings = new String[args.length];
        int precedence = ops.getPrecedence(operator);
        for (int i = 0; i < strings.length; i++){
            boolean wrap = false;
            if (args[i] instanceof Operation){
                // wrap if outer operator precedes
                wrap = precedence < ops.getPrecedence(((Operation<?,?>)args[i]).getOperator());
            }
            strings[i] = _toString(args[i],wrap);
        }
        _append(String.format(pattern, strings));
    }

}
