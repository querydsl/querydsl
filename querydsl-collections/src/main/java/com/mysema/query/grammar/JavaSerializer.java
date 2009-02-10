/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import static com.mysema.query.grammar.types.PathMetadata.LISTVALUE_CONSTANT;
import static com.mysema.query.grammar.types.PathMetadata.PROPERTY;
import static com.mysema.query.grammar.types.PathMetadata.VARIABLE;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.janino.CompileException;
import org.codehaus.janino.ExpressionEvaluator;
import org.codehaus.janino.Parser.ParseException;
import org.codehaus.janino.Scanner.ScanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.grammar.Ops.Op;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Path;
import com.mysema.query.grammar.types.Expr.EConstant;
import com.mysema.query.grammar.types.ExtTypes.ExtString;
import com.mysema.query.grammar.types.PathMetadata.PathType;
import com.mysema.query.serialization.BaseSerializer;
import com.mysema.query.serialization.OperationPatterns;


/**
 * JavaSerializer is a Serializer implementation for the Java language 
 * 
 * @author tiwe
 * @version $Id$
 */
public class JavaSerializer extends BaseSerializer<JavaSerializer>{
        
    private static final Logger logger = LoggerFactory.getLogger(JavaSerializer.class);
    
    public JavaSerializer(OperationPatterns ops){
        super(ops);
    }
    
    public ExpressionEvaluator createExpressionEvaluator(List<Expr<?>> sources, Class<?> targetType) throws CompileException, ParseException, ScanException{
        if (targetType == null) throw new IllegalArgumentException("targetType was null");
        String expr = builder.toString();
                
        final Object[] constArray = constants.toArray();
        Class<?>[] types = new Class<?>[constArray.length + sources.size()];
        String[] names = new String[constArray.length + sources.size()];
        for (int i = 0; i < constArray.length; i++){
            types[i] = normalize(constArray[i].getClass());
            names[i] = "a" + (i+1);
        }
        
        int off = constArray.length;
        for (int i = 0; i < sources.size(); i++){
            types[off + i] = normalize(sources.get(i).getType());            
            names[off + i] = ((Path<?>)sources.get(i)).getMetadata().getExpression().toString();    
        }         
        
        logger.info(expr + " "+Arrays.asList(names) +" "+ Arrays.asList(types));
        
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
        
    public ExpressionEvaluator createExpressionEvaluator(List<Expr<?>> sources, Expr<?> projection) throws Exception{
        Class<?> targetType = projection.getType();
        if (targetType == null) targetType = Object.class;
        return createExpressionEvaluator(sources, targetType);
    }
    
    private Class<?> normalize(Class<?> type) {
        Class<?> newType = ClassUtils.wrapperToPrimitive(type);
        return newType != null ? newType : type;
    }
    
    protected void visit(ExtString stringPath){
        visit((Path<String>)stringPath);
    }

    @Override
    protected void visitOperation(Op<?> operator, Expr<?>... args) {
        if (operator.equals(Ops.STRING_CAST)){
            visitCast(operator, args[0], String.class);
        }else if (operator.equals(Ops.NUMCAST)){
            visitCast(operator, args[0], (Class<?>) ((EConstant<?>)args[1]).getConstant());
        }else{
            super.visitOperation(operator, args);    
        }  
    }
    
    private void visitCast(Op<?> operator, Expr<?> source, Class<?> targetType) {
        if (Number.class.isAssignableFrom(source.getType())){
            append("new ").append(source.getType().getSimpleName()).append("(");
            handle(source);
            append(")");
        }else{
            handle(source);    
        }
//        num.byteValue() 
//        num.doubleValue()
//        num.floatValue()
//        num.intValue()
//        num.longValue()
//        num.shortValue()
//        num.stringValue()
        if (Byte.class.equals(targetType)){
            append(".byteValue()");
        }else if (Double.class.equals(targetType)){
            append(".doubleValue()");
        }else if (Float.class.equals(targetType)){
            append(".floatValue()");
        }else if (Integer.class.equals(targetType)){
            append(".intValue()");
        }else if (Long.class.equals(targetType)){
            append(".longValue()");
        }else if (Short.class.equals(targetType)){
            append(".shortValue()");
        }else if (String.class.equals(targetType)){
            append(".toString()");
        }else{
            throw new IllegalArgumentException("Unsupported cast type " + targetType.getName());
        }
    }
    
    @Override
    protected void visit(Path<?> path) {
        PathType pathType = path.getMetadata().getPathType();
        String parentAsString = null, exprAsString = null;
        
        if (path.getMetadata().getParent() != null){
            parentAsString = toString((Expr<?>)path.getMetadata().getParent(), false);    
        }        
        if (pathType == VARIABLE){
            exprAsString = path.getMetadata().getExpression().toString();
        }else if (pathType == PROPERTY ){
            String prefix = "get";
            if (((Expr<?>)path).getType() != null && ((Expr<?>)path).getType().equals(Boolean.class)){
                prefix = "is";    
            }
            exprAsString = prefix+StringUtils.capitalize(path.getMetadata().getExpression().toString())+"()";
            
        }else if (pathType == LISTVALUE_CONSTANT){
            exprAsString = path.getMetadata().getExpression().toString();
            
        }else if (path.getMetadata().getExpression() != null){
            exprAsString = toString(path.getMetadata().getExpression(), false);
        }
        
        String pattern = ops.getPattern(pathType);
        if (parentAsString != null){
            append(String.format(pattern, parentAsString, exprAsString));    
        }else{
            append(String.format(pattern, exprAsString));
        }        
    }
        
}
