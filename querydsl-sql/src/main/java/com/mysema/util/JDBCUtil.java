/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.util;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.ClassUtils;

import com.mysema.query.types.Param;
import com.mysema.query.types.ParamNotSetException;

/**
 * @author tiwe
 *
 */
public final class JDBCUtil {
    
    private JDBCUtil(){}
    
    public static void setParameters(PreparedStatement stmt, Collection<Object> objects, Map<Param<?>, Object> params){
        int counter = 1;
        for (Object o : objects) {
            try {
                if (Param.class.isInstance(o)){
                    if (!params.containsKey(o)){
                        throw new ParamNotSetException((Param<?>) o);
                    }
                    o = params.get(o);
                }
                setParameter(stmt, counter++, o);
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException(e);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            } catch (InvocationTargetException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
    
    // TODO : don't use reflection here
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
