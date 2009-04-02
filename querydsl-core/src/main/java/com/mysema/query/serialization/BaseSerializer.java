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
import com.mysema.query.grammar.types.*;
import com.mysema.query.grammar.types.Alias.ASimple;
import com.mysema.query.grammar.types.Alias.AToPath;
import com.mysema.query.grammar.types.PathMetadata.PathType;

/**
 * BaseSerializer is a stub for Serializer implementations
 *
 * @author tiwe
 * @version $Id$
 */
public abstract class BaseSerializer<SubType extends BaseSerializer<SubType>> extends AbstractVisitor<SubType>{
    
    protected StringBuilder builder = new StringBuilder();
    
    protected final List<Object> constants = new ArrayList<Object>();
    
    protected final OperationPatterns ops;
    
    @SuppressWarnings("unchecked")
    private final SubType _this = (SubType)this;
    
    public BaseSerializer(OperationPatterns ops){
        if (ops == null) throw new IllegalArgumentException("ops was null");
        this.ops = ops;
    }
    
    protected final SubType append(String... str) {
        for (String s : str){
            builder.append(s);    
        }        
        return _this;
    }
        
    protected final SubType append(String sep, List<? extends Expr<?>> expressions) {
        boolean first = true;
        for (Expr<?> expr : expressions){
            if (!first) builder.append(sep);
            handle(expr); first = false;
        }
        return _this;
    }
    
    protected void visit(Path<?> path) {
        PathType pathType = path.getMetadata().getPathType();
        String parentAsString = null, exprAsString = null;
        
        if (path.getMetadata().getParent() != null){
            parentAsString = toString((Expr<?>)path.getMetadata().getParent(),false);    
        }        
        if (pathType == PROPERTY || pathType == VARIABLE ||
              pathType == LISTVALUE_CONSTANT){
            exprAsString = path.getMetadata().getExpression().toString();
        }else if (path.getMetadata().getExpression() != null){
            exprAsString = toString(path.getMetadata().getExpression(),false);
        }
        
        String pattern = ops.getPattern(pathType);
        if (parentAsString != null){
            append(String.format(pattern, parentAsString, exprAsString));    
        }else{
            append(String.format(pattern, exprAsString));
        }
        
    }
    
    protected final String toString(Expr<?> expr, boolean wrap) {
        StringBuilder old = builder;
        builder = new StringBuilder();
        if (wrap) builder.append("(");
        handle(expr);
        if (wrap) builder.append(")");
        String ret = builder.toString();
        builder = old;
        return ret;
    }
    
    protected void visit(Custom<?> expr){
        Object[] strings = new String[expr.getArgs().size()];
        for (int i = 0; i < strings.length; i++){
            strings[i] = toString(expr.getArgs().get(i),false);
        }
        append(String.format(expr.getPattern(), strings));
    }
    
    public List<Object> getConstants(){
        return constants;
    }
        
    public String toString(){ return builder.toString(); }

    protected void visit(Expr.EConstructor<?> expr){
        append("new ").append(expr.getType().getName()).append("(");
        append(", ",expr.getArgs()).append(")");
    }
        
    @Override
    protected void visit(Expr.EConstant<?> expr) {
        append("a");
        if (!constants.contains(expr.getConstant())){
            constants.add(expr.getConstant());
            append(Integer.toString(constants.size()));
        }else{
            append(Integer.toString(constants.indexOf(expr.getConstant())+1));
        }     
    }
    
    protected void visit(Expr.EArrayConstructor<?> oa) {
//        _append("new Object[]{");
        append("new ").append(oa.getElementType().getName()).append("[]{");
        append(", ",oa.getArgs()).append("}");
    }
        
    @Override
    protected final void visit(Operation<?,?> expr) {
        visitOperation(expr.getType(), expr.getOperator(), expr.getArgs());
    }
    
    @Override
    protected void visit(ASimple<?> expr) {
        throw new UnsupportedOperationException("not implemented");        
    }

    @Override
    protected void visit(AToPath expr) {
        throw new UnsupportedOperationException("not implemented");        
    }
    
    protected void visitOperation(Class<?> type, Op<?> operator, List<Expr<?>> args) {        
        String pattern = ops.getPattern(operator);
        if (pattern == null)
            throw new IllegalArgumentException("Got no pattern for " + operator);
        Object[] strings = new String[args.size()];
        int precedence = ops.getPrecedence(operator);
        for (int i = 0; i < strings.length; i++){
            boolean wrap = false;
            if (args.get(i) instanceof Operation){
                // wrap if outer operator precedes
                wrap = precedence < ops.getPrecedence(((Operation<?,?>)args.get(i)).getOperator());
            }
            strings[i] = toString(args.get(i),wrap);
        }
        appendOperationResult(operator, String.format(pattern, strings));
    }
    
    protected void appendOperationResult(Op<?> operator, String result){
        append(result);
    }
}
