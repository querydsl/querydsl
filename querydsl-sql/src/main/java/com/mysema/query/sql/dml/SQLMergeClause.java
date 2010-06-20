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

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.QueryException;
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

/**
 * SQLMergeClause defines an MERGE INTO clause
 * 
 * @author tiwe
 *
 */
public class SQLMergeClause {
    
    private static final Logger logger = LoggerFactory.getLogger(SQLMergeClause.class);
    
    private final List<Path<?>> columns = new ArrayList<Path<?>>();
    
    private final Connection connection;

    private final PEntity<?> entity;

    private final List<Path<?>> keys = new ArrayList<Path<?>>();

    @Nullable
    private SubQuery<?> subQuery;

    private final SQLTemplates templates;

    private final List<Expr<?>> values = new ArrayList<Expr<?>>();

    public SQLMergeClause(Connection connection, SQLTemplates templates, PEntity<?> entity) {
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

    public long execute() {
        SQLSerializer serializer = new SQLSerializer(templates, true);
        serializer.serializeForMerge(entity, keys, columns, values, subQuery);
        String queryString = serializer.toString();
        logger.debug(queryString);

        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(queryString);
            JDBCUtil.setParameters(stmt, serializer.getConstants(),Collections.<Param<?>,Object>emptyMap());
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

    public SQLMergeClause keys(Path<?>... paths){
        for (Path<?> path : paths){
            keys.add(path);
        }
        return this;
    }

    public SQLMergeClause select(SubQuery<?> subQuery) {
        this.subQuery = subQuery;
        return this;
    }
    
    public <T> SQLMergeClause set(Path<T> path, T value) {
        columns.add(path);
        if (value != null){
            values.add(ExprConst.create(value));    
        }else{
            values.add(new NullExpr<T>(path.getType()));
        }        
        return this;
    }

    @Override
    public String toString(){
        SQLSerializer serializer = new SQLSerializer(templates, true);
        serializer.serializeForMerge(entity, keys, columns, values, subQuery);
        return serializer.toString();
    }
    
    public SQLMergeClause values(Object... v) {
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

    

}
