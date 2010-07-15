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

import com.mysema.commons.lang.Pair;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.QueryException;
import com.mysema.query.dml.UpdateClause;
import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.SQLSerializer;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.types.Param;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.NullExpr;
import com.mysema.query.types.path.PEntity;

import edu.umd.cs.findbugs.annotations.SuppressWarnings;

/**
 * SQLUpdateClause defines a UPDATE clause
 *
 * @author tiwe
 *
 */
@SuppressWarnings("SQL_PREPARED_STATEMENT_GENERATED_FROM_NONCONSTANT_STRING")
public class SQLUpdateClause extends AbstractSQLClause  implements UpdateClause<SQLUpdateClause> {

    private static final Logger logger = LoggerFactory.getLogger(SQLInsertClause.class);

    private final Connection connection;

    private final PEntity<?> entity;

    private final List<Pair<Path<?>,?>> updates = new ArrayList<Pair<Path<?>,?>>();

    private final BooleanBuilder where = new BooleanBuilder();

    public SQLUpdateClause(Connection connection, SQLTemplates templates, PEntity<?> entity) {
        this(connection, new Configuration(templates), entity);
    }
    
    public SQLUpdateClause(Connection connection, Configuration configuration, PEntity<?> entity) {
        super(configuration);
        this.connection = connection;
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
        serializer.serializeForUpdate(entity, updates, where.getValue());
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
    public <T> SQLUpdateClause set(Path<T> path, T value) {
        if (value != null){
            updates.add(Pair.<Path<?>,Object>of(path, value));
        }else{
            updates.add(Pair.<Path<?>,Object>of(path, new NullExpr<T>(path.getType())));
        }
        return this;
    }

    @java.lang.SuppressWarnings("unchecked")
    @Override
    public SQLUpdateClause set(List<? extends Path<?>> paths, List<?> values) {
        for (int i = 0; i < paths.size(); i++){
            if (values.get(i) != null){
                updates.add(Pair.<Path<?>,Object>of(paths.get(i), values.get(i)));
            }else{
                updates.add(Pair.<Path<?>,Object>of(paths.get(i), new NullExpr(paths.get(i).getType())));
            }
        }
        return this;
    }

    @Override
    public SQLUpdateClause where(EBoolean... o) {
        for (EBoolean e : o){
            where.and(e);
        }
        return this;
    }

    @Override
    public String toString(){
        SQLSerializer serializer = new SQLSerializer(configuration.getTemplates(), true);
        serializer.serializeForUpdate(entity, updates, where.getValue());
        return serializer.toString();
    }
}
