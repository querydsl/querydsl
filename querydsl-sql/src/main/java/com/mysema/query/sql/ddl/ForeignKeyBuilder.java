/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.ddl;

import java.util.List;

import com.mysema.query.sql.support.ForeignKeyData;

/**
 * @author tiwe
 *
 */
public class ForeignKeyBuilder {

    private final List<ForeignKeyData> foreignKeys;
    
    private final CreateTableClause clause;
    
    private final String name;
    
    private final String[] foreignColumns;
    
    public ForeignKeyBuilder(CreateTableClause clause, List<ForeignKeyData> foreignKeys, String name, String[] columns) {
        this.clause = clause;
        this.foreignKeys = foreignKeys;
        this.name = name;
        this.foreignColumns = columns;
    }

    public CreateTableClause references(String table, String... parentColumns) {
        ForeignKeyData foreignKey = new ForeignKeyData(name, table);
        for (int i = 0; i < parentColumns.length; i++){
            foreignKey.add(foreignColumns[i], parentColumns[i]);
        }
        foreignKeys.add(foreignKey);
        return clause;
    }

}
