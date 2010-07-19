/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.mysema.query.sql.types.*;

/**
 * Configuration for SQLQuery instances
 * 
 * @author tiwe
 *
 */
public class Configuration {
    
    private static final Map<Class<?>,Type<?>> defaultTypes = new HashMap<Class<?>,Type<?>>();
    
    private static void registerDefault(Type<?> type) {
        defaultTypes.put(type.getReturnedClass(), type);
    }

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
    
    private final Map<Class<?>,Type<?>> types = new HashMap<Class<?>,Type<?>>();
    
    private final SQLTemplates templates;

    public Configuration(SQLTemplates templates) {        
          
        this.templates = templates;
    }
    
    public SQLTemplates getTemplates() {
        return templates;
    }

    public void register(Type<?> type) {
        types.put(type.getReturnedClass(), type);
    }
    
    @Nullable    
    public <T> T get(ResultSet rs, int i, Class<T> clazz) throws SQLException {        
        Type<T> type = getType(clazz);
        return type.getValue(rs, i);
    }
    
    @SuppressWarnings("unchecked")
    public <T> int set(PreparedStatement stmt, int i, T value) throws SQLException{
        Type<T> type = getType((Class)value.getClass());
        type.setValue(stmt, i, value);
        return type.getSQLTypes().length;        
    }
    
    @SuppressWarnings("unchecked")
    private <T> Type<T> getType(Class<T> clazz){
        if (types.containsKey(clazz)){
            return (Type<T>) types.get(clazz);
        }else if (defaultTypes.containsKey(clazz)){
            return (Type<T>) defaultTypes.get(clazz);
        }else{
            throw new IllegalArgumentException("Got not type for " + clazz.getName());
        }        
    }
    

    
}
