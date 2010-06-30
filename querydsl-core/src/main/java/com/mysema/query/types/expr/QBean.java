/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import java.util.List;

import org.apache.commons.collections15.BeanMap;

import com.mysema.query.QueryException;
import com.mysema.query.types.EConstructor;
import com.mysema.query.types.Expr;
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
    
    public QBean(Class<T> type, Expr<?>... args) {
        super(type, new Class[0], args);
    }
    
    @Override
    public T newInstance(Object... args){
        try {
            T rv = getType().newInstance();
            List<Expr<?>> exprs = getArgs();
            BeanMap beanMap = new BeanMap(rv);
            for (int i = 0; i < args.length; i++){
                if (exprs.get(i) instanceof Path<?>){
                    Path<?> path = (Path<?>)exprs.get(i);
                    String property = path.getMetadata().getExpression().toString();
                    beanMap.put(property, args[i]);
                }else{
                    throw new IllegalArgumentException("Only paths are supported");
                }
            }
            return rv;
        } catch (InstantiationException e) {
            throw new QueryException(e.getMessage(),e);
        } catch (IllegalAccessException e) {
            throw new QueryException(e.getMessage(),e);
        }                
    }    

}
