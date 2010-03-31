/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.hibernate.sql;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.hibernate.transform.ResultTransformer;

import com.mysema.query.QueryException;
import com.mysema.query.types.EConstructor;

/**
 * @author tiwe
 *
 */
public final class ConstructorResultTransformer implements ResultTransformer{

    private static final long serialVersionUID = -3625957233853100239L;

    private transient Constructor<?> constructor;
    
    public ConstructorResultTransformer(EConstructor<?> constructor){
        this.constructor = constructor.getJavaConstructor();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List transformList(List collection) {
        return collection;
    }

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        try {
            return constructor.newInstance(tuple);
        } catch (InstantiationException e) {
            throw new QueryException(e);
        } catch (IllegalAccessException e) {
            throw new QueryException(e);
        } catch (InvocationTargetException e) {
            throw new QueryException(e);
        }
    }

}
