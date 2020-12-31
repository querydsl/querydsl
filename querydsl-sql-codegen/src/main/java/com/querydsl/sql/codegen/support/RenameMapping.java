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
package com.querydsl.sql.codegen.support;

import com.querydsl.sql.Configuration;

import java.util.Arrays;

/**
 * {@code RenameMapping} overrides schemas, tables, columns and combinations of the three.
 *
 * @author tiwe
 */
public class RenameMapping implements Mapping {

    private String fromSchema, fromTable, fromColumn;

    private String toSchema, toTable, toColumn;

    @Override
    public void apply(Configuration configuration) {
        if (fromSchema != null) {
            if (fromTable != null && fromColumn != null && toColumn != null) {
                configuration.registerColumnOverride(fromSchema, fromTable, fromColumn, toColumn);
            } else if (fromTable != null && toTable != null) {
                if (toSchema != null) {
                    configuration.registerTableOverride(fromSchema, fromTable, toSchema, toTable);
                } else {
                    configuration.registerTableOverride(fromSchema, fromTable, toTable);
                }
            } else if (toSchema != null) {
                configuration.registerSchemaOverride(fromSchema, toSchema);
            } else {
                insufficientArgs();
            }
        } else if (fromTable != null) {
            if (fromColumn != null && toColumn != null) {
                configuration.registerColumnOverride(fromTable, fromColumn, toColumn);
            } else if (toTable != null) {
                configuration.registerTableOverride(fromTable, toTable);
            } else {
                insufficientArgs();
            }
        } else {
            insufficientArgs();
        }
    }

    private void insufficientArgs() {
        throw new IllegalArgumentException("Insufficient args " +
                Arrays.asList(fromSchema, fromTable, fromColumn) + " to " +
                Arrays.asList(toSchema, toTable, toColumn));
    }

    public String getFromSchema() {
        return fromSchema;
    }

    public void setFromSchema(String fromSchema) {
        this.fromSchema = fromSchema;
    }

    public String getFromTable() {
        return fromTable;
    }

    public void setFromTable(String fromTable) {
        this.fromTable = fromTable;
    }

    public String getFromColumn() {
        return fromColumn;
    }

    public void setFromColumn(String fromColumn) {
        this.fromColumn = fromColumn;
    }

    public String getToSchema() {
        return toSchema;
    }

    public void setToSchema(String toSchema) {
        this.toSchema = toSchema;
    }

    public String getToTable() {
        return toTable;
    }

    public void setToTable(String toTable) {
        this.toTable = toTable;
    }

    public String getToColumn() {
        return toColumn;
    }

    public void setToColumn(String toColumn) {
        this.toColumn = toColumn;
    }
}
