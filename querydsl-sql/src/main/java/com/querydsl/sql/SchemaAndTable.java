/*
 * Copyright 2014, Mysema Ltd
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

import java.io.Serializable;

import com.google.common.base.Objects;

/**
 *
 */
public class SchemaAndTable implements Serializable {

    private final String schema, table;

    public SchemaAndTable(String schema, String table) {
        this.schema = schema;
        this.table = table;
    }

    public String getSchema() {
        return schema;
    }

    public String getTable() {
        return table;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof SchemaAndTable) {
            SchemaAndTable st = (SchemaAndTable)o;
            return Objects.equal(st.schema, schema) && Objects.equal(st.table, table);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return (schema != null ? 31 * schema.hashCode() : 0) + table.hashCode();
    }

}
