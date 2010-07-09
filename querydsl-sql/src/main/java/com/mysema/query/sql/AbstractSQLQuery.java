/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.IteratorAdapter;
import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryException;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.support.ProjectableQuery;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.EConstructor;
import com.mysema.query.types.Expr;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Path;
import com.mysema.query.types.SubQuery;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.query.ListSubQuery;
import com.mysema.util.JDBCUtil;
import com.mysema.util.MathUtils;
import com.mysema.util.ResultSetAdapter;

/**
 * AbstractSQLQuery is the base type for SQL query implementations
 *
 * @author tiwe
 * @version $Id$
 */
@edu.umd.cs.findbugs.annotations.SuppressWarnings("SQL_PREPARED_STATEMENT_GENERATED_FROM_NONCONSTANT_STRING")
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
    private SubQuery<?>[] union;

    private final SQLTemplates templates;

    public AbstractSQLQuery(@Nullable Connection conn, SQLTemplates templates) {
        this(conn, templates, new DefaultQueryMetadata());
    }

    @SuppressWarnings("unchecked")
    public AbstractSQLQuery(@Nullable Connection conn, SQLTemplates templates,
            QueryMetadata metadata) {
        super(new QueryMixin<Q>(metadata));
        this.queryMixin.setSelf((Q) this);
        this.conn = conn;
        this.templates = templates;
    }

    protected String buildQueryString(boolean forCountRow) {
        SQLSerializer serializer = createSerializer();
        if (union != null) {
            serializer.serializeUnion(union, queryMixin.getMetadata().getOrderBy());
        } else {
            serializer.serialize(queryMixin.getMetadata(), forCountRow);
        }
        constants = serializer.getConstants();
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
        return new SQLSerializer(templates);
    }

    public Q from(Expr<?>... args) {
        return queryMixin.from(args);
    }

    public Q fullJoin(PEntity<?> target) {
        return queryMixin.fullJoin(target);
    }

    public Q fullJoin(SubQuery<?> target, Path<?> alias) {
        return queryMixin.fullJoin(target, alias);
    }

    public <E> Q fullJoin(ForeignKey<E> key, PEntity<E> entity) {
        return queryMixin.fullJoin(entity).on(key.on(entity));
    }

    public Q innerJoin(PEntity<?> target) {
        return queryMixin.innerJoin(target);
    }

    public Q innerJoin(SubQuery<?> target, Path<?> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    public <E> Q innerJoin(ForeignKey<E> key, PEntity<E> entity) {
        return queryMixin.innerJoin(entity).on(key.on(entity));
    }

    public Q join(PEntity<?> target) {
        return queryMixin.join(target);
    }

    public Q join(SubQuery<?> target, Path<?> alias) {
        return queryMixin.join(target, alias);
    }

    public <E> Q join(ForeignKey<E> key, PEntity<E>  entity) {
        return queryMixin.join(entity).on(key.on(entity));
    }

    public Q leftJoin(PEntity<?> target) {
        return queryMixin.leftJoin(target);
    }

    public Q leftJoin(SubQuery<?> target, Path<?> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    public <E> Q leftJoin(ForeignKey<E> key, PEntity<E>  entity) {
        return queryMixin.leftJoin(entity).on(key.on(entity));
    }

    public Q rightJoin(PEntity<?> target) {
        return queryMixin.rightJoin(target);
    }

    public Q rightJoin(SubQuery<?> target, Path<?> alias) {
        return queryMixin.rightJoin(target, alias);
    }

    public <E> Q rightJoin(ForeignKey<E> key, PEntity<E>  entity) {
        return queryMixin.rightJoin(entity).on(key.on(entity));
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private <T> T get(ResultSet rs, int i, Class<T> type) {
        try {
            Object value = rs.getObject(i);
            if (value != null && !type.isAssignableFrom(value.getClass())){
                if (Number.class.isAssignableFrom(type)){
                    return (T)MathUtils.cast((Number)value, (Class)type);
                }else{
                    throw new IllegalArgumentException(
                        "Unable to cast " + value.getClass().getName() + " to " + type.getName());
                }
            }else{
                return (T)value;
            }
        } catch (SQLException e) {
            throw new QueryException(e);
        }
    }

    public QueryMetadata getMetadata() {
        return queryMixin.getMetadata();
    }

    public ResultSet getResults(Expr<?>... exprs) {
        queryMixin.addToProjection(exprs);
        String queryString = buildQueryString(false);
        logger.debug("query : {}", queryString);

        try {
            final PreparedStatement stmt = conn.prepareStatement(queryString);
            JDBCUtil.setParameters(stmt, constants, getMetadata().getParams());
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

    protected SQLTemplates getTemplates() {
        return templates;
    }

    private <RT> UnionBuilder<RT> innerUnion(SubQuery<?>... sq) {
        if (!queryMixin.getMetadata().getJoins().isEmpty()) {
            throw new IllegalArgumentException("Don't mix union and from");
        }
        this.union = sq;
        return new UnionBuilder<RT>();
    }

    @Override
    public CloseableIterator<Object[]> iterate(Expr<?>[] args) {
        queryMixin.addToProjection(args);
        return iterateMultiple();
    }

    @Override
    public <RT> CloseableIterator<RT> iterate(Expr<RT> expr) {
        queryMixin.addToProjection(expr);
        return iterateSingle(expr);
    }

    private CloseableIterator<Object[]> iterateMultiple() {
        String queryString = buildQueryString(false);
        logger.debug("query : {}", queryString);
        try {
            PreparedStatement stmt = conn.prepareStatement(queryString);
            final List<? extends Expr<?>> projection = getMetadata().getProjection();
            JDBCUtil.setParameters(stmt, constants, getMetadata().getParams());
            ResultSet rs = stmt.executeQuery();

            return new SQLResultIterator<Object[]>(stmt, rs) {

                @SuppressWarnings("unchecked")
                @Override
                protected Object[] produceNext(ResultSet rs) {
                    try {
                        List<Object> objects = new ArrayList<Object>(projection.size());
                        int index = 0;
                        for (int i = 0; i < projection.size(); i++){
                            Expr<?> expr = projection.get(i);
                            if (expr instanceof EConstructor){
                                objects.add(newInstance((EConstructor)expr, rs, index));
                                index += ((EConstructor)expr).getArgs().size();
                            }else if (expr.getType().isArray()){
                                for (int j = index; j < rs.getMetaData().getColumnCount(); j++){
                                    objects.add(get(rs, index++ + 1, Object.class));
                                }
                                i = objects.size();
                            }else{
                                objects.add(get(rs, index++ + 1, expr.getType()));
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
    private <RT> CloseableIterator<RT> iterateSingle(@Nullable final Expr<RT> expr) {
        String queryString = buildQueryString(false);
        logger.debug("query : {}", queryString);
        try {
            PreparedStatement stmt = conn.prepareStatement(queryString);
            JDBCUtil.setParameters(stmt, constants, getMetadata().getParams());
            ResultSet rs = stmt.executeQuery();

            return new SQLResultIterator<RT>(stmt, rs) {

                @Override
                public RT produceNext(ResultSet rs) {
                    try {
                        if (expr == null){
                            return (RT) rs.getObject(1);
                        }else if (expr instanceof EConstructor) {
                            return newInstance((EConstructor<RT>) expr, rs, 0);
                        }else if (expr.getType().isArray()){
                            Object[] rv = new Object[rs.getMetaData().getColumnCount()];
                            for (int i = 0; i < rv.length; i++){
                                rv[i] = rs.getObject(i+1);
                            }
                            return (RT) rv;
                        } else{
                            return (RT) get(rs, 1, expr.getType());
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
            throw new QueryException(e);

        } finally {
            reset();
        }
    }

    @Override
    public List<Object[]> list(Expr<?>[] args) {
        return IteratorAdapter.asList(iterate(args));
    }

    @Override
    public <RT> List<RT> list(Expr<RT> expr) {
        return IteratorAdapter.asList(iterate(expr));
    }

    @Override
    public <RT> SearchResults<RT> listResults(Expr<RT> expr) {
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

    private <RT> RT newInstance(EConstructor<RT> c, ResultSet rs, int offset)
        throws InstantiationException, IllegalAccessException, InvocationTargetException{
        Object[] args = new Object[c.getArgs().size()];
        for (int i = 0; i < args.length; i++) {
            args[i] = get(rs, offset + i + 1, c.getArgs().get(i).getType());
        }
        return c.newInstance(args);
    }

    public Q on(EBoolean... conditions) {
        return queryMixin.on(conditions);
    }

    private void reset() {
        queryMixin.getMetadata().reset();
        constants = null;
    }

    @Override
    public String toString() {
        return buildQueryString(false).trim();
    }

    public <RT> UnionBuilder<RT> union(ListSubQuery<RT>... sq) {
        return innerUnion(sq);
    }

    public <RT> UnionBuilder<RT> union(SubQuery<RT>... sq) {
        return innerUnion(sq);
    }

    @Override
    public <RT> RT uniqueResult(Expr<RT> expr) {
        List<RT> list = list(expr);
        return !list.isEmpty() ? list.get(0) : null;
    }

    private long unsafeCount() throws SQLException {
        String queryString = buildQueryString(true);
        logger.debug("query : {}", queryString);
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(queryString);
            JDBCUtil.setParameters(stmt, constants, getMetadata().getParams());
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
