/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mysema.query.sql;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.commons.lang.Assert;
import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.IteratorAdapter;
import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.JoinExpression;
import com.mysema.query.JoinFlag;
import com.mysema.query.JoinType;
import com.mysema.query.Query;
import com.mysema.query.QueryException;
import com.mysema.query.QueryFlag;
import com.mysema.query.QueryFlag.Position;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.support.Expressions;
import com.mysema.query.support.ProjectableQuery;
import com.mysema.query.types.Expression;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.ParamExpression;
import com.mysema.query.types.ParamNotSetException;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.query.ListSubQuery;
import com.mysema.query.types.template.NumberTemplate;
import com.mysema.query.types.template.SimpleTemplate;
import com.mysema.util.ResultSetAdapter;

/**
 * AbstractSQLQuery is the base type for SQL query implementations
 *
 * @author tiwe
 */
public abstract class AbstractSQLQuery<Q extends AbstractSQLQuery<Q> & Query> extends
        ProjectableQuery<Q> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractSQLQuery.class);

    @Nullable
    private final Connection conn;

    @Nullable
    private List<Object> constants;

    @Nullable
    private List<Path<?>> constantPaths;

    @Nullable
    protected SubQueryExpression<?>[] union;

    private final Configuration configuration;
    
    protected final SQLQueryMixin<Q> queryMixin;
    
    protected boolean unionAll;

    public AbstractSQLQuery(@Nullable Connection conn, Configuration configuration) {
        this(conn, configuration, new DefaultQueryMetadata());
    }

    @SuppressWarnings("unchecked")
    public AbstractSQLQuery(@Nullable Connection conn, Configuration configuration, QueryMetadata metadata) {
        super(new SQLQueryMixin<Q>(metadata));
        this.queryMixin = (SQLQueryMixin<Q>)super.queryMixin;
        this.queryMixin.setSelf((Q) this);        
        this.conn = conn;
        this.configuration = configuration;
    }

    /**
     * Add the given String literal as a join flag to the last added join with the position 
     * BEFORE_TARGET
     *
     * @param flag
     * @return
     */
    public Q addJoinFlag(String flag) {
        return addJoinFlag(flag, JoinFlag.Position.BEFORE_TARGET);
    }

    /**
     * Add the given String literal as a join flag to the last added join
     *
     * @param flag
     * @param position
     * @return
     */
    @SuppressWarnings("unchecked")
    public Q addJoinFlag(String flag, JoinFlag.Position position) {
        List<JoinExpression> joins = queryMixin.getMetadata().getJoins();
        joins.get(joins.size()-1).addFlag(new JoinFlag(flag, position));
        return (Q)this;
    }

    /**
     * Add the given prefix and expression as a general query flag
     *
     * @param position position of the flag
     * @param prefix prefix for the flag
     * @param expr expression of the flag
     * @return
     */
    public Q addFlag(Position position, String prefix, Expression<?> expr) {
        Expression<?> flag = SimpleTemplate.create(expr.getType(), prefix + "{0}", expr);
        return queryMixin.addFlag(new QueryFlag(position, flag));
    }

    /**
     * Add the given String literal as query flag
     *
     * @param position
     * @param flag
     * @return
     */
    public Q addFlag(Position position, String flag) {
        return queryMixin.addFlag(new QueryFlag(position, flag));
    }

    /**
     * Add the given Expression as a query flag
     *
     * @param position
     * @param flag
     * @return
     */
    public Q addFlag(Position position, Expression<?> flag) {
        return queryMixin.addFlag(new QueryFlag(position, flag));
    }

    protected String buildQueryString(boolean forCountRow) {
        SQLSerializer serializer = createSerializer();
        if (union != null) {
            serializer.serializeUnion(union, queryMixin.getMetadata(), unionAll);
        } else {
            serializer.serialize(queryMixin.getMetadata(), forCountRow);
        }
        constants = serializer.getConstants();
        constantPaths = serializer.getConstantPaths();
        return serializer.toString();
    }

    private Expression<?> combineUnion(SubQueryExpression<?>[] union, Path<?> alias) {
        SQLTemplates templates = configuration.getTemplates();
        StringBuilder builder = new StringBuilder("(");
        String separator = unionAll ? templates.getUnionAll() : templates.getUnion();
        for (int i = 0; i < union.length; i++) {
            if (i > 0) builder.append(separator);
            builder.append("{"+i+"}");
        }
        builder.append(")");
        Expression<?> combined = Expressions.template(Object.class, builder.toString(), union);
        return ExpressionUtils.as((Expression)combined, alias);
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

    @Override
    public boolean exists() {
        return limit(1).uniqueResult(NumberTemplate.ONE) != null;
    }

    protected SQLSerializer createSerializer() {
        return new SQLSerializer(configuration.getTemplates());
    }

    public Q from(Expression<?>... args) {
        return queryMixin.from(args);
    }

    @SuppressWarnings("unchecked")
    public Q from(SubQueryExpression<?> subQuery, Path<?> alias) {
        return queryMixin.from(ExpressionUtils.as((Expression)subQuery, alias));
    }

    public Q fullJoin(RelationalPath<?> target) {
        return queryMixin.fullJoin(target);
    }
    
    public <E> Q fullJoin(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.fullJoin(target, alias);
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
    
    public <E> Q innerJoin(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.innerJoin(target, alias);
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
    
    public <E> Q join(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.join(target, alias);
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
    
    public <E> Q leftJoin(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.leftJoin(target, alias);
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
    
    public <E> Q rightJoin(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.rightJoin(target, alias);
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

    private <RT> Union<RT> innerUnion(SubQueryExpression<?>... sq) {
        queryMixin.getMetadata().setValidate(false);
        if (!queryMixin.getMetadata().getJoins().isEmpty()) {
            throw new IllegalArgumentException("Don't mix union and from");
        }
        this.union = sq;
        return new UnionImpl<Q ,RT>((Q)this, union[0].getMetadata().getProjection());
    }

    protected Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public CloseableIterator<Object[]> iterate(Expression<?>[] args) {     
        queryMixin.addToProjection(args);
        return iterateMultiple(queryMixin.getMetadata());
    }

    @Override
    public <RT> CloseableIterator<RT> iterate(Expression<RT> expr) {
        expr = queryMixin.convert(expr);
        queryMixin.addToProjection(expr);
        return iterateSingle(queryMixin.getMetadata(), expr);
    }

    private CloseableIterator<Object[]> iterateMultiple(QueryMetadata metadata) {
        String queryString = buildQueryString(false);
        logger.debug("query : {}", queryString);
        try {
            PreparedStatement stmt = Assert.notNull(conn, "connection").prepareStatement(queryString);
            final List<? extends Expression<?>> projection = metadata.getProjection();
            setParameters(stmt, constants, constantPaths, metadata.getParams());
            ResultSet rs = stmt.executeQuery();

            return new SQLResultIterator<Object[]>(stmt, rs) {

                @SuppressWarnings("unchecked")
                @Override
                protected Object[] produceNext(ResultSet rs) {
                    try {
                        List<Object> objects = new ArrayList<Object>(projection.size());
                        int index = 0;
                        for (int i = 0; i < projection.size(); i++) {
                            Expression<?> expr = projection.get(i);
                            if (expr instanceof FactoryExpression) {
                                objects.add(newInstance((FactoryExpression)expr, rs, index));
                                index += ((FactoryExpression)expr).getArgs().size();
                            } else if (expr.getType().isArray()) {
                                for (int j = index; j < rs.getMetaData().getColumnCount(); j++) {
                                    objects.add(get(rs, expr, index++ + 1, Object.class));
                                }
                                i = objects.size();
                            } else {
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
    private <RT> CloseableIterator<RT> iterateSingle(QueryMetadata metadata, @Nullable final Expression<RT> expr) {
        String queryString = buildQueryString(false);
        logger.debug("query : {}", queryString);
        try {
            PreparedStatement stmt = Assert.notNull(conn, "connection").prepareStatement(queryString);
            setParameters(stmt, constants, constantPaths, metadata.getParams());
            ResultSet rs = stmt.executeQuery();

            return new SQLResultIterator<RT>(stmt, rs) {

                @Override
                public RT produceNext(ResultSet rs) {
                    try {
                        if (expr == null) {
                            return (RT) rs.getObject(1);
                        } else if (expr instanceof FactoryExpression) {
                            return newInstance((FactoryExpression<RT>) expr, rs, 0);
                        } else if (expr.getType().isArray()) {
                            Object[] rv = new Object[rs.getMetaData().getColumnCount()];
                            for (int i = 0; i < rv.length; i++) {
                                rv[i] = rs.getObject(i+1);
                            }
                            return (RT) rv;
                        } else {
                            return get(rs, expr, 1, expr.getType());
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
                queryMixin.getMetadata().clearProjection();
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

    protected void setParameters(PreparedStatement stmt, List<?> objects, List<Path<?>> constantPaths, 
            Map<ParamExpression<?>, ?> params) {
        if (objects.size() != constantPaths.size()) {
            throw new IllegalArgumentException("Expected " + objects.size() + 
                    " paths, but got " + constantPaths.size());
        }
        int counter = 1;
        for (int i = 0; i < objects.size(); i++) {
            Object o = objects.get(i);
            try {
                if (ParamExpression.class.isInstance(o)) {
                    if (!params.containsKey(o)) {
                        throw new ParamNotSetException((ParamExpression<?>) o);
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

    public <RT> Union<RT> union(ListSubQuery<RT>... sq) {
        return innerUnion(sq);
    }
    
    public <RT> Q union(Path<?> alias, ListSubQuery<RT>... sq) {
        return from(combineUnion(sq, alias));
    }

    public <RT> Union<RT> union(SubQueryExpression<RT>... sq) {
        return innerUnion(sq);
    }
    
    public <RT> Q union(Path<?> alias, SubQueryExpression<RT>... sq) {
        return from(combineUnion(sq, alias));
    }
    
    public <RT> Union<RT> unionAll(ListSubQuery<RT>... sq) {
        unionAll = true;
        return innerUnion(sq);
    }
    
    public <RT> Q unionAll(Path<?> alias, ListSubQuery<RT>... sq) {
        return from(combineUnion(sq, alias));
    }

    public <RT> Union<RT> unionAll(SubQueryExpression<RT>... sq) {
        unionAll = true;
        return innerUnion(sq);
    }
    
    public <RT> Q unionAll(Path<?> alias, SubQueryExpression<RT>... sq) {
        return from(combineUnion(sq, alias));
    }

    @Override
    public <RT> RT uniqueResult(Expression<RT> expr) {
        if (getMetadata().getModifiers().getLimit() == null 
           && !expr.toString().contains("count(")) {
            limit(2);    
        }        
        CloseableIterator<RT> iterator = iterate(expr);
        return uniqueResult(iterator);
    }
    
    @Override
    public Object[] uniqueResult(Expression<?>[] expr) {
        if (getMetadata().getModifiers().getLimit() == null) {
            limit(2);    
        }        
        CloseableIterator<Object[]> iterator = iterate(expr);
        return uniqueResult(iterator);
    }

    private long unsafeCount() throws SQLException {
        String queryString = buildQueryString(true);
        logger.debug("query : {}", queryString);
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = Assert.notNull(conn, "connection").prepareStatement(queryString);
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
