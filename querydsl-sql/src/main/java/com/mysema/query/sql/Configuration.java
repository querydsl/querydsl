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
import com.mysema.query.types.Path;

/**
 * Configuration for SQLQuery instances
 * 
 * @author tiwe
 *
 */
public class Configuration {
    
    public static final Configuration DEFAULT = new Configuration(new SQLTemplates("\"",false));
    
    private final JDBCTypeMapping jdbcTypeMapping = new JDBCTypeMapping();
    
    private final JavaTypeMapping javaTypeMapping = new JavaTypeMapping();
    
    private final SQLTemplates templates;

    public Configuration(SQLTemplates templates) {       
        this.templates = templates;
    }

    public SQLTemplates getTemplates() {
        return templates;
    }
    
    public Class<?> getJavaType(int sqlType, String tableName, String columnName) {
        Type<?> type = javaTypeMapping.getType(tableName, columnName);
        if (type != null){
            return type.getReturnedClass();
        }else{
            return jdbcTypeMapping.get(sqlType);
        }
    }
    
    @Nullable    
    public <T> T get(ResultSet rs, @Nullable Path<?> path, int i, Class<T> clazz) throws SQLException {        
        Type<T> type = getType(path, clazz);
        return type.getValue(rs, i);
    }
    
    @SuppressWarnings("unchecked")
    public <T> int set(PreparedStatement stmt, Path<?> path, int i, T value) throws SQLException{
        Type<T> type = getType(path, (Class)value.getClass());
        type.setValue(stmt, i, value);
        return type.getSQLTypes().length;        
    }

    @SuppressWarnings("unchecked")
    private <T> Type<T> getType(@Nullable Path<?> path, Class<T> clazz){
        if (path != null && path.getMetadata().getParent() instanceof RelationalPath){
            String table = path.getMetadata().getParent().getAnnotatedElement().getAnnotation(Table.class).value();
            String column = path.getMetadata().getExpression().toString();
            Type<T> type = (Type)javaTypeMapping.getType(table, column);
            if (type != null){
                return type;
            }                        
        }
        return javaTypeMapping.getType(clazz);
    }
    
    public void register(Type<?> type) {
        javaTypeMapping.register(type);
    }
        
    public void setType(int sqlType, Class<?> javaType) {
        jdbcTypeMapping.register(sqlType, javaType);
    }

    public void setType(String table, String column, Type<?> type) {
        javaTypeMapping.setType(table, column, type);
    }

        
}
