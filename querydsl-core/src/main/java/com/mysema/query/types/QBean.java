/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections15.BeanMap;

/**
 * QBean is a JavaBean populating projection type
 * 
 * @author tiwe
 *
 * @param <T>
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
            Path<?> parent = (Path<?>)path.getMetadata().getParent();
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
    
    private final Map<String, ? extends Expression<?>> bindings;
    
    private final List<Expression<?>> args;
    
    @SuppressWarnings("unchecked")
    public QBean(Path<T> type, Expression<?>... args) {
        this((Class)type.getType(), args);        
    }
    
    @SuppressWarnings("unchecked")
    public QBean(Path<T> type, Map<String, ? extends Expression<?>> bindings) {
        this((Class)type.getType(), bindings);
    }
        
    public QBean(Class<T> type, Map<String, ? extends Expression<?>> bindings) {
        super(type);
        this.args = new ArrayList<Expression<?>>(bindings.values());        
        this.bindings = bindings;
    }
    
    public QBean(Class<T> type, Expression<?>... args) {
        super(type);
        this.args = Arrays.asList(args);
        bindings = createBindings(args);        
    }

    private Map<String,Expression<?>> createBindings(Expression<?>... args) {
        HashMap<String,Expression<?>> rv = new HashMap<String,Expression<?>>(args.length);
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
                    Path<?> alias = (Path<?>)operation.getArg(1);
                    rv.put(alias.getMetadata().getExpression().toString(), expr);    
                }else{
                    throw new IllegalArgumentException("Unsupported expression " + expr);
                }
                
            }else{
                throw new IllegalArgumentException("Unsupported expression " + expr);
            }
        }
        return rv;
    }
    
    @Override
    public T newInstance(Object... args){
        try {
            T rv = getType().newInstance();
            BeanMap beanMap = new BeanMap(rv);
            for (Map.Entry<String, ? extends Expression<?>> entry : bindings.entrySet()){
                Object value = args[this.args.indexOf(entry.getValue())];
                if (value != null){
                    beanMap.put(entry.getKey(), value);    
                }                
            }
            return rv;
        } catch (InstantiationException e) {
            throw new ExpressionException(e.getMessage(),e);
        } catch (IllegalAccessException e) {
            throw new ExpressionException(e.getMessage(),e);
        }                
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
