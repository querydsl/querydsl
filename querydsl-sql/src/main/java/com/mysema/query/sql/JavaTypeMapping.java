/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.mysema.commons.lang.Pair;
import com.mysema.query.sql.types.*;
import com.mysema.util.ReflectionUtils;

/**
 * JavaTypeMapping provides a mapping from Class to Type instances
 * 
 * @author tiwe
 *
 */
public class JavaTypeMapping {

    private static final Map<Class<?>,Type<?>> defaultTypes = new HashMap<Class<?>,Type<?>>();
    
    static{
        registerDefault(new BigDecimalType());
        registerDefault(new BlobType());
        registerDefault(new BooleanType());
        registerDefault(new BytesType());
        registerDefault(new ByteType());
        registerDefault(new CharacterType());
        registerDefault(new ClobType());
        registerDefault(new DateType());
        registerDefault(new DoubleType());
        registerDefault(new FloatType());
        registerDefault(new IntegerType());
        registerDefault(new LongType());
        registerDefault(new ObjectType());
        registerDefault(new ShortType());
        registerDefault(new StringType());
        registerDefault(new TimestampType());
        registerDefault(new TimeType());
        registerDefault(new URLType());
        registerDefault(new UtilDateType());
    }

    private static void registerDefault(Type<?> type) {
        defaultTypes.put(type.getReturnedClass(), type);
    }
    
    private final Map<Class<?>,Type<?>> typeByClass = new HashMap<Class<?>,Type<?>>();
    
    private final Map<Class<?>,Type<?>> resolvedTypesByClass = new HashMap<Class<?>,Type<?>>();
    
    private final Map<Pair<String,String>, Type<?>> typeByColumn = new HashMap<Pair<String,String>,Type<?>>();
    
    @Nullable
    public Type<?> getType(String table, String column){
        return typeByColumn.get(Pair.of(table.toLowerCase(), column.toLowerCase())); 
    }
    
    @SuppressWarnings("unchecked")
    public <T> Type<T> getType(Class<T> clazz){
        Type<?> resolvedType = resolvedTypesByClass.get(clazz);
        if (resolvedType == null) {
            resolvedType = findType(clazz);
            if (resolvedType != null) {
                resolvedTypesByClass.put(clazz, resolvedType);
            }
        }
        if (resolvedType == null) {
            throw new IllegalArgumentException("Got not type for " + clazz.getName());
        } else {
            return (Type<T>) resolvedType;
        }
    }

    @Nullable
    private Type<?> findType(Class<?> clazz) {
        //Look for a registered type in the class hierarchy
        Class<?> cl = clazz;
        while (!cl.equals(Object.class)){
            if (typeByClass.containsKey(cl)){
                return typeByClass.get(cl);
            }else if (defaultTypes.containsKey(cl)){
                return defaultTypes.get(cl);
            }    
            cl = cl.getSuperclass(); 
        }
        
        //Look for a registered type in any implemented interfaces
        Set<Class<?>> interfaces = ReflectionUtils.getImplementedInterfaces(clazz);
        for (Class<?> itf : interfaces) {
            if (typeByClass.containsKey(itf)){
                return typeByClass.get(itf);
            }else if (defaultTypes.containsKey(itf)){
                return defaultTypes.get(itf);
            }
        }
        return null;
    }
    
    public void register(Type<?> type) {
        typeByClass.put(type.getReturnedClass(), type);
        // Clear previous resolved types, so they won't impact future lookups
        resolvedTypesByClass.clear();
    }

    public void setType(String table, String column, Type<?> type) {
        typeByColumn.put(Pair.of(table.toLowerCase(), column.toLowerCase()), type);        
    }
    
}