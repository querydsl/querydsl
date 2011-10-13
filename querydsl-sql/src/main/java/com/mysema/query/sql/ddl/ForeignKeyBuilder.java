/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.ddl;

import java.util.List;

import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.sql.support.ForeignKeyData;

/**
 * ForeignKeyBuilder is part of the fluent interface of CreateTableClause
 * 
 * @author tiwe
 *
 */
public class ForeignKeyBuilder {

    private final List<ForeignKeyData> foreignKeys;
    
    private final CreateTableClause clause;
    
    private final String name;
    
    private final String[] foreignColumns;

    private final SQLTemplates templates;
    
    public ForeignKeyBuilder(CreateTableClause clause, SQLTemplates templates, List<ForeignKeyData> foreignKeys, String name, String[] columns) {
        this.clause = clause;
        this.templates = templates;
        this.foreignKeys = foreignKeys;
        this.name = name;
        this.foreignColumns = columns;
    }

    public CreateTableClause references(String table, String... parentColumns) {
        ForeignKeyData foreignKey = new ForeignKeyData(name, templates.quoteIdentifier(table), null);
        for (int i = 0; i < parentColumns.length; i++) {
            foreignKey.add(
                templates.quoteIdentifier(foreignColumns[i]), 
                templates.quoteIdentifier(parentColumns[i]));
        }
        foreignKeys.add(foreignKey);
        return clause;
    }

}
