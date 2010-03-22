/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mysema.query.serialization.SerializerBase;
import com.mysema.query.types.Constant;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathType;
import com.mysema.query.types.SubQuery;
import com.mysema.query.types.Template;

/**
 * ColQuerySerializer is a Serializer implementation for the Java language
 * 
 * @author tiwe
 * @version $Id$
 */
public final class ColQuerySerializer extends SerializerBase<ColQuerySerializer> {
    
    private static final List<PathType> nonGeneric = Arrays.asList(
            PathType.ARRAYVALUE,
            PathType.ARRAYVALUE_CONSTANT,
            PathType.PROPERTY,
            PathType.VARIABLE
    );

    public ColQuerySerializer(ColQueryTemplates patterns) {
        super(patterns);
    }

    @Override
    public void visit(Path<?> path) {
        PathType pathType = path.getMetadata().getPathType();
        
        if (pathType == PathType.PROPERTY){            
            String prefix = "get";
            if (path.getType() != null && path.getType().equals(Boolean.class)) {
                prefix = "is";
            }
            handle((Expr<?>) path.getMetadata().getParent());
            append(".").append(prefix);
            append(StringUtils.capitalize(path.getMetadata().getExpression().toString()) + "()");
            
        }else{
            if (!nonGeneric.contains(pathType)){
                append("((").append(path.getType().getName()).append(")");
            }        
            List<Expr<?>> args = new ArrayList<Expr<?>>(2);
            if (path.getMetadata().getParent() != null){
                args.add((Expr<?>)path.getMetadata().getParent());
            }
            args.add(path.getMetadata().getExpression());            
            Template template = getTemplate(pathType);
            for (Template.Element element : template.getElements()){
                if (element.getStaticText() != null){
                    append(element.getStaticText());
                }else if (element.isAsString()){
                    append(args.get(element.getIndex()).toString());
                }else{
                    handle(args.get(element.getIndex()));
                }
            }  
            if (!nonGeneric.contains(pathType)){
                append(")");
            }
        }
        
    }

    private void visitCast(Operator<?> operator, Expr<?> source, Class<?> targetType) {
        if (Number.class.isAssignableFrom(source.getType()) && !Constant.class.isInstance(source)) {
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
            throw new IllegalArgumentException("Unsupported cast type " + targetType.getName());
        }
    }

    @Override
    protected void visitOperation(Class<?> type, Operator<?> operator, List<Expr<?>> args) {
        if (operator.equals(Ops.STRING_CAST)) {
            visitCast(operator, args.get(0), String.class);
        } else if (operator.equals(Ops.NUMCAST)) {
            visitCast(operator, args.get(0), (Class<?>) ((Constant<?>) args.get(1)).getConstant());
        } else {
            super.visitOperation(type, operator, args);
        }
    }

    @Override
    public void visit(SubQuery expr) {
        throw new IllegalArgumentException("Not supported");        
    }

}
