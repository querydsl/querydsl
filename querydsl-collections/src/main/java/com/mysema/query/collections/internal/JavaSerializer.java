/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.internal;

import static com.mysema.query.grammar.types.PathMetadata.LISTVALUE_CONSTANT;
import static com.mysema.query.grammar.types.PathMetadata.PROPERTY;
import static com.mysema.query.grammar.types.PathMetadata.VARIABLE;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang.StringUtils;
import org.codehaus.janino.ExpressionEvaluator;
import org.codehaus.janino.Parser.ParseException;

import com.mysema.query.grammar.Ops.Op;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Path;
import com.mysema.query.grammar.types.Alias.Simple;
import com.mysema.query.grammar.types.Alias.ToPath;
import com.mysema.query.grammar.types.PathMetadata.PathType;
import com.mysema.query.serialization.BaseSerializer;


/**
 * HqlSerializer provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class JavaSerializer extends BaseSerializer<JavaSerializer>{
            
    // TODO : refactor to DI style
    private static final ColOps ops = new ColOps();
    
    @Override
    protected void visit(Path<?> path) {
        PathType pathType = path.getMetadata().getPathType();
        String parentAsString = null, exprAsString = null;
        
        if (path.getMetadata().getParent() != null){
            parentAsString = _toString((Expr<?>)path.getMetadata().getParent(), false);    
        }        
        if (pathType == VARIABLE){
            exprAsString = path.getMetadata().getExpression().toString();
        }else if (pathType == PROPERTY ){
            String prefix = "get";
            if (((Expr)path).getType() != null && ((Expr)path).getType().equals(Boolean.class)){
                prefix = "is";    
            }
            exprAsString = prefix+StringUtils.capitalize(path.getMetadata().getExpression().toString())+"()";
            
        }else if (pathType == LISTVALUE_CONSTANT){
            // ?!?
            
        }else if (path.getMetadata().getExpression() != null){
            exprAsString = _toString(path.getMetadata().getExpression(), false);
        }
        
        String pattern = ops.getPattern(pathType);
        if (parentAsString != null){
            _append(String.format(pattern, parentAsString, exprAsString));    
        }else{
            _append(String.format(pattern, exprAsString));
        }        
    }

    @Override
    protected void visit(Simple<?> expr) {
        // TODO Auto-generated method stub        
    }
    
    @Override
    protected void visit(ToPath expr) {
        // TODO Auto-generated method stub        
    }
    
    protected void visitOperation(Op<?> operator, Expr<?>... args) {
        String pattern = ops.getPattern(operator);
        if (pattern == null)
            throw new IllegalArgumentException("Got no operation pattern for " + operator);
        Object[] strings = new String[args.length];
        for (int i = 0; i < strings.length; i++){
            strings[i] = _toString(args[i], true);
        }
        _append(String.format(pattern, strings));
    }
    
    public ExpressionEvaluator createExpressionEvaluator(Expr<?> source, Class<?> targetType) throws Exception{
        if (targetType == null) throw new IllegalArgumentException("targetType was null");
        String expr = builder.toString();
        System.out.println(expr);
        
        final Object[] constArray = constants.toArray();
        Class<?>[] types = new Class<?>[constArray.length+1];
        String[] names = new String[constArray.length+1];
        for (int i = 0; i < constArray.length; i++){
            types[i] = constArray[i].getClass();
            names[i] = "a" + (i+1);
        }
        types[types.length-1] = source.getType();
        names[names.length-1] = ((Path<?>)source).getMetadata().getExpression().toString(); 
        
        return new ExpressionEvaluator(expr, targetType, names, types){
            @Override
            public Object evaluate(Object[] origArgs) throws InvocationTargetException{
                Object[] args = new Object[constArray.length + origArgs.length];
                System.arraycopy(constArray, 0, args, 0, constArray.length);
                System.arraycopy(origArgs, 0, args, constArray.length, origArgs.length);
                return super.evaluate(args);
            }
        };
    }
    
    public ExpressionEvaluator createExpressionEvaluator(Expr<?> source, Expr<?> projection) throws Exception{
        Class<?> targetType = projection.getType();
        if (targetType == null) targetType = Object.class;
        return createExpressionEvaluator(source, targetType);
    }
    
}
