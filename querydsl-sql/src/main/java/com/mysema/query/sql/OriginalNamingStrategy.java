/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import org.apache.commons.lang3.StringUtils;

import com.mysema.query.codegen.EntityType;
import com.mysema.util.JavaSyntaxUtils;

/**
 * OriginalNamingStrategy preserves the table and column names in the conversion
 *
 * @author tiwe
 *
 */
public class OriginalNamingStrategy extends AbstractNamingStrategy {
    
    @Override
    public String getClassName(String tableName) {
        return tableName;
    }

    @Override
    public String getDefaultAlias(EntityType entityType) {
        String table = (String)entityType.getData().get("table");
        return table != null ? table : getDefaultVariableName(entityType);
    }

    @Override
    public String getDefaultVariableName( EntityType entityType) {
        return StringUtils.uncapitalize(entityType.getSimpleName());
    }

    @Override
    public String getPropertyName(String columnName, EntityType entityType) {        
        return getPropertyName(columnName);
    }

    @Override
    public String getPropertyNameForForeignKey(String foreignKeyName, EntityType entityType) {
        return getPropertyName(foreignKeyName);
    }

    @Override
    public String getPropertyNameForInverseForeignKey(String foreignKeyName, EntityType entityType) {
        return "_" + foreignKeyName;
    }

    @Override
    public String getPropertyNameForPrimaryKey(String primaryKeyName, EntityType model) {
        return getPropertyName(primaryKeyName);
    }


    @Override
    public String normalizeColumnName(String columnName) {
        return columnName;
    }

    @Override
    public String normalizeTableName(String tableName) {
        return tableName;
    }
    
    @Override
    public String normalizeSchemaName(String schemaName) {
        return schemaName;
    }
        
    private String getPropertyName(String name) {
        return JavaSyntaxUtils.isReserved(name) ? name + reservedSuffix : name;
    }
    
}
