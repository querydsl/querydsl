/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections15.BeanMap;

import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.expr.SimpleOperation;

/**
 * QBean is a JavaBean populating projection type
 *
 * @author tiwe
 *
 * @param <T> bean type
 */
public class QBean<T> extends ExpressionBase<T> implements FactoryExpression<T>{

    private static final long serialVersionUID = -8210214512730989778L;

    private static final Map<Path<?>, String> pathToProperty = Collections.synchronizedMap(new HashMap<Path<?>, String>());

    private static Class<?> RelationalPathClass = null;

    static {
        try {
            RelationalPathClass = Class.forName("com.mysema.query.sql.RelationalPath");
        } catch (ClassNotFoundException e) {
            // do nothing
        }
    }

    private static final String resolvePropertyViaFields(Path<?> path){
        String property = pathToProperty.get(path);
        if (property == null){
            Path<?> parent = path.getMetadata().getParent();
            for (Field field : parent.getClass().getFields()){
                try {
                    if (field.get(parent) == path){
                        property = field.getName();
                        break;
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            if (property == null){
                property = path.getMetadata().getExpression().toString();
            }
            pathToProperty.put(path, property);
        }
        return property;
    }

    private static Map<String,Expression<?>> createBindings(Expression<?>... args) {
        Map<String,Expression<?>> rv = new LinkedHashMap<String,Expression<?>>(args.length);
        for (Expression<?> expr : args){
            if (expr instanceof Path<?>){
                Path<?> path = (Path<?>)expr;
                String property;
                if (path.getMetadata().getParent() != null
                   && RelationalPathClass != null
                   && RelationalPathClass.isAssignableFrom(path.getMetadata().getParent().getClass())){
                    property = resolvePropertyViaFields(path);
                }else{
                    property = path.getMetadata().getExpression().toString();
                }
                rv.put(property, expr);
            }else if (expr instanceof Operation<?>){
                Operation<?> operation = (Operation<?>)expr;
                if (operation.getOperator() == Ops.ALIAS && operation.getArg(1) instanceof Path<?>){
                    Path<?> path = (Path<?>)operation.getArg(1);
                    rv.put(path.getMetadata().getExpression().toString(), operation.getArg(0));
                }else{
                    throw new IllegalArgumentException("Unsupported expression " + expr);
                }

            }else{
                throw new IllegalArgumentException("Unsupported expression " + expr);
            }
        }
        return rv;
    }


    private final Map<String, ? extends Expression<?>> bindings;

    private final Map<String, Field> fields = new HashMap<String, Field>();

    private final List<Expression<?>> args;

    private final boolean fieldAccess;

    @SuppressWarnings("unchecked")
    public QBean(Path<T> type, Expression<?>... args) {
        this((Class)type.getType(), false, args);
    }

    @SuppressWarnings("unchecked")
    public QBean(Path<T> type, Map<String, ? extends Expression<?>> bindings) {
        this((Class)type.getType(), false, bindings);
    }

    @SuppressWarnings("unchecked")
    public QBean(Path<T> type, boolean fieldAccess, Expression<?>... args) {
        this((Class)type.getType(), fieldAccess, args);
    }

    @SuppressWarnings("unchecked")
    public QBean(Path<T> type, boolean fieldAccess, Map<String, ? extends Expression<?>> bindings) {
        this((Class)type.getType(), fieldAccess, bindings);
    }

    public QBean(Class<T> type, Map<String, ? extends Expression<?>> bindings) {
        this(type, false, bindings);
    }

    public QBean(Class<T> type, Expression<?>... args) {
        this(type, false, args);
    }

    public QBean(Class<T> type, boolean fieldAccess, Expression<?>... args) {
        this(type, fieldAccess, createBindings(args));
    }

    public QBean(Class<T> type, boolean fieldAccess, Map<String, ? extends Expression<?>> bindings) {
        super(type);
        this.bindings = bindings;
        this.args = new ArrayList<Expression<?>>(bindings.values());
        this.fieldAccess = fieldAccess;
        if (fieldAccess){
            initFields();
        }
    }

    private void initFields() {
        for (String property : bindings.keySet()){
            Class<?> beanType = type;
            while (!beanType.equals(Object.class)){
                try {
                    Field field = type.getDeclaredField(property);
                    field.setAccessible(true);
                    fields.put(property, field);
                    beanType = Object.class;
                } catch (SecurityException e) {
                    // do nothing
                } catch (NoSuchFieldException e) {
                    beanType = beanType.getSuperclass();
                }
            }
        }
    }


    @Override
    public T newInstance(Object... a){
        try {
            T rv = getType().newInstance();
            if (fieldAccess){
                for (Map.Entry<String, ? extends Expression<?>> entry : bindings.entrySet()){
                    Object value = a[this.args.indexOf(entry.getValue())];
                    if (value != null){
                        fields.get(entry.getKey()).set(rv, value);
                    }
                }
            }else{
                BeanMap beanMap = new BeanMap(rv);
                for (Map.Entry<String, ? extends Expression<?>> entry : bindings.entrySet()){
                    Object value = a[this.args.indexOf(entry.getValue())];
                    if (value != null){
                        beanMap.put(entry.getKey(), value);
                    }
                }
            }
            return rv;
        } catch (InstantiationException e) {
            throw new ExpressionException(e.getMessage(),e);
        } catch (IllegalAccessException e) {
            throw new ExpressionException(e.getMessage(),e);
        }
    }

    /**
     * Create an alias for the expression
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public Expression<T> as(Path<T> alias) {
        return OperationImpl.create((Class<T>)getType(),Ops.ALIAS, this, alias);
    }

    /**
     * Create an alias for the expression
     *
     * @return
     */
    public Expression<T> as(String alias) {
        return as(new PathImpl<T>(getType(), alias));
    }
    
    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this){
            return true;
        }else if (obj instanceof QBean<?>){
            QBean<?> c = (QBean<?>)obj;
            return args.equals(c.args) && getType().equals(c.getType());
        }else{
            return false;
        }
    }

    @Override
    public int hashCode(){
        return getType().hashCode();
    }

    @Override
    public List<Expression<?>> getArgs() {
        return args;
    }

}
