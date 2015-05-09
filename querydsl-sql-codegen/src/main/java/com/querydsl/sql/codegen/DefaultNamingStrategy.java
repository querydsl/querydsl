/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
 * {@code DefaultNamingStrategy} is the default implementation of the {@link NamingStrategy}
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
        String className = tableName.substring(0, 1).toUpperCase(Locale.ENGLISH) +
                toCamelCase(tableName.substring(1));
        return normalizeJavaName(className);
    }

    @Override
    public String getDefaultAlias(EntityType entityType) {
        return entityType.getData().get("table").toString();
    }

    @Override
    public String getDefaultVariableName(EntityType entityType) {
        String variable = escape(entityType, toCamelCase(entityType.getData().get("table").toString()));
        return normalizeJavaName(variable);
    }

    @Override
    public String getForeignKeysVariable(EntityType entityType) {
        String variable = escape(entityType, foreignKeysVariable);
        return normalizeJavaName(variable);
    }

    @Override
    public String getPrimaryKeysVariable(EntityType entityType) {
        String variable = escape(entityType, primaryKeysVariable);
        return normalizeJavaName(variable);
    }

    @Override
    public String getPropertyName(String columnName, EntityType entityType) {
        String normalized = normalizeJavaName(columnName);
        return normalizeJavaName(normalized.substring(0, 1).toLowerCase(Locale.ENGLISH) +
                toCamelCase(normalized.substring(1)));
    }

    @Override
    public String getPropertyNameForForeignKey(String fkName, EntityType entityType) {
        if (fkName.toLowerCase().startsWith("fk_")) {
            fkName = fkName.substring(3) + "_" + fkName.substring(0,2);
        }
        String propertyName = escape(entityType, getPropertyName(fkName, entityType));
        return normalizeJavaName(propertyName);
    }

    @Override
    public String getPropertyNameForInverseForeignKey(String fkName, EntityType entityType) {
        String propertyName = "_" + getPropertyNameForForeignKey(fkName, entityType);
        return normalizeJavaName(propertyName);
    }


    @Override
    public String getPropertyNameForPrimaryKey(String pkName, EntityType entityType) {
        if (pkName.toLowerCase().startsWith("pk_")) {
            pkName = pkName.substring(3) + "_" + pkName.substring(0,2);
        }
        String propertyName = getPropertyName(pkName, entityType);
        for (String candidate : ImmutableList.of(propertyName, propertyName + "Pk")) {
            if (!entityType.getEscapedPropertyNames().contains(candidate)) {
                return normalizeJavaName(candidate);
            }
        }
        return normalizeJavaName(escape(entityType, propertyName));
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
