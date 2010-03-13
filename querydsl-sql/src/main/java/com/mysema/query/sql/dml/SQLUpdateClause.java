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
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.commons.lang.Pair;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.QueryException;
import com.mysema.query.dml.UpdateClause;
import com.mysema.query.sql.SQLSerializer;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.PEntity;
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

    private final PEntity<?> entity;

    private final SQLTemplates templates;
    
    private final List<Pair<Path<?>,?>> updates = new ArrayList<Pair<Path<?>,?>>();
    
    private final BooleanBuilder where = new BooleanBuilder();

    public SQLUpdateClause(Connection connection, SQLTemplates templates, PEntity<?> entity) {
        this.connection = connection;
        this.templates = templates;
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
        SQLSerializer serializer = new SQLSerializer(templates, true);
        serializer.serializeForUpdate(entity, updates, where.getValue());
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
        updates.add(Pair.<Path<?>,Object>of(path, value));
        return this;
    }

    @Override
    public SQLUpdateClause where(EBoolean... o) {
        for (EBoolean e : o){
            where.and(e);
        }
        return this;
    }

}
