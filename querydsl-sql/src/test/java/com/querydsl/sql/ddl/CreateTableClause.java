/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.querydsl.sql.ddl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;
import com.querydsl.core.QueryException;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLTemplates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CreateTableClause defines a CREATE TABLE clause
 *
 * @author tiwe
 *
 */
public class CreateTableClause {

    private static final Logger logger = LoggerFactory.getLogger(CreateTableClause.class);

    private static final Joiner COMMA_JOINER = Joiner.on(", ");

    private final Connection connection;

    private final Configuration configuration;

    private final SQLTemplates templates;

    private final String table;

    private final List<ColumnData> columns = new ArrayList<ColumnData>();

    private final List<IndexData> indexes = new ArrayList<IndexData>();

    private PrimaryKeyData primaryKey;

    private final List<ForeignKeyData> foreignKeys = new ArrayList<ForeignKeyData>();

    public CreateTableClause(Connection conn, Configuration c, String table) {
        this.connection = conn;
        this.configuration = c;
        this.templates = c.getTemplates();
        this.table = templates.quoteIdentifier(table);
    }

    /**
     * Add a new column definition
     *
     * @param name
     * @param type
     * @return
     */
    public CreateTableClause column(String name, Class<?> type) {
        String typeName = configuration.getTypeName(type);
        columns.add(new ColumnData(templates.quoteIdentifier(name), typeName));
        return this;
    }

    private ColumnData lastColumn() {
        return columns.get(columns.size()-1);
    }

    /**
     * Set the last added column to not null
     *
     * @return
     */
    public CreateTableClause notNull() {
        lastColumn().setNullAllowed(false);
        return this;
    }

    /**
     * Set the size of the last column's type
     *
     * @param size
     * @return
     */
    public CreateTableClause size(int size) {
        lastColumn().setSize(size);
        return this;
    }

    /**
     * Set the last column to auto increment
     *
     * @return
     */
    public CreateTableClause autoIncrement() {
        lastColumn().setAutoIncrement(true);
        return this;
    }

    /**
     * Set the primary key
     *
     * @param name
     * @param columns
     * @return
     */
    public CreateTableClause primaryKey(String name, String... columns) {
        for (int i = 0; i < columns.length; i++) {
            columns[i] = templates.quoteIdentifier(columns[i]);
        }
        primaryKey = new PrimaryKeyData(templates.quoteIdentifier(name), columns);
        return this;
    }

    /**
     * Add an index
     *
     * @param name
     * @param columns
     * @return
     */
    public CreateTableClause index(String name, String... columns) {
        indexes.add(new IndexData(name, columns));
        return this;
    }

    /**
     * Set the last added index to unique
     *
     * @return
     */
    public CreateTableClause unique() {
        indexes.get(indexes.size()-1).setUnique(true);
        return this;
    }

    /**
     * Add a foreign key
     *
     * @param name
     * @param columns
     * @return
     */
    public ForeignKeyBuilder foreignKey(String name, String... columns) {
        return new ForeignKeyBuilder(this, templates, foreignKeys, templates.quoteIdentifier(name), columns);
    }

    /**
     * Execute the clause
     */
    @SuppressWarnings("SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE")
    public void execute() {
        StringBuilder builder = new StringBuilder();
        builder.append(templates.getCreateTable() + table + " (\n");
        List<String> lines = new ArrayList<String>(columns.size() + foreignKeys.size() + 1);
        // columns
        for (ColumnData column : columns) {
            StringBuilder line = new StringBuilder();
            line.append(column.getName() + " " + column.getType().toUpperCase());
            if (column.getSize() != null) {
                line.append("(" + column.getSize() + ")");
            }
            if (!column.isNullAllowed()) {
                line.append(templates.getNotNull().toUpperCase());
            }
            if (column.isAutoIncrement()) {
                line.append(templates.getAutoIncrement().toUpperCase());
            }
            lines.add(line.toString());
        }

        // primary key
        if (primaryKey != null) {
            StringBuilder line = new StringBuilder();
            line.append("CONSTRAINT " + primaryKey.getName()+ " ");
            line.append("PRIMARY KEY(" + COMMA_JOINER.join(primaryKey.getColumns()) +")");
            lines.add(line.toString());
        }

        // foreign keys
        for (ForeignKeyData foreignKey : foreignKeys) {
            StringBuilder line = new StringBuilder();
            line.append("CONSTRAINT " + foreignKey.getName()+ " ");
            line.append("FOREIGN KEY(" + COMMA_JOINER.join(foreignKey.getForeignColumns())+ ") ");
            line.append("REFERENCES " + foreignKey.getTable() + "(" + COMMA_JOINER.join(foreignKey.getParentColumns())+ ")");
            lines.add(line.toString());
        }
        builder.append("  " + Joiner.on(",\n  ").join(lines));
        builder.append("\n)\n");
        logger.info(builder.toString());

        Statement stmt = null;
        try{
            stmt = connection.createStatement();
            stmt.execute(builder.toString());

            // indexes
            for (IndexData index : indexes) {
                String indexColumns = COMMA_JOINER.join(index.getColumns());
                String prefix = templates.getCreateIndex();
                if (index.isUnique()) {
                    prefix = templates.getCreateUniqueIndex();
                }
                String sql = prefix + index.getName() + templates.getOn() + table + "(" + indexColumns+ ")";
                logger.info(sql);
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            System.err.println(builder.toString());
            throw new QueryException(e.getMessage(), e);
        }finally{
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    throw new QueryException(e);
                }
            }
        }
    }

}
