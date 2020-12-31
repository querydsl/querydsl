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

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.querydsl.codegen.utils.model.Type;

/**
 * {@code ForeignKeyData} defines foreign key data
 *
 * @author tiwe
 *
 */
public class ForeignKeyData implements KeyData {

    private final String name;

    @Nullable
    private final String schema;

    private final String table;

    @Nullable
    private final Type type;

    private final List<String> foreignColumns = new ArrayList<String>();

    private final List<String> parentColumns = new ArrayList<String>();

    public ForeignKeyData(String name, @Nullable String schema, String parentTable, @Nullable Type type) {
        this.name = name;
        this.schema = schema;
        this.table = parentTable;
        this.type = type;
    }

    public void add(String foreignColumn, String parentColumn) {
        foreignColumns.add(foreignColumn);
        parentColumns.add(parentColumn);
    }

    @Override
    public String getName() {
        return name;
    }

    @Nullable
    public String getSchema() {
        return schema;
    }

    @Override
    public String getTable() {
        return table;
    }

    @Override
    public List<String> getForeignColumns() {
        return foreignColumns;
    }

    @Override
    public List<String> getParentColumns() {
        return parentColumns;
    }

    @Nullable
    @Override
    public Type getType() {
        return type;
    }

}
