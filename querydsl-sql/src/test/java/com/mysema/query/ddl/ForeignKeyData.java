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
package com.mysema.query.ddl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tiwe
 *
 */
public class ForeignKeyData implements KeyData {

    private final String name;

    private final String table;
    
    private final List<String> foreignColumns = new ArrayList<String>();
    
    private final List<String> parentColumns = new ArrayList<String>();

    public ForeignKeyData(String name, String parentTable) {
        this.name = name;
        this.table = parentTable;
    }

    public void add(String foreignColumn, String parentColumn) {
        foreignColumns.add(foreignColumn);
        parentColumns.add(parentColumn);
    }

    public String getName() {
        return name;
    }

    public String getTable() {
        return table;
    }

    public List<String> getForeignColumns() {
        return foreignColumns;
    }

    public List<String> getParentColumns() {
        return parentColumns;
    }
    
}
