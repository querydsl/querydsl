/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Nullable;

import com.mysema.query.sql.types.Type;

/**
 * Configuration for SQLQuery instances
 * 
 * @author tiwe
 *
 */
public class Configuration {
    
    public static final Configuration DEFAULT = new Configuration(new SQLTemplates());
    
    private final SQLTypeMapping sqlTypeMapping;
    
    private final JavaTypeMapping javaTypeMapping;
    
    private final SQLTemplates templates;

    public Configuration(SQLTemplates templates) {       
        this.templates = templates;
        this.sqlTypeMapping = new SQLTypeMapping();
        this.javaTypeMapping = new JavaTypeMapping();
    }

    public SQLTemplates getTemplates() {
        return templates;
    }
    
    public Class<?> getJavaType(int sqlType, String tableName, String columnName) {
        Type<?> type = javaTypeMapping.getType(tableName, columnName);
        if (type != null){
            return type.getReturnedClass();
        }else{
            return sqlTypeMapping.get(sqlType);
        }
    }
    
    @Nullable    
    public <T> T get(ResultSet rs, int i, Class<T> clazz) throws SQLException {        
        Type<T> type = javaTypeMapping.getType(clazz);
        return type.getValue(rs, i);
    }
    
    public void register(Type<?> type) {
        javaTypeMapping.register(type);
    }
    
    @SuppressWarnings("unchecked")
    public <T> int set(PreparedStatement stmt, int i, T value) throws SQLException{
        Type<T> type = javaTypeMapping.getType((Class)value.getClass());
        type.setValue(stmt, i, value);
        return type.getSQLTypes().length;        
    }

    public void setType(int sqlType, Class<?> javaType) {
        sqlTypeMapping.register(sqlType, javaType);
    }

    public void setType(String table, String column, Type<?> type) {
        javaTypeMapping.setType(table, column, type);
    }

        
}
