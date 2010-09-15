/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.util.ArrayList;
import java.util.Arrays;
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
public class QBean<T> implements FactoryExpression<T>{
 
    private static final long serialVersionUID = -8210214512730989778L;

    private final Class<T> type;
    
    private final Map<String,Expression<?>> bindings;
    
    private final List<Expression<?>> args;
    
    @SuppressWarnings("unchecked")
    public QBean(Path<T> type, Expression<?>... args) {
        this((Class)type.getType(), args);        
    }
    
    @SuppressWarnings("unchecked")
    public QBean(Path<T> type, Map<String,Expression<?>> bindings) {
        this((Class)type.getType(), bindings);
    }
        
    public QBean(Class<T> type, Map<String,Expression<?>> bindings) {
        this.type = type;
        this.args = new ArrayList<Expression<?>>(bindings.values());        
        this.bindings = bindings;
    }
    
    public QBean(Class<T> type, Expression<?>... args) {
        this.type = type;
        this.args = Arrays.asList(args);
        bindings = createBindings(args);        
    }

    private Map<String,Expression<?>> createBindings(Expression<?>... args) {
        HashMap<String,Expression<?>> rv = new HashMap<String,Expression<?>>(args.length);
        for (Expression<?> expr : args){
            if (expr instanceof Path<?>){
                Path<?> path = (Path<?>)expr;
                rv.put(path.getMetadata().getExpression().toString(), expr);
            }else if (expr instanceof Operation<?>){
                Operation<?> operation = (Operation<?>)expr;
                Path<?> alias = (Path<?>)operation.getArg(1);
                rv.put(alias.getMetadata().getExpression().toString(), expr);
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
            for (Map.Entry<String,Expression<?>> entry : bindings.entrySet()){
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

    @Override
    public Class<? extends T> getType() {
        return type;
    }    

}
