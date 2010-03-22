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
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.QueryException;
import com.mysema.query.dml.InsertClause;
import com.mysema.query.sql.SQLSerializer;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.types.Path;
import com.mysema.query.types.SubQuery;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.expr.ExprConst;
import com.mysema.query.types.path.PEntity;
import com.mysema.util.JDBCUtil;

import edu.umd.cs.findbugs.annotations.SuppressWarnings;

/**
 * SQLInsertClause defines an INSERT INTO clause
 * 
 * @author tiwe
 * 
 */
@SuppressWarnings("SQL_PREPARED_STATEMENT_GENERATED_FROM_NONCONSTANT_STRING")
public class SQLInsertClause implements InsertClause<SQLInsertClause> {

    private static final Logger logger = LoggerFactory
            .getLogger(SQLInsertClause.class);

    private final List<Path<?>> columns = new ArrayList<Path<?>>();

    private final Connection connection;

    private final PEntity<?> entity;

    private SubQuery<?> subQuery;

    private final SQLTemplates templates;

    private final List<Expr<?>> values = new ArrayList<Expr<?>>();

    public SQLInsertClause(Connection connection, SQLTemplates templates,
            PEntity<?> entity) {
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
    public SQLInsertClause columns(Path<?>... columns) {
        this.columns.addAll(Arrays.asList(columns));
        return this;
    }

    @Override
    public long execute() {
        SQLSerializer serializer = new SQLSerializer(templates, true);
        serializer.serializeForInsert(entity, columns, values, subQuery);
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
    public SQLInsertClause select(SubQuery<?> subQuery) {
        this.subQuery = subQuery;
        return this;
    }

    @Override
    @java.lang.SuppressWarnings("unchecked")
    public SQLInsertClause values(Object... v) {
        for (Object value : v) {
            if (value instanceof Expr) {
                values.add((Expr) value);
            } else {
                values.add(ExprConst.create(value));
            }
        }
        return this;
    }

}
