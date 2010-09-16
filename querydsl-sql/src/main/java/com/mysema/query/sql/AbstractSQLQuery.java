/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.IteratorAdapter;
import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.JoinExpression;
import com.mysema.query.JoinFlag;
import com.mysema.query.QueryException;
import com.mysema.query.QueryFlag;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.QueryFlag.Position;
import com.mysema.query.support.ProjectableQuery;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.Expression;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.ParamExpression;
import com.mysema.query.types.ParamNotSetException;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.QBean;
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.custom.SimpleTemplate;
import com.mysema.query.types.expr.Param;
import com.mysema.query.types.query.ListSubQuery;
import com.mysema.util.ResultSetAdapter;

/**
 * AbstractSQLQuery is the base type for SQL query implementations
 *
 * @author tiwe
 * @version $Id$
 */
public abstract class AbstractSQLQuery<Q extends AbstractSQLQuery<Q>> extends
        ProjectableQuery<Q> {

    public class UnionBuilder<RT> implements Union<RT> {

        @Override
        @SuppressWarnings("unchecked")
        public List<RT> list() {
            if (union[0].getMetadata().getProjection().size() == 1) {
                return (List<RT>) IteratorAdapter.asList(iterateSingle(null));
            } else {
                return (List<RT>) IteratorAdapter.asList(iterateMultiple());
            }
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public CloseableIterator<RT> iterate() {
            if (union[0].getMetadata().getProjection().size() == 1) {
                return (CloseableIterator<RT>) iterateSingle(null);
            } else {
                return (CloseableIterator<RT>) iterateMultiple();
            }
        }

        @Override
        public UnionBuilder<RT> orderBy(OrderSpecifier<?>... o) {
            AbstractSQLQuery.this.orderBy(o);
            return this;
        }
        
        @Override
        public String toString(){
            return AbstractSQLQuery.this.toString();
        }

    }

    private static final Logger logger = LoggerFactory.getLogger(AbstractSQLQuery.class);

    @Nullable
    private final Connection conn;

    @Nullable
    private List<Object> constants;

    @Nullable
    private List<Path<?>> constantPaths;
    
    @Nullable
    private SubQueryExpression<?>[] union;

    private final Configuration configuration;

    public AbstractSQLQuery(@Nullable Connection conn, Configuration configuration) {
        this(conn, configuration, new DefaultQueryMetadata());
    }

    @SuppressWarnings("unchecked")
    public AbstractSQLQuery(@Nullable Connection conn, Configuration configuration, QueryMetadata metadata) {
        super(new QueryMixin<Q>(metadata));
        this.queryMixin.setSelf((Q) this);
        this.conn = conn;
        this.configuration = configuration;
    }
    
    @SuppressWarnings("unchecked")
    protected Q addJoinFlag(String flag){
        List<JoinExpression> joins = queryMixin.getMetadata().getJoins();
        joins.get(joins.size()-1).addFlag(new JoinFlag(flag));
        return (Q)this;
    }

    protected Q addFlag(Position position, String prefix, Expression<?> expr){
        Expression<?> flag = SimpleTemplate.create(expr.getType(), prefix + "{0}", expr);
        return queryMixin.addFlag(new QueryFlag(position, flag));
    }
    
    protected Q addFlag(Position position, String flag){
        return queryMixin.addFlag(new QueryFlag(position, flag));
    }
    
    protected Q addFlag(Position position, Expression<?> flag){
        return queryMixin.addFlag(new QueryFlag(position, flag));
    }
    
    protected String buildQueryString(boolean forCountRow) {
        SQLSerializer serializer = createSerializer();
        if (union != null) {
            serializer.serializeUnion(union, queryMixin.getMetadata().getOrderBy());
        } else {
            serializer.serialize(queryMixin.getMetadata(), forCountRow);
        }
        constants = serializer.getConstants();
        constantPaths = serializer.getConstantPaths();
        return serializer.toString();
    }

    @Override
    public long count() {
        try {
            return unsafeCount();
        } catch (SQLException e) {
            String error = "Caught " + e.getClass().getName();
            logger.error(error, e);
            throw new QueryException(e.getMessage(), e);
        }
    }

    protected SQLSerializer createSerializer() {
        return new SQLSerializer(configuration.getTemplates());
    }

    public Q from(Expression<?>... args) {
        return queryMixin.from(args);
    }

    public Q fullJoin(RelationalPath<?> target) {
        return queryMixin.fullJoin(target);
    }

    public Q fullJoin(SubQueryExpression<?> target, Path<?> alias) {
        return queryMixin.fullJoin(target, alias);
    }

    public <E> Q fullJoin(ForeignKey<E> key, RelationalPath<E> entity) {
        return queryMixin.fullJoin(entity).on(key.on(entity));
    }

    public Q innerJoin(RelationalPath<?> target) {
        return queryMixin.innerJoin(target);
    }

    public Q innerJoin(SubQueryExpression<?> target, Path<?> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    public <E> Q innerJoin(ForeignKey<E> key, RelationalPath<E> entity) {
        return queryMixin.innerJoin(entity).on(key.on(entity));
    }

    public Q join(RelationalPath<?> target) {
        return queryMixin.join(target);
    }

    public Q join(SubQueryExpression<?> target, Path<?> alias) {
        return queryMixin.join(target, alias);
    }

    public <E> Q join(ForeignKey<E> key, RelationalPath<E>  entity) {
        return queryMixin.join(entity).on(key.on(entity));
    }

    public Q leftJoin(RelationalPath<?> target) {
        return queryMixin.leftJoin(target);
    }

    public Q leftJoin(SubQueryExpression<?> target, Path<?> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    public <E> Q leftJoin(ForeignKey<E> key, RelationalPath<E>  entity) {
        return queryMixin.leftJoin(entity).on(key.on(entity));
    }

    public Q rightJoin(RelationalPath<?> target) {
        return queryMixin.rightJoin(target);
    }

    public Q rightJoin(SubQueryExpression<?> target, Path<?> alias) {
        return queryMixin.rightJoin(target, alias);
    }

    public <E> Q rightJoin(ForeignKey<E> key, RelationalPath<E>  entity) {
        return queryMixin.rightJoin(entity).on(key.on(entity));
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private <T> T get(ResultSet rs, Expression<?> expr, int i, Class<T> type) throws SQLException {
        return configuration.get(rs, expr instanceof Path ? (Path)expr : null, i, type);
    }
    
    private int set(PreparedStatement stmt, Path<?> path, int i, Object value) throws SQLException{
        return configuration.set(stmt, path, i, value);
    }
    
    public QueryMetadata getMetadata() {
        return queryMixin.getMetadata();
    }

    public ResultSet getResults(Expression<?>... exprs) {
        queryMixin.addToProjection(exprs);
        String queryString = buildQueryString(false);
        logger.debug("query : {}", queryString);

        try {
            final PreparedStatement stmt = conn.prepareStatement(queryString);
            setParameters(stmt, constants, constantPaths, getMetadata().getParams());
            ResultSet rs = stmt.executeQuery();

            return new ResultSetAdapter(rs) {
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
            throw new QueryException(e);

        } finally {
            reset();
        }
    }

    private <RT> UnionBuilder<RT> innerUnion(SubQueryExpression<?>... sq) {
        if (!queryMixin.getMetadata().getJoins().isEmpty()) {
            throw new IllegalArgumentException("Don't mix union and from");
        }
        this.union = sq;
        return new UnionBuilder<RT>();
    }

    protected Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public CloseableIterator<Object[]> iterate(Expression<?>[] args) {
        queryMixin.addToProjection(args);
        return iterateMultiple();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT> CloseableIterator<RT> iterate(Expression<RT> expr) {
        if (expr instanceof RelationalPath<?>){
            try{
                Map<String,Expression<?>> bindings = new HashMap<String,Expression<?>>();
                for (Field field : expr.getClass().getFields()){
                    if (Expression.class.isAssignableFrom(field.getType()) && !Modifier.isStatic(field.getModifiers())){
                        field.setAccessible(true);
                        Expression<?> column = (Expression<?>) field.get(expr);
                        bindings.put(field.getName(), column);
                    }
                }
                QBean<RT> bean = new QBean(expr.getType(), bindings);
                return iterate(bean);
            }catch(IllegalAccessException e){
                throw new QueryException(e);
            }
        }else{
            queryMixin.addToProjection(expr);
            return iterateSingle(expr);    
        }    
    }

    private CloseableIterator<Object[]> iterateMultiple() {
        String queryString = buildQueryString(false);
        logger.debug("query : {}", queryString);
        try {
            PreparedStatement stmt = conn.prepareStatement(queryString);
            final List<? extends Expression<?>> projection = getMetadata().getProjection();
            setParameters(stmt, constants, constantPaths, getMetadata().getParams());
            ResultSet rs = stmt.executeQuery();

            return new SQLResultIterator<Object[]>(stmt, rs) {

                @SuppressWarnings("unchecked")
                @Override
                protected Object[] produceNext(ResultSet rs) {
                    try {
                        List<Object> objects = new ArrayList<Object>(projection.size());
                        int index = 0;
                        for (int i = 0; i < projection.size(); i++){
                            Expression<?> expr = projection.get(i);
                            if (expr instanceof FactoryExpression){
                                objects.add(newInstance((FactoryExpression)expr, rs, index));
                                index += ((FactoryExpression)expr).getArgs().size();
                            }else if (expr.getType().isArray()){
                                for (int j = index; j < rs.getMetaData().getColumnCount(); j++){
                                    objects.add(get(rs, expr, index++ + 1, Object.class));
                                }
                                i = objects.size();
                            }else{
                                objects.add(get(rs, expr, index++ + 1, expr.getType()));
                            }
                        }
                        return objects.toArray();
                    } catch (InstantiationException e) {
                        close();
                        throw new QueryException(e);
                    } catch (IllegalAccessException e) {
                        close();
                        throw new QueryException(e);
                    } catch (InvocationTargetException e) {
                        close();
                        throw new QueryException(e);
                    } catch (SQLException e) {
                        close();
                        throw new QueryException(e);
                    }
                }

            };

        } catch (SQLException e) {
            throw new QueryException(e);

        } finally {
            reset();
        }

    }

    @SuppressWarnings("unchecked")
    private <RT> CloseableIterator<RT> iterateSingle(@Nullable final Expression<RT> expr) {
        String queryString = buildQueryString(false);
        logger.debug("query : {}", queryString);
        try {
            PreparedStatement stmt = conn.prepareStatement(queryString);
            setParameters(stmt, constants, constantPaths, getMetadata().getParams());
            ResultSet rs = stmt.executeQuery();

            return new SQLResultIterator<RT>(stmt, rs) {

                @Override
                public RT produceNext(ResultSet rs) {
                    try {
                        if (expr == null){
                            return (RT) rs.getObject(1);
                        }else if (expr instanceof FactoryExpression) {
                            return newInstance((FactoryExpression<RT>) expr, rs, 0);
                        }else if (expr.getType().isArray()){
                            Object[] rv = new Object[rs.getMetaData().getColumnCount()];
                            for (int i = 0; i < rv.length; i++){
                                rv[i] = rs.getObject(i+1);
                            }
                            return (RT) rv;
                        } else{
                            return (RT) get(rs, expr, 1, expr.getType());
                        }
                    } catch (IllegalAccessException e) {
                        close();
                        throw new QueryException(e);
                    } catch (InvocationTargetException e) {
                        close();
                        throw new QueryException(e);
                    } catch (InstantiationException e) {
                        close();
                        throw new QueryException(e);
                    } catch (SQLException e) {
                        close();
                        throw new QueryException(e);
                    }
                }

            };

        } catch (SQLException e) {
            throw new QueryException("Caught " + e.getClass().getSimpleName() + " for " + queryString, e);

        } finally {
            reset();
        }
    }

    @Override
    public List<Object[]> list(Expression<?>[] args) {
        return IteratorAdapter.asList(iterate(args));
    }

    @Override
    public <RT> List<RT> list(Expression<RT> expr) {
        return IteratorAdapter.asList(iterate(expr));
    }

    @Override
    public <RT> SearchResults<RT> listResults(Expression<RT> expr) {
        queryMixin.addToProjection(expr);
        long total = count();
        try {
            if (total > 0) {
                QueryModifiers modifiers = queryMixin.getMetadata().getModifiers();
                return new SearchResults<RT>(list(expr), modifiers, total);
            } else {
                return SearchResults.emptyResults();
            }

        } finally {
            reset();
        }
    }

    private <RT> RT newInstance(FactoryExpression<RT> c, ResultSet rs, int offset)
        throws InstantiationException, IllegalAccessException, InvocationTargetException, SQLException{
        Object[] args = new Object[c.getArgs().size()];
        for (int i = 0; i < args.length; i++) {
            args[i] = get(rs, c.getArgs().get(i), offset + i + 1, c.getArgs().get(i).getType());
        }
        return c.newInstance(args);
    }

    public Q on(Predicate... conditions) {
        return queryMixin.on(conditions);
    }

    private void reset() {
        queryMixin.getMetadata().reset();
        constants = null;
    }
    
    protected void setParameters(PreparedStatement stmt, List<?> objects, List<Path<?>> constantPaths, Map<ParamExpression<?>, ?> params){
        if (objects.size() != constantPaths.size()){
            throw new IllegalArgumentException("Expected " + objects.size() + " paths, but got " + constantPaths.size());
        }
        int counter = 1;
        for (int i = 0; i < objects.size(); i++){
            Object o = objects.get(i);        
            try {
                if (Param.class.isInstance(o)){
                    if (!params.containsKey(o)){
                        throw new ParamNotSetException((Param<?>) o);
                    }
                    o = params.get(o);
                }
                counter += set(stmt, constantPaths.get(i), counter, o);
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    @Override
    public String toString() {
        return buildQueryString(false).trim();
    }

    public <RT> UnionBuilder<RT> union(ListSubQuery<RT>... sq) {
        return innerUnion(sq);
    }

    public <RT> UnionBuilder<RT> union(SubQueryExpression<RT>... sq) {
        return innerUnion(sq);
    }

    @Override
    public <RT> RT uniqueResult(Expression<RT> expr) {
        CloseableIterator<RT> iterator = iterate(expr);
        try{
            if (iterator.hasNext()){
                return iterator.next();
            }else{
                return null;
            }
        }finally{
            try {
                iterator.close();
            } catch (IOException e) {
                throw new QueryException(e);
            }
        }
        
    }

    private long unsafeCount() throws SQLException {
        String queryString = buildQueryString(true);
        logger.debug("query : {}", queryString);
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(queryString);
            setParameters(stmt, constants, constantPaths, getMetadata().getParams());
            rs = stmt.executeQuery();
            rs.next();
            return rs.getLong(1);

        } catch (SQLException e) {
            throw new QueryException(e.getMessage(), e);

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        }
    }
    
}
