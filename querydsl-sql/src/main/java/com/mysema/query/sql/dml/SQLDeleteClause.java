/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql.dml;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;
import com.mysema.query.dml.DeleteClause;
import com.mysema.query.sql.JDBCUtil;
import com.mysema.query.sql.SQLSerializer;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.PEntity;

/**
 * @author tiwe
 *
 */
public class SQLDeleteClause extends AbstractDMLClause implements DeleteClause<SQLDeleteClause>{

    private final QueryMetadata md = new DefaultQueryMetadata();
    
    private final Connection connection;
    
    private final SQLTemplates templates;
    
    public SQLDeleteClause(Connection connection, SQLTemplates templates, PEntity<?> entity){
        this.connection = connection;
        this.templates = templates;
        md.addFrom(entity);        
    }
    
    @Override
    public long execute() {
        SQLSerializer serializer = new SQLSerializer(templates);
        serializer.serializeForDelete(md);
        String queryString = serializer.toString();
        
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(queryString);
            JDBCUtil.setParameters(stmt, serializer.getConstants());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Caught " + e.getClass().getSimpleName() + " for " + queryString, e);
        }finally{
            if (stmt != null){
                close(stmt);    
            }                        
        }
    }

    @Override
    public SQLDeleteClause where(EBoolean... o) {
        md.addWhere(o);
        return this;
    }

}
