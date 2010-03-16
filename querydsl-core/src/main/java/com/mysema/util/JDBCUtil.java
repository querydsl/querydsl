/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.util;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.util.Collection;

import org.apache.commons.lang.ClassUtils;

import com.mysema.query.QueryException;

/**
 * @author tiwe
 *
 */
public final class JDBCUtil {
    
    private JDBCUtil(){}
    
    public static void setParameters(PreparedStatement stmt, Collection<Object> objects){
        int counter = 1;
        for (Object o : objects) {
            try {
                setParameter(stmt, counter++, o);
            } catch (NoSuchMethodException e) {
                throw new QueryException(e);
            } catch (IllegalAccessException e) {
                throw new QueryException(e);
            } catch (InvocationTargetException e) {
                throw new QueryException(e);
            }
        }
    }
    
    private static void setParameter(PreparedStatement stmt, int i, Object o) throws NoSuchMethodException, 
        IllegalAccessException, InvocationTargetException {
        Class<?> type = o.getClass();        
        String methodName = "set" + type.getSimpleName();
        if (methodName.equals("setInteger")) {
            methodName = "setInt";
        }else if (methodName.equals("setCharacter")){
            methodName = "setString";
            type = String.class;
            o = o.toString();
        }else if (methodName.equals("setDate") && type.equals(java.util.Date.class)) {
            type = java.sql.Date.class;
            o = new java.sql.Date(((java.util.Date) o).getTime());
        }
        
        type = ClassUtils.wrapperToPrimitive(type) != null ? ClassUtils.wrapperToPrimitive(type) : type;
        
        // TODO : cache methods
        PreparedStatement.class.getMethod(methodName, int.class, type).invoke(stmt, i, o);
    }

}
