/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.querydsl.sql.ddl;

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
