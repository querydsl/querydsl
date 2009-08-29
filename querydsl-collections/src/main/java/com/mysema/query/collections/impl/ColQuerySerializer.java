/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mysema.query.collections.ColQueryTemplates;
import com.mysema.query.serialization.SerializerBase;
import com.mysema.query.types.Template;
import com.mysema.query.types.Template.Element;
import com.mysema.query.types.expr.Constant;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Operator;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.path.Path;
import com.mysema.query.types.path.PathType;

/**
 * ColQuerySerializer is a Serializer implementation for the Java language
 * 
 * @author tiwe
 * @version $Id$
 */
public class ColQuerySerializer extends SerializerBase<ColQuerySerializer> {

    public ColQuerySerializer(ColQueryTemplates patterns) {
        super(patterns);
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

}
