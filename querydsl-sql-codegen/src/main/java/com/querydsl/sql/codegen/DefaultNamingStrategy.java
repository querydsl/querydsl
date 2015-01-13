/*
 * Copyright 2011, Mysema Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.sql.codegen;

import java.util.Locale;

import com.google.common.collect.ImmutableList;
import com.querydsl.codegen.EntityType;

/**
 * DefaultNamingStrategy is the default implementation of the NamingStrategy
 * interface. It changes underscore usage into camel case form.
 *
 * @author tiwe
 *
 */
public class DefaultNamingStrategy extends AbstractNamingStrategy {

    public DefaultNamingStrategy() {
        reservedSuffix = "Col";
    }

    @Override
    public String getClassName(String tableName) {
        if (tableName.length() > 1) {
            return tableName.substring(0, 1).toUpperCase(Locale.ENGLISH) +
                    toCamelCase(tableName.substring(1));
        } else {
            return tableName.toUpperCase(Locale.ENGLISH);
        }
    }

    @Override
    public String getDefaultAlias(EntityType entityType) {
        return entityType.getData().get("table").toString();
    }

    @Override
    public String getDefaultVariableName(EntityType entityType) {
        return escape(entityType, toCamelCase(entityType.getData().get("table").toString()));
    }

    @Override
    public String getForeignKeysVariable(EntityType entityType) {
        return escape(entityType, foreignKeysVariable);
    }

    @Override
    public String getPrimaryKeysVariable(EntityType entityType) {
        return escape(entityType, primaryKeysVariable);
    }

    @Override
    public String getPropertyName(String columnName, EntityType entityType) {
        if (columnName.length() > 1) {
            String normalized = normalizePropertyName(columnName);
            return normalizePropertyName(normalized.substring(0, 1).toLowerCase(Locale.ENGLISH) +
                    toCamelCase(normalized.substring(1)));
        } else {
            return columnName.toLowerCase(Locale.ENGLISH);
        }
    }

    @Override
    public String getPropertyNameForForeignKey(String fkName, EntityType entityType) {
        if (fkName.toLowerCase().startsWith("fk_")) {
            fkName = fkName.substring(3) + "_" + fkName.substring(0,2);
        }
        return escape(entityType, getPropertyName(fkName, entityType));
    }

    @Override
    public String getPropertyNameForInverseForeignKey(String fkName, EntityType entityType) {
        return "_" + getPropertyNameForForeignKey(fkName, entityType);
    }


    @Override
    public String getPropertyNameForPrimaryKey(String pkName, EntityType entityType) {
        if (pkName.toLowerCase().startsWith("pk_")) {
            pkName = pkName.substring(3) + "_" + pkName.substring(0,2);
        }
        String propertyName = getPropertyName(pkName, entityType);
        for (String candidate : ImmutableList.of(propertyName, propertyName + "Pk")) {
            if (!entityType.getEscapedPropertyNames().contains(candidate)) {
                return candidate;
            }
        }
        return escape(entityType, propertyName);
    }

    protected String normalizePropertyName(String name) {
        return Naming.normalize(name, reservedSuffix);
    }

    protected String toCamelCase(String str) {
        boolean toLower = str.toUpperCase().equals(str);
        StringBuilder builder = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            if (i < str.length() - 1 && (str.charAt(i) == '_' || str.charAt(i) == ' ')) {
                i += 1;
                if (i < str.length()) {
                    builder.append(Character.toUpperCase(str.charAt(i)));
                }
            } else if (toLower) {
                builder.append(Character.toLowerCase(str.charAt(i)));
            } else {
                builder.append(str.charAt(i));
            }
        }
        return builder.toString();
    }

}
