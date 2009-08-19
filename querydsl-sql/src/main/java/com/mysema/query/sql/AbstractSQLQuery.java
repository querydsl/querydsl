/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.Query;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.support.QueryBaseWithProjectionAndDetach;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.EConstructor;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.query.ListSubQuery;
import com.mysema.query.types.query.ObjectSubQuery;
import com.mysema.query.types.query.SubQuery;

/**
 * AbstractSqlQuery is the base type for SQL query implementations
 * 
 * @author tiwe
 * @version $Id$
 */
public class AbstractSQLQuery<SubType extends AbstractSQLQuery<SubType>>
        extends QueryBaseWithProjectionAndDetach<SubType> implements Query<SubType> {

    private static final Logger logger = LoggerFactory
            .getLogger(AbstractSQLQuery.class);

    private String queryString;

    private List<Object> constants;

    private final Connection conn;

    protected final SQLTemplates templates;

    private SubQuery[] sq;

    public AbstractSQLQuery(Connection conn, SQLTemplates templates) {
        this.conn = conn;
        this.templates = templates;
    }

    public List<Object[]> list(Expr<?> expr1, Expr<?> expr2, Expr<?>... rest) {
        addToProjection(expr1, expr2);
        addToProjection(rest);
        try {
            return listMultiple();
        } catch (SQLException e) {
            String error = "Caught " + e.getClass().getName();
            logger.error(error, e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private List<Object[]> listMultiple() throws SQLException {
        String queryString = toString();
        logger.debug("query : {}", queryString);
        PreparedStatement stmt = conn.prepareStatement(queryString);
        int counter = 1;
        for (Object o : constants) {
            try {
                set(stmt, counter++, o);
            } catch (Exception e) {
                String error = "Caught " + e.getClass().getName();
                logger.error(error, e);
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        ResultSet rs = stmt.executeQuery();
        try {
            List<Object[]> rv = new ArrayList<Object[]>();
            while (rs.next()) {
                // TODO : take constructors into account
                Object[] objects = new Object[rs.getMetaData().getColumnCount()];
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    objects[i] = rs.getObject(i + 1);
                }
                rv.add(objects);
            }
            return rv;
        } finally {
            try {
                rs.close();
            } finally {
                stmt.close();
            }
        }
    }

    public <RT> List<RT> list(Expr<RT> expr) {
        addToProjection(expr);
        try {
            return listSingle(expr);
        } catch (SQLException e) {
            String error = "Caught " + e.getClass().getName();
            logger.error(error, e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public <RT> SearchResults<RT> listResults(Expr<RT> expr) {
        addToProjection(expr);
        long total = count();
        if (total > 0) {
            QueryModifiers modifiers = getMetadata().getModifiers();
            return new SearchResults<RT>(list(expr), modifiers, total);
        } else {
            return SearchResults.emptyResults();
        }
    }

    @SuppressWarnings("unchecked")
    private <RT> List<RT> listSingle(Expr<RT> expr) throws SQLException {
        String queryString = toString();
        logger.debug("query : {}", queryString);
        PreparedStatement stmt = conn.prepareStatement(queryString);
        int counter = 1;
        for (Object o : constants) {
            try {
                set(stmt, counter++, o);
            } catch (Exception e) {
                String error = "Caught " + e.getClass().getName();
                logger.error(error, e);
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        ResultSet rs = stmt.executeQuery();
        try {
            List<RT> rv = new ArrayList<RT>();
            if (expr instanceof EConstructor) {
                EConstructor<RT> c = (EConstructor<RT>) expr;
                java.lang.reflect.Constructor<RT> cc = c.getJavaConstructor();
                while (rs.next()) {
                    try {
                        List<Object> args = new ArrayList<Object>();
                        for (int i = 0; i < c.getArgs().size(); i++) {
                            args.add(get(rs, i + 1, c.getArg(i).getType()));
                        }
                        rv.add(cc.newInstance(args.toArray()));
                    } catch (Exception e) {
                        String error = "Caught " + e.getClass().getName();
                        logger.error(error, e);
                        throw new RuntimeException(e.getMessage(), e);
                    }
                }
            } else {
                while (rs.next()) {
                    rv.add((RT) rs.getObject(1));
                }
            }
            return rv;
        } finally {
            try {
                rs.close();
            } finally {
                stmt.close();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T get(ResultSet rs, int i, Class<T> type) throws Exception {
        String methodName = "get" + type.getSimpleName();
        if (methodName.equals("getInteger")) {
            methodName = "getInt";
        }
        // TODO : cache methods
        return (T) ResultSet.class.getMethod(methodName, int.class).invoke(rs,
                i);
    }

    private void set(PreparedStatement stmt, int i, Object o) throws Exception {
        Class<?> type = o.getClass();
        String methodName = "set" + type.getSimpleName();
        if (methodName.equals("setInteger")) {
            methodName = "setInt";
        }
        type = ClassUtils.wrapperToPrimitive(type) != null ? ClassUtils
                .wrapperToPrimitive(type) : type;
        if (methodName.equals("setDate") && type.equals(java.util.Date.class)) {
            type = java.sql.Date.class;
            o = new java.sql.Date(((java.util.Date) o).getTime());
        }
        // TODO : cache methods
        PreparedStatement.class.getMethod(methodName, int.class, type).invoke(
                stmt, i, o);
    }

    @Override
    public String toString() {
        if (queryString == null) {
            queryString = buildQueryString(false);
        }
        return queryString;
    }
    
    public <RT> UnionBuilder<RT> union(ObjectSubQuery<RT>... sq) {
        return innerUnion(sq);
    }
    
    public <RT> UnionBuilder<RT> union(ListSubQuery<RT>... sq) {
        return innerUnion(sq);
    }

    private <RT> UnionBuilder<RT> innerUnion(SubQuery... sq) {
        if (!getMetadata().getJoins().isEmpty())
            throw new IllegalArgumentException("Don't mix union and from");
        this.sq = sq;
        return new UnionBuilder<RT>();
    }

    protected SQLSerializer createSerializer() {
        return new SQLSerializer(templates);
    }

    protected String buildQueryString(boolean forCountRow) {
        SQLSerializer serializer = createSerializer();
        if (sq != null) {
            serializer.serializeUnion(sq, getMetadata().getOrderBy());
        } else {
            serializer.serialize(getMetadata(), forCountRow);
        }
        constants = serializer.getConstants();
        return serializer.toString();
    }

    private long unsafeCount() throws SQLException {
        // forCountRow = true;
        String queryString = buildQueryString(true);
        logger.debug("query : {}", queryString);
        System.out.println(queryString);
        PreparedStatement stmt = conn.prepareStatement(queryString);
        ResultSet rs = null;
        try {
            int counter = 1;
            for (Object o : constants) {
                try {
                    set(stmt, counter++, o);
                } catch (Exception e) {
                    String error = "Caught " + e.getClass().getName();
                    logger.error(error, e);
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
            rs = stmt.executeQuery();
            rs.next();
            long rv = rs.getLong(1);
            return rv;
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } finally {
                stmt.close();
            }
        }
    }

    public long count() {
        try {
            return unsafeCount();
        } catch (SQLException e) {
            String error = "Caught " + e.getClass().getName();
            logger.error(error, e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public class UnionBuilder<RT> {

        public UnionBuilder<RT> orderBy(OrderSpecifier<?>... o) {
            AbstractSQLQuery.this.orderBy(o);
            return this;
        }

        @SuppressWarnings("unchecked")
        public List<RT> list() throws SQLException {
            if (sq[0].getMetadata().getProjection().size() == 1) {
                return AbstractSQLQuery.this.listSingle(null);
            } else {
                return (List<RT>) AbstractSQLQuery.this.listMultiple();
            }
        }

    }

    public Iterator<Object[]> iterate(Expr<?> e1, Expr<?> e2, Expr<?>... rest) {
        // TODO : optimize
        return list(e1, e2, rest).iterator();
    }

    public <RT> Iterator<RT> iterate(Expr<RT> projection) {
        // TODO : optimize
        return list(projection).iterator();
    }

    public <RT> RT uniqueResult(Expr<RT> expr) {
        List<RT> list = list(expr);
        return !list.isEmpty() ? list.get(0) : null;
    }

    public SubType from(Expr<?>... o) {
        return super.from(o);        
    }

    public SubType fullJoin(Expr<?> o) {
        return super.fullJoin(o);
    }

    public SubType innerJoin(Expr<?> o) {
        return super.innerJoin(o);
    }

    public SubType join(Expr<?> o) {
        return super.join(o);
    }

    public SubType leftJoin(Expr<?> o) {
        return super.leftJoin(o);
    }
}
