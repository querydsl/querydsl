/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql.dml;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mysema.query.QueryException;
import com.mysema.query.sql.SQLSerializer;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PEntity;
import com.mysema.util.JDBCUtil;

@edu.umd.cs.findbugs.annotations.SuppressWarnings("SQL_PREPARED_STATEMENT_GENERATED_FROM_NONCONSTANT_STRING")
public class SQLInsertClause {
    
    private final PEntity<?> entity;
    
    private final Connection connection;
    
    private final SQLTemplates templates;
    
    private final List<Expr<?>> columns = new ArrayList<Expr<?>>();
    
    private final List<Expr<?>> values = new ArrayList<Expr<?>>();
    
    public SQLInsertClause(Connection connection, SQLTemplates templates, PEntity<?> entity){
        this.connection = connection;
        this.templates = templates;
        this.entity = entity;    
    }
    
    @edu.umd.cs.findbugs.annotations.SuppressWarnings("SQL_PREPARED_STATEMENT_GENERATED_FROM_NONCONSTANT_STRING")
    public long execute() {
        SQLSerializer serializer = new SQLSerializer(templates);
        serializer.serializeForInsert(entity, columns, values);
        String queryString = serializer.toString();
        
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(queryString);
            JDBCUtil.setParameters(stmt, serializer.getConstants());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new QueryException("Caught " + e.getClass().getSimpleName() + " for " + queryString, e);
        }finally{
            if (stmt != null){
                close(stmt);    
            }                        
        }
    }
    
    public SQLInsertClause columns(Expr<?>... columns){
        this.columns.addAll(Arrays.asList(columns));
        return this;        
    }
    
    public SQLInsertClause values(Expr<?>... values){
        this.values.addAll(Arrays.asList(values));
        return this;
    }
    
    protected void close(PreparedStatement stmt) {
        try {
            stmt.close();
        } catch (SQLException e) {
            throw new QueryException(e);
        }        
    }


}
