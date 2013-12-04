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

import com.google.common.collect.ImmutableList;
import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.JoinFlag;
import com.mysema.query.Query;
import com.mysema.query.QueryException;
import com.mysema.query.QueryFlag;
import com.mysema.query.QueryFlag.Position;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.Tuple;
import com.mysema.query.support.Expressions;
import com.mysema.query.support.ProjectableQuery;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Expression;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.OperationImpl;
import com.mysema.query.types.ParamExpression;
import com.mysema.query.types.ParamNotSetException;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.QTuple;
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.query.ListSubQuery;
import com.mysema.query.types.template.NumberTemplate;
import com.mysema.query.types.template.SimpleTemplate;
import com.mysema.util.ResultSetAdapter;

/**
 * AbstractSQLQuery is the base type for SQL query implementations
 *
 * @author tiwe
 *
 * @param <Q> concrete subtype
 */
public abstract class AbstractSQLQuery<Q extends AbstractSQLQuery<Q> & Query<Q>> extends
        ProjectableQuery<Q> implements SQLCommonQuery<Q> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractSQLQuery.class);

    @Nullable
    private final Connection conn;

    @Nullable
    private List<Object> constants;

    @Nullable
    private List<Path<?>> constantPaths;

    @Nullable
    protected Expression<?> union;

    private final Configuration configuration;

    private final SQLListeners listeners;

    protected final QueryMixin<Q> queryMixin;

    protected boolean unionAll;

    public AbstractSQLQuery(@Nullable Connection conn, Configuration configuration) {
        this(conn, configuration, new DefaultQueryMetadata().noValidate());
    }

    @SuppressWarnings("unchecked")
    public AbstractSQLQuery(@Nullable Connection conn, Configuration configuration, QueryMetadata metadata) {
        super(new QueryMixin<Q>(metadata, false));
        this.queryMixin = super.queryMixin;
        this.queryMixin.setSelf((Q) this);
        this.conn = conn;
        this.configuration = configuration;
        this.listeners = new SQLListeners(configuration.getListeners());
    }

    /**
     * @param listener
     */
    public void addListener(SQLListener listener) {
        listeners.add(listener);
    }

    /**
     * Add the given String literal as a join flag to the last added join with the position
     * BEFORE_TARGET
     *
     * @param flag
     * @return
     */
    @Override
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
    @Override
    @SuppressWarnings("unchecked")
    public Q addJoinFlag(String flag, JoinFlag.Position position) {
        queryMixin.addJoinFlag(new JoinFlag(flag, position));
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
    @Override
    public Q addFlag(Position position, String prefix, Expression<?> expr) {
        Expression<?> flag = SimpleTemplate.create(expr.getType(), prefix + "{0}", expr);
        return queryMixin.addFlag(new QueryFlag(position, flag));
    }

    /**
     * Add the given query flag
     *
     * @param flag
     * @return
     */
    public Q addFlag(QueryFlag flag) {
        return queryMixin.addFlag(flag);
    }

    /**
     * Add the given String literal as query flag
     *
     * @param position
     * @param flag
     * @return
     */
    @Override
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
    @Override
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
        return limit(1).singleResult(NumberTemplate.ONE) != null;
    }

    /**
     * If you use forUpdate() with a backend that uses page or row locks, rows examined by the
     * query are write-locked until the end of the current transaction.
     *
     * Not supported for SQLite and CUBRID
     *
     * @return
     */
    public Q forUpdate() {
        return addFlag(SQLOps.FOR_UPDATE_FLAG);
    }

    protected SQLSerializer createSerializer() {
        return new SQLSerializer(configuration);
    }

    public Q from(Expression<?> arg) {
        return queryMixin.from(arg);
    }

    @Override
    public Q from(Expression<?>... args) {
        return queryMixin.from(args);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Q from(SubQueryExpression<?> subQuery, Path<?> alias) {
        return queryMixin.from(ExpressionUtils.as((Expression) subQuery, alias));
    }

    @Override
    public Q fullJoin(EntityPath<?> target) {
        return queryMixin.fullJoin(target);
    }

    @Override
    public <E> Q fullJoin(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.fullJoin(target, alias);
    }

    @Override
    public Q fullJoin(SubQueryExpression<?> target, Path<?> alias) {
        return queryMixin.fullJoin(target, alias);
    }

    @Override
    public <E> Q fullJoin(ForeignKey<E> key, RelationalPath<E> entity) {
        return queryMixin.fullJoin(entity).on(key.on(entity));
    }

    @Override
    public Q innerJoin(EntityPath<?> target) {
        return queryMixin.innerJoin(target);
    }

    @Override
    public <E> Q innerJoin(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    @Override
    public Q innerJoin(SubQueryExpression<?> target, Path<?> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    @Override
    public <E> Q innerJoin(ForeignKey<E> key, RelationalPath<E> entity) {
        return queryMixin.innerJoin(entity).on(key.on(entity));
    }

    @Override
    public Q join(EntityPath<?> target) {
        return queryMixin.join(target);
    }

    @Override
    public <E> Q join(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.join(target, alias);
    }

    @Override
    public Q join(SubQueryExpression<?> target, Path<?> alias) {
        return queryMixin.join(target, alias);
    }

    @Override
    public <E> Q join(ForeignKey<E> key, RelationalPath<E>  entity) {
        return queryMixin.join(entity).on(key.on(entity));
    }

    @Override
    public Q leftJoin(EntityPath<?> target) {
        return queryMixin.leftJoin(target);
    }

    @Override
    public <E> Q leftJoin(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    @Override
    public Q leftJoin(SubQueryExpression<?> target, Path<?> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    @Override
    public <E> Q leftJoin(ForeignKey<E> key, RelationalPath<E>  entity) {
        return queryMixin.leftJoin(entity).on(key.on(entity));
    }

    @Override
    public Q rightJoin(EntityPath<?> target) {
        return queryMixin.rightJoin(target);
    }

    @Override
    public <E> Q rightJoin(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.rightJoin(target, alias);
    }

    @Override
    public Q rightJoin(SubQueryExpression<?> target, Path<?> alias) {
        return queryMixin.rightJoin(target, alias);
    }

    @Override
    public <E> Q rightJoin(ForeignKey<E> key, RelationalPath<E>  entity) {
        return queryMixin.rightJoin(entity).on(key.on(entity));
    }

    @Nullable
    private <T> T get(ResultSet rs, Expression<?> expr, int i, Class<T> type) throws SQLException {
        return configuration.get(rs, expr instanceof Path ? (Path)expr : null, i, type);
    }

    private void set(PreparedStatement stmt, Path<?> path, int i, Object value) throws SQLException{
        configuration.set(stmt, path, i, value);
    }

    public QueryMetadata getMetadata() {
        return queryMixin.getMetadata();
    }

    /**
     * Get the query as an SQL query string and bindings
     *
     * @param exprs
     * @return
     */
    public SQLBindings getSQL(Expression<?>... exprs) {
        queryMixin.addProjection(exprs);
        String queryString = buildQueryString(false);
        ImmutableList.Builder<Object> args = ImmutableList.builder();
        Map<ParamExpression<?>, Object> params = getMetadata().getParams();
        for (Object o : constants) {
            if (o instanceof ParamExpression) {
                if (!params.containsKey(o)) {
                    throw new ParamNotSetException((ParamExpression<?>) o);
                }
                o = queryMixin.getMetadata().getParams().get(o);
            }
            args.add(o);
        }
        return new SQLBindings(queryString, args.build());
    }

    /**
     * Get the results as an JDBC result set
     *
     * @param args
     * @return
     */
    public ResultSet getResults(Expression<?>... exprs) {
        queryMixin.addProjection(exprs);
        String queryString = buildQueryString(false);
        if (logger.isDebugEnabled()) {
            logger.debug("query : {}", queryString);
        }
        listeners.notifyQuery(queryMixin.getMetadata());

        try {
            final PreparedStatement stmt = conn.prepareStatement(queryString);
            setParameters(stmt, constants, constantPaths, getMetadata().getParams());
            final ResultSet rs = stmt.executeQuery();

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
        this.union = UnionUtils.union(sq, unionAll);
        return new UnionImpl<Q ,RT>((Q)this, sq[0].getMetadata().getProjection());
    }

    protected Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public CloseableIterator<Tuple> iterate(Expression<?>... args) {
        return iterate(new QTuple(args));
    }

    @Override
    public <RT> CloseableIterator<RT> iterate(Expression<RT> expr) {
        expr = queryMixin.addProjection(expr);
        return iterateSingle(queryMixin.getMetadata(), expr);
    }

    @SuppressWarnings("unchecked")
    private <RT> CloseableIterator<RT> iterateSingle(QueryMetadata metadata, @Nullable final Expression<RT> expr) {
        final String queryString = buildQueryString(false);
        if (logger.isDebugEnabled()) {
            logger.debug("query : {}", queryString);
        }
        listeners.notifyQuery(queryMixin.getMetadata());
        try {
            final PreparedStatement stmt = conn.prepareStatement(queryString);
            setParameters(stmt, constants, constantPaths, metadata.getParams());
            final ResultSet rs = stmt.executeQuery();

            if (expr == null) {
                return new SQLResultIterator<RT>(stmt, rs) {
                    @Override
                    public RT produceNext(ResultSet rs) throws Exception {
                        return (RT) rs.getObject(1);
                    }
                };
            } else if (expr instanceof FactoryExpression) {
                return new SQLResultIterator<RT>(stmt, rs) {
                    @Override
                    public RT produceNext(ResultSet rs) throws Exception {
                        return newInstance((FactoryExpression<RT>) expr, rs, 0);
                    }
                };
            } else if (expr.getType().isArray()) {
                return new SQLResultIterator<RT>(stmt, rs) {
                    @Override
                    public RT produceNext(ResultSet rs) throws Exception {
                        Object[] rv = new Object[rs.getMetaData().getColumnCount()];
                        for (int i = 0; i < rv.length; i++) {
                            rv[i] = rs.getObject(i+1);
                        }
                        return (RT) rv;
                    }
                };
            } else {
                return new SQLResultIterator<RT>(stmt, rs) {
                    @Override
                    public RT produceNext(ResultSet rs) throws Exception {
                        return get(rs, expr, 1, expr.getType());
                    }
                };
            }

        } catch (SQLException e) {
            throw new QueryException("Caught " + e.getClass().getSimpleName() + " for " + queryString, e);

        } finally {
            reset();
        }
    }

    @Override
    public List<Tuple> list(Expression<?>... args) {
        return list(new QTuple(args));
    }

    @Override
    public <RT> List<RT> list(Expression<RT> expr) {
        expr = queryMixin.addProjection(expr);
        final String queryString = buildQueryString(false);
        if (logger.isDebugEnabled()) {
            logger.debug("query : {}", queryString);
        }
        listeners.notifyQuery(queryMixin.getMetadata());
        try {
            final PreparedStatement stmt = conn.prepareStatement(queryString);
            try {
                setParameters(stmt, constants, constantPaths, queryMixin.getMetadata().getParams());
                final ResultSet rs = stmt.executeQuery();
                try {
                    final List<RT> rv = new ArrayList<RT>();
                    if (expr instanceof FactoryExpression) {
                        FactoryExpression<RT> fe = (FactoryExpression<RT>)expr;
                        while (rs.next()) {
                            rv.add(newInstance(fe, rs, 0));
                        }
                    }  else if (expr.getType().isArray()) {
                        while (rs.next()) {
                            Object[] row = new Object[rs.getMetaData().getColumnCount()];
                            for (int i = 0; i < row.length; i++) {
                                row[i] = rs.getObject(i+1);
                            }
                            rv.add((RT)row);
                        }
                    } else {
                        while (rs.next()) {
                            rv.add(get(rs, expr, 1, expr.getType()));
                        }
                    }
                    return rv;
                } catch (IllegalAccessException e) {
                    throw new QueryException(e);
                } catch (InvocationTargetException e) {
                    throw new QueryException(e);
                } catch (InstantiationException e) {
                    throw new QueryException(e);
                } catch (SQLException e) {
                    throw new QueryException(e);
                } finally {
                    rs.close();
                }

            } finally {
                stmt.close();
            }
        } catch (SQLException e) {
            throw new QueryException("Caught " + e.getClass().getSimpleName() + " for " + queryString, e);

        } finally {
            reset();
        }
    }

    @Override
    public SearchResults<Tuple> listResults(Expression<?>... args) {
        return listResults(new QTuple(args));
    }

    @Override
    public <RT> SearchResults<RT> listResults(Expression<RT> expr) {
        queryMixin.addProjection(expr);
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

    public Q on(Predicate condition) {
        return queryMixin.on(condition);
    }

    @Override
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
        for (int i = 0; i < objects.size(); i++) {
            Object o = objects.get(i);
            try {
                if (o instanceof ParamExpression) {
                    if (!params.containsKey(o)) {
                        throw new ParamNotSetException((ParamExpression<?>) o);
                    }
                    o = params.get(o);
                }
                set(stmt, constantPaths.get(i), i+1, o);
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    @Override
    public String toString() {
        return buildQueryString(false).trim();
    }

    /**
     * Creates an union expression for the given subqueries
     *
     * @param <RT>
     * @param sq
     * @return
     */
    public <RT> Union<RT> union(ListSubQuery<RT>... sq) {
        return innerUnion(sq);
    }

    /**
     * Creates an union expression for the given subqueries
     *
     * @param <RT>
     * @param sq
     * @return
     */
    public <RT> Q union(Path<?> alias, ListSubQuery<RT>... sq) {
        return from(UnionUtils.union(sq, alias, false));
    }

    /**
     * Creates an union expression for the given subqueries
     *
     * @param <RT>
     * @param sq
     * @return
     */
    public <RT> Union<RT> union(SubQueryExpression<RT>... sq) {
        return innerUnion(sq);
    }

    /**
     * Creates an union expression for the given subqueries
     *
     * @param <RT>
     * @param sq
     * @return
     */
    public <RT> Q union(Path<?> alias, SubQueryExpression<RT>... sq) {
        return from(UnionUtils.union(sq, alias, false));
    }

    /**
     * Creates an union expression for the given subqueries
     *
     * @param <RT>
     * @param sq
     * @return
     */
    public <RT> Union<RT> unionAll(ListSubQuery<RT>... sq) {
        unionAll = true;
        return innerUnion(sq);
    }

    /**
     * Creates an union expression for the given subqueries
     *
     * @param <RT>
     * @param sq
     * @return
     */
    public <RT> Q unionAll(Path<?> alias, ListSubQuery<RT>... sq) {
        return from(UnionUtils.union(sq, alias, true));
    }

    /**
     * Creates an union expression for the given subqueries
     *
     * @param <RT>
     * @param sq
     * @return
     */
    public <RT> Union<RT> unionAll(SubQueryExpression<RT>... sq) {
        unionAll = true;
        return innerUnion(sq);
    }

    /**
     * Creates an union expression for the given subqueries
     *
     * @param <RT>
     * @param sq
     * @return
     */
    public <RT> Q unionAll(Path<?> alias, SubQueryExpression<RT>... sq) {
        return from(UnionUtils.union(sq, alias, true));
    }

    @Override
    public Tuple uniqueResult(Expression<?>... expr) {
        return uniqueResult(new QTuple(expr));
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
    public Q withRecursive(Path<?> alias, SubQueryExpression<?> query) {
        queryMixin.addFlag(new QueryFlag(QueryFlag.Position.WITH, SQLTemplates.RECURSIVE));
        return with(alias, query);
    }

    @Override
    public Q withRecursive(Path<?> alias, Expression<?> query) {
        queryMixin.addFlag(new QueryFlag(QueryFlag.Position.WITH, SQLTemplates.RECURSIVE));
        return with(alias, query);
    }

    @Override
    public WithBuilder<Q> withRecursive(Path<?> alias, Path<?>... columns) {
        queryMixin.addFlag(new QueryFlag(QueryFlag.Position.WITH, SQLTemplates.RECURSIVE));
        return with(alias, columns);
    }

    @Override
    public Q with(Path<?> alias, SubQueryExpression<?> query) {
        Expression<?> expr = OperationImpl.create(alias.getType(), SQLOps.WITH_ALIAS, alias, query);
        return queryMixin.addFlag(new QueryFlag(QueryFlag.Position.WITH, expr));
    }

    @Override
    public Q with(Path<?> alias, Expression<?> query) {
        Expression<?> expr = OperationImpl.create(alias.getType(), SQLOps.WITH_ALIAS, alias, query);
        return queryMixin.addFlag(new QueryFlag(QueryFlag.Position.WITH, expr));
    }

    @Override
    public WithBuilder<Q> with(Path<?> alias, Path<?>... columns) {
        Expression<?> columnsCombined = ExpressionUtils.list(Object.class, columns);
        Expression<?> aliasCombined = Expressions.operation(alias.getType(), SQLOps.WITH_COLUMNS, alias, columnsCombined);
        return new WithBuilder<Q>(queryMixin, aliasCombined);
    }

    private long unsafeCount() throws SQLException {
        final String queryString = buildQueryString(true);
        if (logger.isDebugEnabled()) {
            logger.debug("query : {}", queryString);
        }
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
