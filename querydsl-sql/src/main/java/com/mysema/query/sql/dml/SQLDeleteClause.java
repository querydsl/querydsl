/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.dml;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryException;
import com.mysema.query.QueryFlag;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryFlag.Position;
import com.mysema.query.dml.DeleteClause;
import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.SQLSerializer;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.types.Param;
import com.mysema.query.types.expr.EBoolean;

/**
 * SQLDeleteClause defines a DELETE clause
 *
 * @author tiwe
 *
 */
public class SQLDeleteClause extends AbstractSQLClause implements DeleteClause<SQLDeleteClause> {

    private static final Logger logger = LoggerFactory.getLogger(SQLDeleteClause.class);

    private final Connection connection;

    private final RelationalPath<?> entity;

    private final List<QueryMetadata> batches = new ArrayList<QueryMetadata>();
    
    private QueryMetadata metadata = new DefaultQueryMetadata();
    
    private transient String queryString;
    
    public SQLDeleteClause(Connection connection, SQLTemplates templates, RelationalPath<?> entity) {
        this(connection, new Configuration(templates), entity);
    }
    
    public SQLDeleteClause(Connection connection, Configuration configuration, RelationalPath<?> entity) {
        super(configuration);
        this.connection = connection;
        this.entity = entity;
    }
    
    public SQLDeleteClause addFlag(Position position, String flag){
        metadata.addFlag(new QueryFlag(position, flag));
        return this;
    }
    
    public SQLDeleteClause addBatch() {
        batches.add(metadata);
        metadata = new DefaultQueryMetadata();
        return this;
    }

    protected void close(PreparedStatement stmt) {
        try {
            stmt.close();
        } catch (SQLException e) {
            throw new QueryException(e);
        }
    }
    
    private PreparedStatement createStatement() throws SQLException{
        PreparedStatement stmt;
        if (batches.isEmpty()){
            SQLSerializer serializer = new SQLSerializer(configuration.getTemplates(), true);
            serializer.serializeForDelete(metadata, entity);
            queryString = serializer.toString();
            logger.debug(queryString);
            stmt = connection.prepareStatement(queryString);
            setParameters(stmt, serializer.getConstants(), Collections.<Param<?>,Object>emptyMap());
        }else{
            SQLSerializer serializer = new SQLSerializer(configuration.getTemplates(), true);
            serializer.serializeForDelete(batches.get(0), entity);
            queryString = serializer.toString();
            logger.debug(queryString);
            
            // add first batch
            stmt = connection.prepareStatement(queryString);
            setParameters(stmt, serializer.getConstants(), Collections.<Param<?>,Object>emptyMap());
            stmt.addBatch();
            
            // add other batches
            for (int i = 1; i < batches.size(); i++){
                serializer = new SQLSerializer(configuration.getTemplates(), true);
                serializer.serializeForDelete(batches.get(i), entity);
                setParameters(stmt, serializer.getConstants(), Collections.<Param<?>,Object>emptyMap());
                stmt.addBatch();
            }
        }
        return stmt;
    }

    @Override
    public long execute() {
        PreparedStatement stmt = null;
        try {
            stmt = createStatement();
            if (batches.isEmpty()){
                return stmt.executeUpdate();    
            }else{
                long rv = 0;
                for (int i : stmt.executeBatch()){
                    rv += i;
                }
                return rv;
            }  
        } catch (SQLException e) {
            throw new QueryException("Caught " + e.getClass().getSimpleName() + " for " + queryString, e);
        } finally {
            if (stmt != null) {
                close(stmt);
            }
        }
    }
    
    @Override
    public SQLDeleteClause where(EBoolean... o) {
        metadata.addWhere(o);
        return this;
    }
    
    @Override
    public String toString(){
        SQLSerializer serializer = new SQLSerializer(configuration.getTemplates(), true);
        serializer.serializeForDelete(metadata, entity);
        return serializer.toString();
    }

}
