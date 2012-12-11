/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mysema.query.collections;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.common.primitives.Primitives;
import com.mysema.query.QueryException;
import com.mysema.query.codegen.Serializer;
import com.mysema.query.support.SerializerBase;
import com.mysema.query.types.Constant;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Expression;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathType;
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.Template;
import com.mysema.util.BeanUtils;

/**
 * CollQuerySerializer is a {@link Serializer} implementation for the Java language
 *
 * @author tiwe
 */
public final class CollQuerySerializer extends SerializerBase<CollQuerySerializer> {

    private static final Map<Operator<?>, String> operatorSymbols = Maps.newHashMap();
    
    private static final Map<Class<?>, String> castSuffixes = Maps.newHashMap();
    
    static {
        operatorSymbols.put(Ops.EQ, " == ");
        operatorSymbols.put(Ops.NE, " != ");
        operatorSymbols.put(Ops.GT, " > ");
        operatorSymbols.put(Ops.LT, " < ");
        operatorSymbols.put(Ops.GOE, " >= ");
        operatorSymbols.put(Ops.LOE, " <= ");
        
        castSuffixes.put(Byte.class, ".byteValue()");
        castSuffixes.put(Character.class, ".charValue()");
        castSuffixes.put(Double.class, ".doubleValue()");
        castSuffixes.put(Float.class, ".floatValue()");
        castSuffixes.put(Integer.class, ".intValue()");
        castSuffixes.put(Long.class, ".longValue()");
        castSuffixes.put(Short.class, ".shortValue()");
        castSuffixes.put(String.class, ".toString()");
    }
    
    public CollQuerySerializer(CollQueryTemplates templates) {
        super(templates);
    }

    @Override
    public Void visit(Path<?> path, Void context) {
        PathType pathType = path.getMetadata().getPathType();

        if (pathType == PathType.PROPERTY) {
            // TODO : move this to PathMetadata ?!?
            String prefix = "get";
            if (path.getType() != null && path.getType().equals(Boolean.class)) {
                prefix = "is";
            }
            String property = path.getMetadata().getName();      
            String accessor = prefix + BeanUtils.capitalize(property);
            Class<?> parentType = path.getMetadata().getParent().getType();
            try {
                // getter
                Method m = getMethod(parentType, accessor);
                if (m != null && Modifier.isPublic(m.getModifiers())) {                    
                    handle((Expression<?>) path.getMetadata().getParent());
                    append(".").append(accessor).append("()");    
                } else {
                    // field
                    Field f = getField(parentType, property);
                    if (f != null && Modifier.isPublic(f.getModifiers())) {
                        handle((Expression<?>) path.getMetadata().getParent());
                        append(".").append(property);
                    } else {
                        // field access by reflection
                        append(CollQueryFunctions.class.getName() + ".<");
                        append(((Class)path.getType()).getName()).append(">get(");
                        handle((Expression<?>) path.getMetadata().getParent());
                        append(", \""+property+"\")");
                    }
                }                
            } catch (Exception e) {
                throw new QueryException(e);
            }
            
        } else if (pathType == PathType.DELEGATE) {
            append("(");
            append("(").append(path.getType().getName()).append(")");
            path.getMetadata().getParent().accept(this, context);
            append(")");

        } else {
            List<Object> args = new ArrayList<Object>(2);
            if (path.getMetadata().getParent() != null) {
                args.add((Expression<?>)path.getMetadata().getParent());
            }
            args.add(path.getMetadata().getElement());
            Template template = getTemplate(pathType);
            for (Template.Element element : template.getElements()) {
                if (element.getStaticText() != null) {
                    append(element.getStaticText());
                } else if (element.isAsString()) {
                    append(args.get(element.getIndex()).toString());
                } else {
                    handle(args.get(element.getIndex()));                    
                }
            }
        }
        return null;

    }
    
    private Method getMethod(Class<?> owner, String method) {
        try {
            return owner.getMethod(method);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private Field getField(Class<?> owner, String field) {
        try {
            return owner.getField(field);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }
    
    @Override
    public Void visit(SubQueryExpression<?> expr, Void context) {
        throw new IllegalArgumentException("Not supported");
    }

    private void visitCast(Operator<?> operator, Expression<?> source, Class<?> targetType) {
        if (Number.class.isAssignableFrom(source.getType()) && !Constant.class.isInstance(source)) {
            append("new ").append(source.getType().getSimpleName()).append("(");
            handle(source);
            append(")");
        } else {
            handle(source);
        }
        
        if (castSuffixes.containsKey(targetType)) {
            append(castSuffixes.get(targetType));
        } else {
            throw new IllegalArgumentException("Unsupported cast type " + targetType.getName());
        }
    }

    @Override
    protected void visitOperation(Class<?> type, Operator<?> operator, List<? extends Expression<?>> args) {
        if (args.size() == 2 && operatorSymbols.containsKey(operator) 
             && isPrimitive(args.get(0).getType()) && isPrimitive(args.get(1).getType())){
            handle(args.get(0));
            append(operatorSymbols.get(operator));
            handle(args.get(1));
            if (args.get(1) instanceof Constant) {
                append(castSuffixes.get(args.get(1).getType()));
            }
            return;
        }
        
        if (operator == Ops.STRING_CAST) {
            visitCast(operator, args.get(0), String.class);
        } else if (operator == Ops.NUMCAST) {
            visitCast(operator, args.get(0), (Class<?>) ((Constant<?>) args.get(1)).getConstant());
        } else {
            super.visitOperation(type, operator, args);
        }
    }
    
    private static boolean isPrimitive(Class<?> type){
        return type.isPrimitive() || Primitives.isWrapperType(type);
    }

    @Override
    public Void visit(FactoryExpression<?> expr, Void context) {
        handle(new ConstantImpl<Object>(expr));
        append(".newInstance(");
        handle(", ", expr.getArgs());
        append(")");
        return null;
    }

}
