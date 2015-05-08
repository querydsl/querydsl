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
package com.querydsl.sql;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;

final class NameMapping {

    private final Map<SchemaAndTable, SchemaAndTable> schemaTables = Maps.newHashMap();

    private final Map<String, String> schemas = Maps.newHashMap();

    private final Map<String, String> tables = Maps.newHashMap();

    private final Map<SchemaAndTable, Map<String, String>> schemaTableColumns = Maps.newHashMap();

    private final Map<String, Map<String, String>> tableColumns = Maps.newHashMap();

    public SchemaAndTable getOverride(SchemaAndTable key) {
        if (!schemaTables.isEmpty() && key.getSchema() != null) {
            if (schemaTables.containsKey(key)) {
                return schemaTables.get(key);
            }
        }
        String schema = key.getSchema(), table = key.getTable();
        boolean changed = false;
        if (schemas.containsKey(key.getSchema())) {
            schema = schemas.get(key.getSchema());
            changed = true;
        }

        if (tables.containsKey(key.getTable())) {
            table = tables.get(key.getTable());
            changed = true;
        }
        return changed ? new SchemaAndTable(schema, table) : key;
    }

    public String getColumnOverride(SchemaAndTable key, String column) {
        Map<String, String> columnOverrides;
        String newColumn = null;
        columnOverrides = schemaTableColumns.get(key);
        if (columnOverrides != null && (newColumn = columnOverrides.get(column)) != null) {
            return newColumn;
        }
        columnOverrides = tableColumns.get(key.getTable());
        if (columnOverrides != null && (newColumn = columnOverrides.get(column)) != null) {
            return newColumn;
        }
        return column;
    }

    public String registerSchemaOverride(String oldSchema, String newSchema) {
        return schemas.put(oldSchema, newSchema);
    }

    public String registerTableOverride(String oldTable, String newTable) {
        return tables.put(oldTable, newTable);
    }

    public SchemaAndTable registerTableOverride(SchemaAndTable from, SchemaAndTable to) {
        return schemaTables.put(from, to);
    }

    public String registerColumnOverride(String schema, String table, String oldColumn, String newColumn) {
        SchemaAndTable key = new SchemaAndTable(schema, table);
        Map<String, String> columnOverrides = schemaTableColumns.get(key);
        if (columnOverrides == null) {
            columnOverrides = new HashMap<String, String>();
            schemaTableColumns.put(key, columnOverrides);
        }
        return columnOverrides.put(oldColumn, newColumn);
    }

    public String registerColumnOverride(String table, String oldColumn, String newColumn) {
        Map<String, String> columnOverrides = tableColumns.get(table);
        if (columnOverrides == null) {
            columnOverrides = new HashMap<String, String>();
            tableColumns.put(table, columnOverrides);
        }
        return columnOverrides.put(oldColumn, newColumn);
    }

}
