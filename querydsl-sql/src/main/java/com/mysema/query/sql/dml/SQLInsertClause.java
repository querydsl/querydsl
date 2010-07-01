/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.dml;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.QueryException;
import com.mysema.query.dml.InsertClause;
import com.mysema.query.sql.SQLSerializer;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Param;
import com.mysema.query.types.Path;
import com.mysema.query.types.SubQuery;
import com.mysema.query.types.expr.ExprConst;
import com.mysema.query.types.path.NullExpr;
import com.mysema.query.types.path.PEntity;
import com.mysema.util.JDBCUtil;
import com.mysema.util.ResultSetAdapter;

import edu.umd.cs.findbugs.annotations.SuppressWarnings;

/**
 * SQLInsertClause defines an INSERT INTO clause
 *
 * @author tiwe
 *
 */
@SuppressWarnings("SQL_PREPARED_STATEMENT_GENERATED_FROM_NONCONSTANT_STRING")
public class SQLInsertClause implements InsertClause<SQLInsertClause> {

    private static final Logger logger = LoggerFactory.getLogger(SQLInsertClause.class);

    private final List<Path<?>> columns = new ArrayList<Path<?>>();

    private final Connection connection;

    private final PEntity<?> entity;

    @Nullable
    private SubQuery<?> subQuery;

    private final SQLTemplates templates;

    private final List<Expr<?>> values = new ArrayList<Expr<?>>();

    public SQLInsertClause(Connection connection, SQLTemplates templates, PEntity<?> entity) {
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
    
    protected void close(ResultSet rs){
        try {
            rs.close();
        } catch (SQLException e) {
            throw new QueryException(e);
        }
    }

    @Override
    public SQLInsertClause columns(Path<?>... columns) {
        this.columns.addAll(Arrays.asList(columns));
        return this;
    }

    @Nullable
    @java.lang.SuppressWarnings("unchecked")
    public <T> T executeWithKey(Path<T> path){
        ResultSet rs = executeWithKeys();
        try{
            if (rs.next()){
                return (T) rs.getObject(1);
            }else{
                return null;
            }
        } catch (SQLException e) {
            throw new QueryException(e.getMessage(), e);
        }finally{
            close(rs);
        }
    }

    @java.lang.SuppressWarnings("unchecked")
    public <T> List<T> executeWithKeys(Path<T> path){
        ResultSet rs = executeWithKeys();
        try{
            List<T> rv = new ArrayList<T>();
            while (rs.next()){
                rv.add((T) rs.getObject(1));
            }
            return rv;
        } catch (SQLException e) {
            throw new QueryException(e.getMessage(), e);
        }finally{
            close(rs);
        }
    }
    
    public ResultSet executeWithKeys(){
        SQLSerializer serializer = new SQLSerializer(templates, true);
        serializer.serializeForInsert(entity, columns, values, subQuery);
        String queryString = serializer.toString();
        logger.debug(queryString);

        try {
            final PreparedStatement stmt = connection.prepareStatement(queryString);
            JDBCUtil.setParameters(stmt, serializer.getConstants(),Collections.<Param<?>,Object>emptyMap());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();

            return new ResultSetAdapter(rs){
                @Override
                public void close() throws SQLException {
                    try {
                        super.close();
                    } finally {
                        stmt.close();
                    }
                }
            };
        } catch (SQLException e) {
            throw new QueryException("Caught " + e.getClass().getSimpleName() + " for " + queryString, e);
        }
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
            JDBCUtil.setParameters(stmt, serializer.getConstants(),Collections.<Param<?>,Object>emptyMap());
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
    public SQLInsertClause select(SubQuery<?> subQuery) {
        this.subQuery = subQuery;
        return this;
    }

    @Override
    public <T> SQLInsertClause set(Path<T> path, T value) {
        columns.add(path);
        if (value != null){
            values.add(ExprConst.create(value));
        }else{
            values.add(new NullExpr<T>(path.getType()));
        }
        return this;
    }

    @Override
    public SQLInsertClause values(Object... v) {
        for (Object value : v) {
            if (value instanceof Expr<?>) {
                values.add((Expr<?>) value);
            } else if (value != null){
                values.add(ExprConst.create(value));
            }else{
                values.add(NullExpr.DEFAULT);
            }
        }
        return this;
    }

    @Override
    public String toString(){
        SQLSerializer serializer = new SQLSerializer(templates, true);
        serializer.serializeForInsert(entity, columns, values, subQuery);
        return serializer.toString();
    }

}
