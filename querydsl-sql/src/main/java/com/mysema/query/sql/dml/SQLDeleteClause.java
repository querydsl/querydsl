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

import com.mysema.query.BooleanBuilder;
import com.mysema.query.QueryException;
import com.mysema.query.dml.DeleteClause;
import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.SQLSerializer;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.types.Param;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.PEntity;

import edu.umd.cs.findbugs.annotations.SuppressWarnings;

/**
 * SQLDeleteClause defines a DELETE clause
 *
 * @author tiwe
 *
 */
@SuppressWarnings("SQL_PREPARED_STATEMENT_GENERATED_FROM_NONCONSTANT_STRING")
public class SQLDeleteClause implements DeleteClause<SQLDeleteClause> {

    private static final Logger logger = LoggerFactory.getLogger(SQLDeleteClause.class);

    private final Connection connection;

    private final PEntity<?> entity;

    private final BooleanBuilder where = new BooleanBuilder();

    private final Configuration configuration;

    public SQLDeleteClause(Connection connection, SQLTemplates templates, PEntity<?> entity) {
        this(connection, new Configuration(templates), entity);
    }
    
    public SQLDeleteClause(Connection connection, Configuration configuration, PEntity<?> entity) {
        this.connection = connection;
        this.configuration = configuration;
        this.entity = entity;
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
        serializer.serializeForDelete(entity, where.getValue());
        String queryString = serializer.toString();
        logger.debug(queryString);

        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(queryString);
            configuration.setParameters(stmt, serializer.getConstants(), Collections.<Param<?>,Object>emptyMap());
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
        for (EBoolean e : o){
            where.and(e);
        }
        return this;
    }
    
    @Override
    public String toString(){
        SQLSerializer serializer = new SQLSerializer(configuration.getTemplates(), true);
        serializer.serializeForDelete(entity, where.getValue());
        return serializer.toString();
    }

}
