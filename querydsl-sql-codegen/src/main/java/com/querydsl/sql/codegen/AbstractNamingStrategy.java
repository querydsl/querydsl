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

import com.querydsl.codegen.EntityType;
import com.querydsl.core.util.JavaSyntaxUtils;

/**
 * AbstractNamingStrategy is an abstract base class for NamingStrategy implementations
 *
 * @author tiwe
 *
 */
public abstract class AbstractNamingStrategy implements NamingStrategy {

    protected String foreignKeysClassName = "ForeignKeys";

    protected String foreignKeysVariable = "fk";

    protected String primaryKeysClassName = "PrimaryKeys";

    protected String primaryKeysVariable = "pk";

    protected String reservedSuffix = "_col";

    @Override
    public String appendSchema(String packageName, String schemaName) {
        String suffix = schemaName.toLowerCase();
        if (JavaSyntaxUtils.isReserved(suffix)) {
            suffix += "_";
        }
        return packageName + "." + suffix;
    }

    protected String escape(EntityType entityType, String name) {
        int suffix = 0;
        while (true) {
            String candidate = suffix > 0 ? name + suffix : name;
            if (entityType.getEscapedPropertyNames().contains(candidate)) {
                suffix++;
            } else {
                return candidate;
            }
        }
    }

    @Override
    public String getForeignKeysClassName() {
        return foreignKeysClassName;
    }

    @Override
    public String getForeignKeysVariable(EntityType entityType) {
        return foreignKeysVariable;
    }

    @Override
    public String getPrimaryKeysClassName() {
        return primaryKeysClassName;
    }

    @Override
    public String getPrimaryKeysVariable(EntityType entityType) {
        return primaryKeysVariable;
    }

    @Override
    public String normalizeColumnName(String columnName) {
        if (columnName != null) {
            return columnName.replaceAll("\r", "").replaceAll("\n", " ");
        } else {
            return null;
        }
    }

    @Override
    public String normalizeTableName(String tableName) {
        if (tableName != null) {
            return tableName.replaceAll("\r", "").replaceAll("\n", " ");
        } else {
            return null;
        }
    }

    @Override
    public String normalizeSchemaName(String schemaName) {
        if (schemaName != null) {
            return schemaName.replaceAll("\r", "").replaceAll("\n", " ");
        } else {
            return null;
        }
    }

    public void setForeignKeysClassName(String foreignKeysClassName) {
        this.foreignKeysClassName = foreignKeysClassName;
    }

    public void setForeignKeysVariable(String foreignKeysVariable) {
        this.foreignKeysVariable = foreignKeysVariable;
    }

    public void setPrimaryKeysClassName(String primaryKeysClassName) {
        this.primaryKeysClassName = primaryKeysClassName;
    }

    public void setPrimaryKeysVariable(String primaryKeysVariable) {
        this.primaryKeysVariable = primaryKeysVariable;
    }

    public void setReservedSuffix(String reservedSuffix) {
        this.reservedSuffix = reservedSuffix;
    }

}
