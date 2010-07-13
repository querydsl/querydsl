/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.ddl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.commons.lang.Assert;
import com.mysema.query.QueryException;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.sql.support.ColumnData;
import com.mysema.query.sql.support.ForeignKeyData;
import com.mysema.query.sql.support.PrimaryKeyData;

/**
 * @author tiwe
 *
 */
public class CreateTableClause {
    
    private static final Logger logger = LoggerFactory.getLogger(CreateTableClause.class);

    private final Connection connection;
    
    private final SQLTemplates templates;
    
    private final String table;
    
    private final List<ColumnData> columns = new ArrayList<ColumnData>();
    
    private PrimaryKeyData primaryKey;
    
    private final List<ForeignKeyData> foreignKeys = new ArrayList<ForeignKeyData>();
    
    public CreateTableClause(Connection conn, SQLTemplates templates, String table) {
        this.connection = conn;
        this.templates = templates;
        this.table = table;
    }

    public CreateTableClause column(String name, Class<?> type) {
        Assert.notNull(name,"name");
        Assert.notNull(type,"type");
        columns.add(new ColumnData(name, templates.getTypeForClass(type))); 
        return this;
    }

    private ColumnData lastColumn(){
        return columns.get(columns.size()-1);
    }
    
    public CreateTableClause notNull() {
        lastColumn().setNullAllowed(false);
        return this;
    }

    public CreateTableClause size(int size) {
        lastColumn().setSize(size);
        return this;
    }

    public CreateTableClause primaryKey(String name, String... columns) {
        Assert.notNull(name,"name");
        Assert.notEmpty(columns,"columns");
        primaryKey = new PrimaryKeyData(name, columns);
        return this;
    }

    public ForeignKeyBuilder foreignKey(String name, String... columns) {
        Assert.notNull(name,"name");
        Assert.notEmpty(columns,"columns");
        return new ForeignKeyBuilder(this, foreignKeys, name, columns);
    }

    public void execute() {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE " + table + " (\n");
        List<String> lines = new ArrayList<String>(columns.size() + foreignKeys.size() + 1);
        // columns 
        for (ColumnData column : columns){
            StringBuilder line = new StringBuilder();
            line.append(column.getName() + " " + column.getType().toUpperCase());
            if (column.getSize() != null){
                line.append("(" + column.getSize() + ")");
            }
            line.append(column.isNullAllowed() ? " NULL" : " NOT NULL");            
            lines.add(line.toString());
        }
        // primary key
        if (primaryKey != null){
            StringBuilder line = new StringBuilder();
            line.append("CONSTRAINT " + primaryKey.getName()+ " ");
            line.append("PRIMARY KEY(" + StringUtils.join(primaryKey.getColumns(), ", ") +")");
            lines.add(line.toString());
        }
        // foreign keys
        for (ForeignKeyData foreignKey : foreignKeys){
            StringBuilder line = new StringBuilder();
            line.append("CONSTRAINT " + foreignKey.getName()+ " ");
            line.append("FOREIGN KEY(" + StringUtils.join(foreignKey.getForeignColumns(), ", ")+ ") ");
            line.append("REFERENCES " + foreignKey.getTable() + "(" + StringUtils.join(foreignKey.getParentColumns(),", ")+ ")");
            lines.add(line.toString());
        }
        builder.append("  " + StringUtils.join(lines, ",\n  "));
        builder.append("\n)\n");
        logger.info(builder.toString());
        
        Statement stmt = null;
        try{
            stmt = connection.createStatement();
            stmt.execute(builder.toString());
        } catch (SQLException e) {
            throw new QueryException(e.getMessage(), e);
        }finally{
            if (stmt != null){
                close(stmt);    
            }            
        }        
    }
    
    protected void close(Statement stmt) {
        try {
            stmt.close();
        } catch (SQLException e) {
            throw new QueryException(e);
        }
    }

}
