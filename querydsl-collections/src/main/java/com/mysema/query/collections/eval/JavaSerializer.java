/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.eval;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.codehaus.janino.CompileException;
import org.codehaus.janino.ExpressionEvaluator;
import org.codehaus.janino.Parser.ParseException;
import org.codehaus.janino.Scanner.ScanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.commons.lang.Assert;
import com.mysema.query.collections.JavaPatterns;
import com.mysema.query.serialization.BaseSerializer;
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
public class JavaSerializer extends BaseSerializer<JavaSerializer> {

    private static final Logger logger = LoggerFactory
            .getLogger(JavaSerializer.class);

    public JavaSerializer(JavaPatterns patterns) {
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
    public ExpressionEvaluator createExpressionEvaluator(
            List<? extends Expr<?>> sources, Class<?> targetType)
            throws CompileException, ParseException, ScanException {
        Assert.notNull(targetType);
        String expr = normalize(builder.toString());

        final Object[] constArray = constants.toArray();
        Class<?>[] types = new Class<?>[constArray.length + sources.size()];
        String[] names = new String[constArray.length + sources.size()];
        for (int i = 0; i < constArray.length; i++) {
            types[i] = constArray[i].getClass();
            names[i] = "a" + (i + 1);
        }

        int off = constArray.length;
        for (int i = 0; i < sources.size(); i++) {
            types[off + i] = sources.get(i).getType();
            names[off + i] = ((Path<?>) sources.get(i)).getMetadata()
                    .getExpression().toString();
        }

        if (logger.isInfoEnabled()) {
            logger.info(expr + " " + Arrays.asList(names) + " "
                    + Arrays.asList(types));
        }

        return instantiateExpressionEvaluator(targetType, expr, constArray,
                types, names);
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
        String parentAsString = null, exprAsString = null;

        if (path.getMetadata().getParent() != null) {
            parentAsString = toString((Expr<?>) path.getMetadata().getParent(),
                    false);
        }
        if (pathType == PathType.VARIABLE) {
            exprAsString = path.getMetadata().getExpression().toString();
        } else if (pathType == PathType.PROPERTY) {
            String prefix = "get";
            if (((Expr<?>) path).getType() != null
                    && ((Expr<?>) path).getType().equals(Boolean.class)) {
                prefix = "is";
            }
            exprAsString = prefix
                    + StringUtils.capitalize(path.getMetadata().getExpression()
                            .toString()) + "()";

        } else if (pathType == PathType.LISTVALUE_CONSTANT) {
            exprAsString = path.getMetadata().getExpression().toString();

        } else if (path.getMetadata().getExpression() != null) {
            exprAsString = toString(path.getMetadata().getExpression(), false);
        }

        String pattern = patterns.getPattern(pathType);
        if (parentAsString != null) {
            append(String.format(pattern, parentAsString, exprAsString));
        } else {
            append(String.format(pattern, exprAsString));
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
        if (operator.equals(Ops.LIKE)) {
            // optimize like matches to startsWith and endsWith, when possible
            String right = args.get(1).toString();
            if (!right.contains("_")) {
                int lastIndex = right.lastIndexOf('%');
                if (lastIndex == right.length() - 1) {
                    operator = Ops.STARTSWITH;
                    args = Arrays
                            .<Expr<?>> asList(args.get(0),
                                    new EConstant<String>(right.substring(0,
                                            lastIndex)));
                } else if (lastIndex == 0) {
                    operator = Ops.ENDSWITH;
                    args = Arrays.<Expr<?>> asList(args.get(0),
                            new EConstant<String>(right.substring(1)));
                }
            }
            super.visitOperation(type, operator, args);
        } else if (operator.equals(Ops.STRING_CAST)) {
            visitCast(operator, args.get(0), String.class);
        } else if (operator.equals(Ops.NUMCAST)) {
            visitCast(operator, args.get(0), (Class<?>) ((EConstant<?>) args
                    .get(1)).getConstant());
        } else {
            super.visitOperation(type, operator, args);
        }
    }

}
