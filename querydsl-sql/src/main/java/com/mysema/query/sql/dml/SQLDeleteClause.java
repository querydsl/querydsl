/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.dml;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;

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

import edu.umd.cs.findbugs.annotations.SuppressWarnings;

/**
 * SQLDeleteClause defines a DELETE clause
 *
 * @author tiwe
 *
 */
@SuppressWarnings("SQL_PREPARED_STATEMENT_GENERATED_FROM_NONCONSTANT_STRING")
public class SQLDeleteClause extends AbstractSQLClause implements DeleteClause<SQLDeleteClause> {

    private static final Logger logger = LoggerFactory.getLogger(SQLDeleteClause.class);

    private final Connection connection;

    private final RelationalPath<?> entity;

    private final QueryMetadata metadata = new DefaultQueryMetadata();
    
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

    protected void close(PreparedStatement stmt) {
        try {
            stmt.close();
        } catch (SQLException e) {
            throw new QueryException(e);
        }
    }

    @Override
    public long execute() {
        SQLSerializer serializer = new SQLSerializer(configuration.getTemplates(), true);
        serializer.serializeForDelete(metadata, entity);
        String queryString = serializer.toString();
        logger.debug(queryString);

        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(queryString);
            setParameters(stmt, serializer.getConstants(), Collections.<Param<?>,Object>emptyMap());
            return stmt.executeUpdate();
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
