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


/**
 * JavaSerializer is a Serializer implementation for the Java language 
 * 
 * @author tiwe
 * @version $Id$
 */
public class JavaSerializer extends BaseSerializer<JavaSerializer>{
        
    private static final Logger logger = LoggerFactory.getLogger(JavaSerializer.class);
    
    public JavaSerializer(JavaOps ops){
        super(ops);
    }
    
    protected static Object[] combine(int size, Object[]... arrays){
        int offset = 0;
        Object[] target = new Object[size];
        for(Object[] arr : arrays){
            System.arraycopy(arr, 0, target, offset, arr.length);
            offset += arr.length;
        }
        return target;
    }

    /**
     * Create an ExpressionEvaluator for the given sources and targetType 
     * 
     * @param sources
     * @param targetType
     * @return
     * @throws CompileException
     * @throws ParseException
     * @throws ScanException
     */
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
        
        if (logger.isInfoEnabled()){
            logger.info(expr + " "+Arrays.asList(names) +" "+ Arrays.asList(types));    
        }        
        
        return instantiateExpressionEvaluator(targetType, expr, constArray,
                types, names);
    }
    
    /**
     * Create an ExpressionEvaluator for the given sources and projection
     * 
     * @param sources
     * @param projection
     * @return
     * @throws Exception
     */
    public ExpressionEvaluator createExpressionEvaluator(List<Expr<?>> sources, Expr<?> projection) throws Exception{
        Class<?> targetType = projection.getType();
        if (targetType == null) targetType = Object.class;
        return createExpressionEvaluator(sources, targetType);
    }
        
    /**
     * Instantiate a new ExpressionEvaluator
     * 
     * @param targetType
     * @param expr
     * @param constArray
     * @param types
     * @param names
     * @return
     * @throws CompileException
     * @throws ParseException
     * @throws ScanException
     */
    protected ExpressionEvaluator instantiateExpressionEvaluator(
            Class<?> targetType, String expr, final Object[] constArray,
            Class<?>[] types, String[] names) throws CompileException,
            ParseException, ScanException {
        return new ExpressionEvaluator(expr, targetType, names, types){
            @Override
            public Object evaluate(Object[] origArgs) throws InvocationTargetException{
                return super.evaluate(combine(constArray.length + origArgs.length, constArray, origArgs));
            }
        };
    }
    
    /**
     * Changes wrapper types to primitive types
     * 
     * @param type
     * @return
     */
    private Class<?> normalize(Class<?> type) {
        Class<?> newType = ClassUtils.wrapperToPrimitive(type);
        return newType != null ? newType : type;
    }
    
    protected void visit(ExtString stringPath){
        visit((Path<String>)stringPath);
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
    protected void visitOperation(Class<?> type, Op<?> operator, Expr<?>... args) {
        if (operator.equals(Ops.STRING_CAST)){
            visitCast(operator, args[0], String.class);
        }else if (operator.equals(Ops.NUMCAST)){
            visitCast(operator, args[0], (Class<?>) ((EConstant<?>)args[1]).getConstant());
        }else{
            super.visitOperation(type, operator, args);    
        }  
    }
        
}
