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

import com.mysema.commons.lang.Pair;
import com.mysema.query.sql.types.*;

/**
 * Configuration for SQLQuery instances
 * 
 * @author tiwe
 *
 */
public class Configuration {
    
    private final Map<Pair<String,String>, Type<?>> typeByColumn = new HashMap<Pair<String,String>,Type<?>>();
    
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
        typeByColumn.put(Pair.of(table, column), type);        
    }
    

    
}
