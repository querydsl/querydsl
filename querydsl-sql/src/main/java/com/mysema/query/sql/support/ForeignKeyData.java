/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql.support;

import java.util.ArrayList;
import java.util.List;

import com.mysema.commons.lang.Pair;

/**
 * @author tiwe
 *
 */
public class ForeignKeyData {
    
    private final String name;
    
    private final String table;
    
    private final List<Pair<String,String>> columns = new ArrayList<Pair<String,String>>();
    
    public ForeignKeyData(String name, String parentTable) {
        this.name = name;
        this.table = parentTable;
    }
    
    public void add(String foreignColumn, String parentColumn){
        columns.add(Pair.of(foreignColumn, parentColumn));
    }
    
    public String getName() {
        return name;
    }

    public String getTable() {
        return table;
    }

    public List<Pair<String, String>> getColumns() {
        return columns;
    }
    
}
