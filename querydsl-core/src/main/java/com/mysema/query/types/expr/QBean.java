/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections15.BeanMap;

import com.mysema.query.QueryException;
import com.mysema.query.types.Expr;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.Operation;
import com.mysema.query.types.Path;
import com.mysema.query.types.Visitor;

/**
 * QBean is a JavaBean populating projection type
 * 
 * @author tiwe
 *
 * @param <T>
 */
public class QBean<T> extends ESimple<T> implements FactoryExpression<T>{
 
    private static final long serialVersionUID = -8210214512730989778L;

    private final Map<String,Expr<?>> bindings;
    
    private final List<Expr<?>> args;
    
    @SuppressWarnings("unchecked")
    public QBean(Path<T> type, Expr<?>... args) {
        this((Class)type.getType(), args);        
    }
    
    @SuppressWarnings("unchecked")
    public QBean(Path<T> type, Map<String,Expr<?>> bindings) {
        this((Class)type.getType(), bindings);
    }
        
    public QBean(Class<T> type, Map<String,Expr<?>> bindings) {
        super(type);
        this.args = new ArrayList<Expr<?>>(bindings.values());        
        this.bindings = bindings;
    }
    
    public QBean(Class<T> type, Expr<?>... args) {
        super(type);
        this.args = Arrays.asList(args);
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
            BeanMap beanMap = new BeanMap(rv);
            for (Map.Entry<String,Expr<?>> entry : bindings.entrySet()){
                beanMap.put(entry.getKey(), args[this.args.indexOf(entry.getValue())]);
            }
            return rv;
        } catch (InstantiationException e) {
            throw new QueryException(e.getMessage(),e);
        } catch (IllegalAccessException e) {
            throw new QueryException(e.getMessage(),e);
        }                
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
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
    public List<Expr<?>> getArgs() {
        return args;
    }    

}
