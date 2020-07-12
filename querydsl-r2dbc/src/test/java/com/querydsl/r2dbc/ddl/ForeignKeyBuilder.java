/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.querydsl.r2dbc.ddl;

import com.querydsl.r2dbc.SQLTemplates;

import java.util.List;

/**
 * ForeignKeyBuilder is part of the fluent interface of CreateTableClause
 *
 * @author mc_fish
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
        this.foreignColumns = columns.clone();
    }

    public CreateTableClause references(String table, String... parentColumns) {
        ForeignKeyData foreignKey = new ForeignKeyData(name, templates.quoteIdentifier(table));
        for (int i = 0; i < parentColumns.length; i++) {
            foreignKey.add(
                    templates.quoteIdentifier(foreignColumns[i]),
                    templates.quoteIdentifier(parentColumns[i]));
        }
        foreignKeys.add(foreignKey);
        return clause;
    }

}
