/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.eval;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.codehaus.janino.CompileException;
import org.codehaus.janino.ExpressionEvaluator;
import org.codehaus.janino.Parser.ParseException;
import org.codehaus.janino.Scanner.ScanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.commons.lang.Assert;
import com.mysema.query.serialization.SerializerBase;
import com.mysema.query.types.Template;
import com.mysema.query.types.Template.Element;
import com.mysema.query.types.expr.EConstant;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Operator;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.path.Path;
import com.mysema.query.types.path.PathType;

/**
 * JavaSerializer is a Serializer implementation for the Java language
 * 
 * @author tiwe
 * @version $Id$
 */
public class JavaSerializer extends SerializerBase<JavaSerializer> {

    private static final Logger logger = LoggerFactory.getLogger(JavaSerializer.class);

    public JavaSerializer(ColQueryTemplates patterns) {
        super(patterns);
    }

    public static Object[] combine(int size, Object[]... arrays) {
        int offset = 0;
        Object[] target = new Object[size];
        for (Object[] arr : arrays) {
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
    @SuppressWarnings("unchecked")
    public ExpressionEvaluator createExpressionEvaluator(
            List<? extends Expr<?>> sources, Class<?> targetType)
            throws CompileException, ParseException, ScanException {
        Assert.notNull(targetType);
        String expr = normalize(toString());

        final Object[] constArray = constantToLabel.keySet().toArray();        
        Class<?>[] types = new Class<?>[constArray.length + sources.size()];
        String[] names = new String[constArray.length + sources.size()];
        for (int i = 0; i < constArray.length; i++) {
            if (constArray[i] instanceof List){
                types[i] = List.class;
            }else if (constArray[i] instanceof Set){
                types[i] = Set.class;
            }else if (constArray[i] instanceof Collection){
                types[i] = Collection.class;
            }else{
                types[i] = constArray[i].getClass();    
            }            
            names[i] = constantToLabel.get(constArray[i]);
        }

        int off = constArray.length;
        for (int i = 0; i < sources.size(); i++) {
            types[off + i] = sources.get(i).getType();
            names[off + i] = ((Path<?>) sources.get(i)).getMetadata()
                    .getExpression().toString();
        }

        if (logger.isInfoEnabled()) {
            logger.info(expr + " " + Arrays.asList(names) + " " + Arrays.asList(types));
        }

        return instantiateExpressionEvaluator(targetType, expr, constArray, types, names);
    }

    protected String normalize(String expr) {
        return expr;
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
        return new ExpressionEvaluator(expr, targetType, names, types) {
            @Override
            public Object evaluate(Object[] origArgs)
                    throws InvocationTargetException {
                return super.evaluate(combine(constArray.length
                        + origArgs.length, constArray, origArgs));
            }
        };
    }

    @Override
    protected void visit(Path<?> path) {
        PathType pathType = path.getMetadata().getPathType();
        
        if (pathType == PathType.PROPERTY){            
            String prefix = "get";
            if (((Expr<?>) path).getType() != null && ((Expr<?>) path).getType().equals(Boolean.class)) {
                prefix = "is";
            }
            handle((Expr<?>) path.getMetadata().getParent());
            append(".").append(prefix);
            append(StringUtils.capitalize(path.getMetadata().getExpression().toString()) + "()");
        }else{
            if (pathType.isGeneric()){
                append("((").append(path.getType().getName()).append(")");
            }        
            List<Expr<?>> args = new ArrayList<Expr<?>>(2);
            if (path.getMetadata().getParent() != null){
                args.add((Expr<?>)path.getMetadata().getParent());
            }
            args.add(path.getMetadata().getExpression());            
            Template template = templates.getTemplate(pathType);
            for (Element element : template.getElements()){
                if (element.getStaticText() != null){
                    append(element.getStaticText());
                }else if (element.isAsString()){
                    append(args.get(element.getIndex()).toString());
                }else{
                    handle(args.get(element.getIndex()));
                }
            }  
            if (pathType.isGeneric()){
                append(")");
            }
        }
        
    }

    private void visitCast(Operator<?> operator, Expr<?> source, Class<?> targetType) {
        if (Number.class.isAssignableFrom(source.getType())
                && !EConstant.class.isInstance(source)) {
            append("new ").append(source.getType().getSimpleName()).append("(");
            handle(source);
            append(")");
        } else {
            handle(source);
        }

        if (Byte.class.equals(targetType)) {
            append(".byteValue()");
        } else if (Double.class.equals(targetType)) {
            append(".doubleValue()");
        } else if (Float.class.equals(targetType)) {
            append(".floatValue()");
        } else if (Integer.class.equals(targetType)) {
            append(".intValue()");
        } else if (Long.class.equals(targetType)) {
            append(".longValue()");
        } else if (Short.class.equals(targetType)) {
            append(".shortValue()");
        } else if (String.class.equals(targetType)) {
            append(".toString()");
        } else {
            throw new IllegalArgumentException("Unsupported cast type "
                    + targetType.getName());
        }
    }

    @Override
    protected void visitOperation(Class<?> type, Operator<?> operator,
            List<Expr<?>> args) {
        if (operator.equals(Ops.STRING_CAST)) {
            visitCast(operator, args.get(0), String.class);
        } else if (operator.equals(Ops.NUMCAST)) {
            visitCast(operator, args.get(0), (Class<?>) ((EConstant<?>) args.get(1)).getConstant());
        } else {
            super.visitOperation(type, operator, args);
        }
    }

}
