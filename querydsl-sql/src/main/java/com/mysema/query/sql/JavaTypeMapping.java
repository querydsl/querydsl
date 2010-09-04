/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.mysema.commons.lang.Pair;
import com.mysema.query.sql.types.*;

/**
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

    private final Map<Pair<String,String>, Type<?>> typeByColumn = new HashMap<Pair<String,String>,Type<?>>();
    
    @Nullable
    public Type<?> getType(String table, String column){
        return typeByColumn.get(Pair.of(table.toLowerCase(), column.toLowerCase())); 
    }
    
    @SuppressWarnings("unchecked")
    public <T> Type<T> getType(Class<T> clazz){
        if (typeByClass.containsKey(clazz)){
            return (Type<T>) typeByClass.get(clazz);
        }else if (defaultTypes.containsKey(clazz)){
            return (Type<T>) defaultTypes.get(clazz);
        }else{
            throw new IllegalArgumentException("Got not type for " + clazz.getName());
        }        
    }
    
    public void register(Type<?> type) {
        typeByClass.put(type.getReturnedClass(), type);
    }

    public void setType(String table, String column, Type<?> type) {
        typeByColumn.put(Pair.of(table.toLowerCase(), column.toLowerCase()), type);        
    }
    
}
