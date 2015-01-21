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
package com.querydsl.collections;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.primitives.Primitives;
import com.querydsl.core.QueryException;
import com.querydsl.codegen.Serializer;
import com.querydsl.core.support.SerializerBase;
import com.querydsl.core.types.Constant;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.Operator;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathType;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.Template;

/**
 * CollQuerySerializer is a {@link Serializer} implementation for the Java language
 *
 * @author tiwe
 */
public final class CollQuerySerializer extends SerializerBase<CollQuerySerializer> {

    private static final Set<Class<?>> WRAPPER_TYPES = ImmutableSet.copyOf(Primitives.allWrapperTypes());
    
    private static final Map<Operator<?>, String> OPERATOR_SYMBOLS = Maps.newIdentityHashMap();
    
    private static final Map<Class<?>, String> CAST_SUFFIXES = Maps.newHashMap();
    
    static {
        OPERATOR_SYMBOLS.put(Ops.EQ, " == ");
        OPERATOR_SYMBOLS.put(Ops.NE, " != ");
        OPERATOR_SYMBOLS.put(Ops.GT, " > ");
        OPERATOR_SYMBOLS.put(Ops.LT, " < ");
        OPERATOR_SYMBOLS.put(Ops.GOE, " >= ");
        OPERATOR_SYMBOLS.put(Ops.LOE, " <= ");
        
        OPERATOR_SYMBOLS.put(Ops.ADD, " + ");
        OPERATOR_SYMBOLS.put(Ops.SUB, " - ");
        OPERATOR_SYMBOLS.put(Ops.MULT, " * ");
        OPERATOR_SYMBOLS.put(Ops.DIV, " / ");
        
        CAST_SUFFIXES.put(Boolean.class, ".booleanValue()");
        CAST_SUFFIXES.put(Byte.class, ".byteValue()");
        CAST_SUFFIXES.put(Character.class, ".charValue()");
        CAST_SUFFIXES.put(Double.class, ".doubleValue()");
        CAST_SUFFIXES.put(Float.class, ".floatValue()");
        CAST_SUFFIXES.put(Integer.class, ".intValue()");
        CAST_SUFFIXES.put(Long.class, ".longValue()");
        CAST_SUFFIXES.put(Short.class, ".shortValue()");
        CAST_SUFFIXES.put(String.class, ".toString()");
    }
    
    public CollQuerySerializer(CollQueryTemplates templates) {
        super(templates);
    }

    @Override
    public Void visit(Path<?> path, Void context) {
        final PathType pathType = path.getMetadata().getPathType();
        if (pathType == PathType.PROPERTY) {
            final Path<?> parent = path.getMetadata().getParent();
            final String property = path.getMetadata().getName();      
            final Class<?> parentType = parent.getType();
            try {
                // getter
                Method m = getAccessor(parentType, property);
                if (m != null && Modifier.isPublic(m.getModifiers())) {                    
                    handle(parent);
                    append(".").append(m.getName()).append("()");    
                } else {
                    // field
                    Field f = getField(parentType, property);
                    if (f != null && Modifier.isPublic(f.getModifiers())) {
                        handle(parent);
                        append(".").append(property);
                    } else {
                        // field access by reflection
                        append(CollQueryFunctions.class.getName() + ".<");
                        append(((Class)path.getType()).getName()).append(">get(");
                        handle(parent);
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
            final Template template = getTemplate(pathType);
            for (Template.Element element : template.getElements()) {
                Object rv = element.convert(args);
                if (rv instanceof Expression) {                    
                    ((Expression)rv).accept(this, context);
                } else if (element.isString()) {    
                    append(rv.toString());
                } else {
                    visitConstant(rv);
                }
            }
        }
        return null;

    }
    
    private Method getAccessor(Class<?> owner, String property) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(owner);
            PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
            for(PropertyDescriptor pd : descriptors) {
                if(pd.getName().equals(property)) {
                    return pd.getReadMethod();
                }
            }
            return null;
        } catch (IntrospectionException e) {
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
        
        if (CAST_SUFFIXES.containsKey(targetType)) {
            append(CAST_SUFFIXES.get(targetType));
        } else {
            throw new IllegalArgumentException("Unsupported cast type " + targetType.getName());
        }
    }

    @Override
    protected void visitOperation(Class<?> type, Operator<?> operator, List<? extends Expression<?>> args) {
        if (Ops.aggOps.contains(operator)) {
            throw new UnsupportedOperationException("Aggregation operators are only supported as single expressions");
        }
        if (args.size() == 2 && OPERATOR_SYMBOLS.containsKey(operator) 
             && isPrimitive(args.get(0).getType()) && isPrimitive(args.get(1).getType())) {
            handle(args.get(0));
            append(OPERATOR_SYMBOLS.get(operator));
            handle(args.get(1));
            if (args.get(1) instanceof Constant) {
                append(CAST_SUFFIXES.get(args.get(1).getType()));
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
    
    private static boolean isPrimitive(Class<?> type) {
        return type.isPrimitive() || WRAPPER_TYPES.contains(type);
    }

    @Override
    public Void visit(FactoryExpression<?> expr, Void context) {
        visitConstant(expr);
        append(".newInstance(");
        handle(", ", expr.getArgs());
        append(")");
        return null;
    }

}
