/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.support;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.mysema.codegen.model.Type;
import com.mysema.commons.lang.Assert;

/**
 * @author tiwe
 *
 */
public class ForeignKeyData implements KeyData {

    private final String name;

    private final String table;
    
    @Nullable
    private final Type type;
    
    private final List<String> foreignColumns = new ArrayList<String>();
    
    private final List<String> parentColumns = new ArrayList<String>();

    public ForeignKeyData(String name, String parentTable, @Nullable Type type) {
        this.name = Assert.hasLength(name,"name");
        this.table = Assert.hasLength(parentTable,"parentTable");
        this.type = type;
    }

    public void add(String foreignColumn, String parentColumn){
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

    @Nullable
    public Type getType() {
        return type;
    }
    
}
