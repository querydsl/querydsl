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

import com.querydsl.codegen.EntityType;

/**
 * {@code OriginalNamingStrategy} preserves the table and column names in the conversion
 *
 * @author tiwe
 *
 */
public class OriginalNamingStrategy extends AbstractNamingStrategy {

    @Override
    public String getClassName(String tableName) {
        return normalizeJavaName(tableName);
    }

    @Override
    public String getDefaultAlias(EntityType entityType) {
        return entityType.getData().get("table").toString();
    }

    @Override
    public String getDefaultVariableName(EntityType entityType) {
        String escaped = escape(entityType, entityType.getData().get("table").toString());
        return normalizeJavaName(escaped);
    }

    @Override
    public String getPropertyName(String columnName, EntityType entityType) {
        return normalizeJavaName(columnName);
    }

    @Override
    public String getPropertyNameForForeignKey(String foreignKeyName, EntityType entityType) {
        return normalizeJavaName(foreignKeyName);
    }

    @Override
    public String getPropertyNameForInverseForeignKey(String foreignKeyName, EntityType entityType) {
        return normalizeJavaName("_" + foreignKeyName);
    }

    @Override
    public String getPropertyNameForPrimaryKey(String primaryKeyName, EntityType model) {
        return normalizeJavaName(primaryKeyName);
    }

}
