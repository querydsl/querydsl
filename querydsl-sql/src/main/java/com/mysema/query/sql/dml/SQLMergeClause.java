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
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.commons.lang.Assert;
import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryException;
import com.mysema.query.QueryFlag;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryFlag.Position;
import com.mysema.query.dml.StoreClause;
import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.SQLQueryImpl;
import com.mysema.query.sql.SQLSerializer;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Expression;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.NullExpr;
import com.mysema.query.types.Path;
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.expr.Param;

/**
 * SQLMergeClause defines an MERGE INTO clause
 *
 * @author tiwe
 *
 */
public class SQLMergeClause extends AbstractSQLClause implements StoreClause<SQLMergeClause>{

    private static final Logger logger = LoggerFactory.getLogger(SQLMergeClause.class);

    private final List<Path<?>> columns = new ArrayList<Path<?>>();

    private final Connection connection;

    private final RelationalPath<?> entity;

    private final QueryMetadata metadata = new DefaultQueryMetadata();
    
    private final List<Path<?>> keys = new ArrayList<Path<?>>();

    @Nullable
    private SubQueryExpression<?> subQuery;
    
    private final List<SQLMergeBatch> batches = new ArrayList<SQLMergeBatch>();

    private final List<Expression<?>> values = new ArrayList<Expression<?>>();
    
    private transient String queryString;

    public SQLMergeClause(Connection connection, SQLTemplates templates, RelationalPath<?> entity) {
        this(connection, new Configuration(templates), entity);
    }
    
    public SQLMergeClause(Connection connection, Configuration configuration, RelationalPath<?> entity) {
        super(configuration);
        this.connection = Assert.notNull(connection,"connection");
        this.entity = Assert.notNull(entity,"entity");
    }
    
    public SQLMergeClause addFlag(Position position, String flag){
        metadata.addFlag(new QueryFlag(position, flag));
        return this;
    }
    

    public SQLMergeClause addBatch() {
        if (!configuration.getTemplates().isNativeMerge()){
            throw new IllegalStateException("batch only supported for databases that support native merge");
        }
        
        batches.add(new SQLMergeBatch(keys, columns, values, subQuery));
        columns.clear();
        values.clear();
        keys.clear();
        subQuery = null;
        return this;
    }
    
    public SQLMergeClause columns(Path<?>... columns) {
        this.columns.addAll(Arrays.asList(columns));
        return this;
    }

    protected void close(PreparedStatement stmt) {
        try {
            stmt.close();
        } catch (SQLException e) {
            throw new QueryException(e);
        }
    }

    public long execute() {                
        if (configuration.getTemplates().isNativeMerge()){
            return executeNativeMerge();
        }else{
            return executeCompositeMerge();
        }        
    }

    @SuppressWarnings("unchecked")
    private long executeCompositeMerge() {        
        // select 
        SQLQuery query = new SQLQueryImpl(connection, configuration.getTemplates()).from(entity);
        for (int i=0; i < columns.size(); i++){
            if (values.get(i) instanceof NullExpr){
                query.where(ExpressionUtils.isNull(columns.get(i)));
            }else{
                query.where(ExpressionUtils.eq(columns.get(i),(Expression)values.get(i)));    
            }            
        }
        List<?> ids = query.list(keys.get(0));
        
        if (!ids.isEmpty()){
            // update
            SQLUpdateClause update = new SQLUpdateClause(connection, configuration.getTemplates(), entity);
            populate(update);
            update.where(ExpressionUtils.in((Expression)keys.get(0),ids));
            return update.execute();
        }else{
            // insert
            SQLInsertClause insert = new SQLInsertClause(connection, configuration.getTemplates(), entity);
            populate(insert);
            return insert.execute();
            
        }
    }

    @SuppressWarnings("unchecked")
    private void populate(StoreClause<?> clause) {
        for (int i = 0; i < columns.size(); i++){
            clause.set((Path)columns.get(i), (Expression)values.get(i));
        }
    }
    
    private PreparedStatement createStatement() throws SQLException{
        SQLSerializer serializer = new SQLSerializer(configuration.getTemplates(), true);
        PreparedStatement stmt = null;
        if (batches.isEmpty()){
            serializer.serializeForMerge(metadata, entity, keys, columns, values, subQuery);
            queryString = serializer.toString();
            logger.debug(queryString);
            stmt = connection.prepareStatement(queryString);
            setParameters(stmt, serializer.getConstants(), serializer.getConstantPaths(), Collections.<Param<?>,Object>emptyMap());
        }else{
            serializer.serializeForMerge(metadata, entity, 
                    batches.get(0).getKeys(), batches.get(0).getColumns(), 
                    batches.get(0).getValues(), batches.get(0).getSubQuery());
            queryString = serializer.toString();
            logger.debug(queryString);
            stmt = connection.prepareStatement(queryString);
            
            // add first batch
            stmt.addBatch();
            
            // add other batches
            for (int i = 1; i < batches.size(); i++){
                SQLMergeBatch batch = batches.get(i);
                serializer = new SQLSerializer(configuration.getTemplates(), true, true);
                serializer.serializeForMerge(metadata, entity, batch.getKeys(), batch.getColumns(), batch.getValues(), batch.getSubQuery());
                setParameters(stmt, serializer.getConstants(), serializer.getConstantPaths(), Collections.<Param<?>,Object>emptyMap());
                stmt.addBatch();
            }
        }
        return stmt;
    }

    private long executeNativeMerge() {
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

    public SQLMergeClause keys(Path<?>... paths){
        for (Path<?> path : paths){
            keys.add(path);
        }
        return this;
    }

    public SQLMergeClause select(SubQueryExpression<?> subQuery) {
        this.subQuery = subQuery;
        return this;
    }

    public <T> SQLMergeClause set(Path<T> path, @Nullable T value) {
        columns.add(path);
        if (value != null){
            values.add(new ConstantImpl<T>(value));
        }else{
            values.add(new NullExpr<T>(path.getType()));
        }
        return this;
    }

    @Override
    public String toString(){
        SQLSerializer serializer = new SQLSerializer(configuration.getTemplates(), true);
        serializer.serializeForMerge(metadata, entity, keys, columns, values, subQuery);
        return serializer.toString();
    }

    public SQLMergeClause values(Object... v) {
        for (Object value : v) {
            if (value instanceof Expression<?>) {
                values.add((Expression<?>) value);
            } else if (value != null){
                values.add(new ConstantImpl(value));
            }else{
                values.add(NullExpr.DEFAULT);
            }
        }
        return this;
    }

}
