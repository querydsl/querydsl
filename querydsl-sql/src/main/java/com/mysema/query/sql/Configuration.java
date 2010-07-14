/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.mysema.query.sql.types.*;
import com.mysema.query.types.Param;
import com.mysema.query.types.ParamNotSetException;

/**
 * @author tiwe
 *
 */
public class Configuration {
    
    private Map<Class<?>,Type<?>> types = new HashMap<Class<?>,Type<?>>();

    public Configuration() {
        register(new BigDecimalType());
        register(new BlobType());
        register(new BooleanType());
        register(new BytesType());
        register(new ByteType());
        register(new CharacterType());
        register(new ClobType());
        register(new DateType());
        register(new DoubleType());
        register(new FloatType());
        register(new IntegerType());
        register(new LongType());
        register(new ObjectType());
        register(new ShortType());
        register(new StringType());
        register(new TimestampType());
        register(new TimeType());
        register(new URLType());
        register(new UtilDateType());
        
    }

    public void register(Type<?> type) {
        types.put(type.getReturnedClass(), type);
    }
    
    @SuppressWarnings("unchecked")
    private <T> Type<T> getType(Class<T> clazz){
        if (types.containsKey(clazz)){
            return (Type<T>) types.get(clazz);    
        }else{
            throw new IllegalArgumentException("Got not type for " + clazz.getName());
        }        
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
    
    public void setParameters(PreparedStatement stmt, Collection<Object> objects, Map<Param<?>, Object> params){
        int counter = 1;
        for (Object o : objects) {
            try {
                if (Param.class.isInstance(o)){
                    if (!params.containsKey(o)){
                        throw new ParamNotSetException((Param<?>) o);
                    }
                    o = params.get(o);
                }
                counter += set(stmt, counter, o);
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
    
}
