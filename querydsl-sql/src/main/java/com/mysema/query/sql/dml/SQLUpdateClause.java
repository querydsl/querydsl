/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql.dml;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryException;
import com.mysema.query.QueryMetadata;
import com.mysema.query.dml.UpdateClause;
import com.mysema.query.sql.SQLSerializer;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PSimple;
import com.mysema.query.types.path.Path;
import com.mysema.util.JDBCUtil;

import edu.umd.cs.findbugs.annotations.SuppressWarnings;

/**
 * SQLUpdateClause defines a UPDATE clause
 * 
 * @author tiwe
 * 
 */
@SuppressWarnings("SQL_PREPARED_STATEMENT_GENERATED_FROM_NONCONSTANT_STRING")
public class SQLUpdateClause implements UpdateClause<SQLUpdateClause> {

    private static final Logger logger = LoggerFactory
            .getLogger(SQLInsertClause.class);

    private final Connection connection;

    private final QueryMetadata metadata = new DefaultQueryMetadata();

    private final SQLTemplates templates;

    public SQLUpdateClause(Connection connection, SQLTemplates templates,
            PEntity<?> entity) {
        this.connection = connection;
        this.templates = templates;
        metadata.addFrom(entity);
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
        SQLSerializer serializer = new SQLSerializer(templates);
        serializer.serializeForUpdate(metadata);
        String queryString = serializer.toString();
        logger.debug(queryString);

        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(queryString);
            JDBCUtil.setParameters(stmt, serializer.getConstants());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new QueryException("Caught " + e.getClass().getSimpleName()
                    + " for " + queryString, e);
        } finally {
            if (stmt != null) {
                close(stmt);
            }
        }
    }

    @Override
    public <T> SQLUpdateClause set(Path<T> path, T value) {
        PSimple<T> columnPath = new PSimple<T>(path.getType(), path
                .getMetadata().getExpression().toString());
        metadata.addProjection(columnPath.eq(value));
        return this;
    }

    @Override
    public SQLUpdateClause where(EBoolean... o) {
        metadata.addWhere(o);
        return this;
    }

}
