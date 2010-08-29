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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.commons.collections15.BeanMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.commons.lang.Assert;
import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryException;
import com.mysema.query.QueryFlag;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryFlag.Position;
import com.mysema.query.dml.InsertClause;
import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.SQLSerializer;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Param;
import com.mysema.query.types.Path;
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.expr.ExprConst;
import com.mysema.query.types.path.NullExpr;
import com.mysema.util.ResultSetAdapter;

/**
 * SQLInsertClause defines an INSERT INTO clause
 *
 * @author tiwe
 *
 */
public class SQLInsertClause extends AbstractSQLClause implements InsertClause<SQLInsertClause> {

    private static final Logger logger = LoggerFactory.getLogger(SQLInsertClause.class);

    private final Connection connection;

    private final RelationalPath<?> entity;
    
    private final QueryMetadata metadata = new DefaultQueryMetadata();

    @Nullable
    private SubQueryExpression<?> subQuery;

    private final List<SQLInsertBatch> batches = new ArrayList<SQLInsertBatch>();
    
    private final List<Path<?>> columns = new ArrayList<Path<?>>();
    
    private final List<Expr<?>> values = new ArrayList<Expr<?>>();

    private transient String queryString;
    
    public SQLInsertClause(Connection connection, SQLTemplates templates, RelationalPath<?> entity) {
        this(connection, new Configuration(templates), entity);
    }
    
    public SQLInsertClause(Connection connection, Configuration configuration, RelationalPath<?> entity) {
        super(configuration);
        this.connection = Assert.notNull(connection,"connection");
        this.entity = Assert.notNull(entity,"entity");
    }
    
    public SQLInsertClause addFlag(Position position, String flag){
        metadata.addFlag(new QueryFlag(position, flag));
        return this;
    }
    
    public SQLInsertClause addBatch() {
        batches.add(new SQLInsertBatch(columns, values, subQuery));
        columns.clear();
        values.clear();
        subQuery = null;
        return this;
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
    public <T> T executeWithKey(Path<T> path){
        ResultSet rs = executeWithKeys();
        try{
            if (rs.next()){
                return configuration.get(rs, path, 1, path.getType());
            }else{
                return null;
            }
        } catch (SQLException e) {
            throw new QueryException(e.getMessage(), e);
        }finally{
            close(rs);
        }
    }

    public <T> List<T> executeWithKeys(Path<T> path){
        ResultSet rs = executeWithKeys();
        try{
            List<T> rv = new ArrayList<T>();
            while (rs.next()){
                rv.add(configuration.get(rs, path, 1, path.getType()));
            }
            return rv;
        } catch (SQLException e) {
            throw new QueryException(e.getMessage(), e);
        }finally{
            close(rs);
        }
    }
    
    private PreparedStatement createStatement(boolean withKeys) throws SQLException{
        SQLSerializer serializer = new SQLSerializer(configuration.getTemplates(), true);
        PreparedStatement stmt = null;
        if (batches.isEmpty()){
            serializer.serializeForInsert(metadata, entity, columns, values, subQuery);
            stmt = prepareStatementAndSetParameters(serializer, withKeys);    
        }else{
            serializer.serializeForInsert(metadata, entity, batches.get(0).getColumns(), batches.get(0).getValues(), batches.get(0).getSubQuery());
            stmt = prepareStatementAndSetParameters(serializer, withKeys);
            
            // add first batch
            stmt.addBatch();
            
            // add other batches
            for (int i = 1; i < batches.size(); i++){
                SQLInsertBatch batch = batches.get(i);
                serializer = new SQLSerializer(configuration.getTemplates(), true);
                // TODO : add support for dry serialization (without SQL construction)
                serializer.serializeForInsert(metadata, entity, batch.getColumns(), batch.getValues(), batch.getSubQuery());
                setParameters(stmt, serializer.getConstants(),Collections.<Param<?>,Object>emptyMap());
                stmt.addBatch();
            }
        }
        return stmt;        
    }

    private PreparedStatement prepareStatementAndSetParameters(SQLSerializer serializer, boolean withKeys) throws SQLException {
        queryString = serializer.toString();
        logger.debug(queryString);        
        PreparedStatement stmt;
        if (withKeys){
            stmt = connection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);                
        }else{
            stmt = connection.prepareStatement(queryString);
        }            
        setParameters(stmt, serializer.getConstants(),Collections.<Param<?>,Object>emptyMap());
        return stmt;
    }
    
    public ResultSet executeWithKeys(){        
        try {
            final PreparedStatement stmt = createStatement(true);
            if (batches.isEmpty()){
                stmt.executeUpdate();    
            }else{
                stmt.executeBatch();
            }               
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
        PreparedStatement stmt = null;
        try {            
            stmt = createStatement(false);
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
    public SQLInsertClause select(SubQueryExpression<?> sq) {
        subQuery = sq;
        return this;
    }

    @Override
    public <T> SQLInsertClause set(Path<T> path, T value) {
        columns.add(path);
        if (value instanceof Expr<?>){ 
            values.add((Expr<?>)value);        
        }else if (value != null){
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
        SQLSerializer serializer = new SQLSerializer(configuration.getTemplates(), true);
        serializer.serializeForInsert(metadata, entity, columns, values, subQuery);
        return serializer.toString();
    }

    @SuppressWarnings("unchecked")
    public SQLInsertClause populate(Object bean) {
        try {
            BeanMap map = new BeanMap(bean);
            for (Map.Entry entry : map.entrySet()){
                String property = entry.getKey().toString();
                if (!property.equals("class")){
                    Path path = (Path<?>) entity.getClass().getField(property).get(entity);
                    set(path, entry.getValue());    
                }                
            }
            return this;
        } catch (SecurityException e) {
            throw new QueryException(e);
        } catch (IllegalAccessException e) {
            throw new QueryException(e);
        } catch (NoSuchFieldException e) {
            throw new QueryException(e);
        }            
    }

}
