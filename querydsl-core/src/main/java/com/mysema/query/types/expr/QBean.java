/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections15.BeanMap;

import com.mysema.query.QueryException;
import com.mysema.query.types.EConstructor;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Operation;
import com.mysema.query.types.Path;

/**
 * QBean is a JavaBean populating projection type
 * 
 * @author tiwe
 *
 * @param <T>
 */
public class QBean<T> extends EConstructor<T>{
 
    private static final long serialVersionUID = -8210214512730989778L;

    private final Map<String,Expr<?>> bindings;
    
    @SuppressWarnings("unchecked")
    public QBean(Path<T> type, Expr<?>... args) {
        this((Class)type.getType(), args);
    }
    
    @SuppressWarnings("unchecked")
    public QBean(Path<T> type, Map<String,Expr<?>> bindings) {
        this((Class)type.getType(), bindings);
    }
        
    @SuppressWarnings("unchecked")
    public QBean(Class<T> type, Map<String,Expr<?>> bindings) {
        super(type, new Class[0], new ArrayList(bindings.values()));
        this.bindings = bindings;
    }
    
    public QBean(Class<T> type, Expr<?>... args) {
        super(type, new Class[0], args);
        bindings = createBindings(args);
    }

    private Map<String,Expr<?>> createBindings(Expr<?>... args) {
        HashMap<String,Expr<?>> rv = new HashMap<String,Expr<?>>(args.length);
        for (Expr<?> expr : args){
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
            List<Expr<?>> exprs = getArgs();
            BeanMap beanMap = new BeanMap(rv);
            for (Map.Entry<String,Expr<?>> entry : bindings.entrySet()){
                beanMap.put(entry.getKey(), args[exprs.indexOf(entry.getValue())]);
            }
            return rv;
        } catch (InstantiationException e) {
            throw new QueryException(e.getMessage(),e);
        } catch (IllegalAccessException e) {
            throw new QueryException(e.getMessage(),e);
        }                
    }    

}
