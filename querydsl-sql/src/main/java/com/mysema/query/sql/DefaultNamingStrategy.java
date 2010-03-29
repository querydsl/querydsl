/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import java.lang.annotation.Annotation;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import com.mysema.query.codegen.EntityType;

/**
 * DefaultNamingStrategy is the default implementation of the NamingStrategy
 * interface
 * 
 * @author tiwe
 * 
 */
public class DefaultNamingStrategy implements NamingStrategy {

    @Override
    public String getClassName(String namePrefix, String tableName) {
        return namePrefix
                + tableName.substring(0, 1).toUpperCase(Locale.ENGLISH)
                + toCamelCase(tableName.substring(1));
    }

    @Override
    public String getPropertyName(String columnName, String namePrefix,
            EntityType entityType) {
        return columnName.substring(0, 1).toLowerCase(Locale.ENGLISH)
                + toCamelCase(columnName.substring(1));
    }

    protected String toCamelCase(String str) {
        StringBuilder builder = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '_') {
                builder.append(Character.toUpperCase(str.charAt(i + 1)));
                i += 1;
            } else {
                builder.append(Character.toLowerCase(str.charAt(i)));
            }
        }
        return builder.toString();
    }

    @Override
    public String getDefaultVariableName(String namePrefix,
            EntityType entityType) {
        String simpleName = entityType.getUncapSimpleName();
        if (namePrefix.length() > 0) {
            simpleName = StringUtils.uncapitalize(simpleName.substring(namePrefix.length()));
        }
        return simpleName;
    }

    @Override
    public String getDefaultAlias(String namePrefix, EntityType entityType) {
        for (Annotation ann : entityType.getAnnotations()) {
            if (ann.annotationType().equals(Table.class)) {
                return ((Table) ann).value();
            }
        }
        return getDefaultVariableName(namePrefix, entityType);
    }

    @Override
    public String normalizeColumnName(String columnName) {
        return columnName;
    }

    @Override
    public String normalizeTableName(String tableName) {
        return tableName;
    }

}
