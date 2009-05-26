/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.Projectable;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.support.QueryBaseWithProjection;
import com.mysema.query.types.Order;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.EConstructor;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PEntity;

/**
 * Default implementation of the JDOQLQuery interface
 * 
 * @author tiwe
 * 
 * @param <A>
 */
class JDOQLQueryImpl extends QueryBaseWithProjection<Object, JDOQLQueryImpl> implements Projectable, JDOQLQuery {

    private static final Logger logger = LoggerFactory.getLogger(JDOQLQueryImpl.class);

    private List<Object> constants;

    private List<PEntity<?>> sources = new ArrayList<PEntity<?>>();

    private String filter;

    private final JDOQLOps ops;

    private final PersistenceManager pm;

    public JDOQLQueryImpl(PersistenceManager pm) {
        this(pm, JDOQLOps.DEFAULT);
    }

    public JDOQLQueryImpl(PersistenceManager pm, JDOQLOps ops) {
        this.ops = ops;
        this.pm = pm;
    }

    @Override
    protected JDOQLQueryImpl addToProjection(Expr<?>... o) {
        for (Expr<?> expr : o) {
            if (expr instanceof EConstructor) {
                EConstructor<?> constructor = (EConstructor<?>) expr;
                for (Expr<?> arg : constructor.getArgs()) {
                    super.addToProjection(arg);
                }
            } else {
                super.addToProjection(expr);
            }
        }
        return this;
    }

    @Override
    public JDOQLQueryImpl from(PEntity<?>... o) {
        for (PEntity<?> expr : o) {
            sources.add(expr);
        }
        return this;
    }

    private String buildFilterString(boolean forCountRow) {
        if (getMetadata().getWhere() == null) {
            return null;
        }
        JDOQLSerializer serializer = new JDOQLSerializer(ops, sources.get(0));
        serializer.handle(getMetadata().getWhere());
        constants = serializer.getConstants();
        System.err.println("SERIALIZED : " + serializer.toString());
        return serializer.toString();
    }

    @Override
    protected void clear() {
        super.clear();
        filter = null;
    }

    public long count() {
        String filterString = getFilterString();
        logger.debug("query : {}", filterString);
        Query query = createQuery(filterString, null, true);
        query.setUnique(true);
        query.setResult("count(this)");
        return (Long) execute(query);
    }

    private Query createQuery(String filterString, QueryModifiers modifiers, boolean forCount) {
        Query query = pm.newQuery(sources.get(0).getType());
        if (filterString != null) {
            query.setFilter(filterString);
        }

        // variables
        if (sources.size() > 1) {
            StringBuffer buffer = new StringBuffer();
            for (int i = 1; i < sources.size(); i++) {
                if (buffer.length() > 0) {
                    buffer.append(", ");
                }
                PEntity<?> source = sources.get(i);
                buffer.append(source.getType().getName()).append(" ").append(
                        source.toString());
            }
        }

        // explicit parameters
        if (constants != null) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < constants.size(); i++) {
                if (builder.length() > 0) {
                    builder.append(", ");
                }
                Object val = constants.get(i);
                builder.append(val.getClass().getName()).append(" ").append(
                        "a" + (i + 1));
            }
            query.declareParameters(builder.toString());
        }

        // range (not for count)
        if (modifiers != null && modifiers.isRestricting() && !forCount) {
            long fromIncl = 0;
            long toExcl = Long.MAX_VALUE;
            if (modifiers.getOffset() != null) {
                fromIncl = modifiers.getOffset().longValue();
            }
            if (modifiers.getLimit() != null) {
                toExcl = fromIncl + modifiers.getLimit().longValue();
            }
            query.setRange(fromIncl, toExcl);
        }

        // projection (not for count)
        if (!getMetadata().getProjection().isEmpty() && !forCount) {
            List<? extends Expr<?>> projection = getMetadata().getProjection();
            if (!projection.get(0).equals(sources.get(0))) {
                JDOQLSerializer serializer = new JDOQLSerializer(ops, sources.get(0));
                serializer.handle(", ", projection);
                query.setResult(serializer.toString());
            }
        }

        // order (not for count)
        if (!getMetadata().getOrderBy().isEmpty() && !forCount) {
            List<OrderSpecifier<?>> order = getMetadata().getOrderBy();
            JDOQLSerializer serializer = new JDOQLSerializer(ops, sources.get(0));
            boolean first = true;
            for (OrderSpecifier<?> os : order) {
                if (!first) {
                    serializer.append(", ");
                }
                serializer.handle(os.getTarget());
                serializer.append(os.getOrder() == Order.ASC ? " ascending" : "descending");
                first = false;
            }
            query.setOrdering(serializer.toString());
        }

        return query;
    }

    protected List<Object> getConstants() {
        return constants;
    }

    public Iterator<Object[]> iterate(Expr<?> e1, Expr<?> e2, Expr<?>... rest) {
        // TODO : optimize
        return list(e1, e2, rest).iterator();
    }

    public <RT> Iterator<RT> iterate(Expr<RT> projection) {
        // TODO : optimize
        return list(projection).iterator();
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> list(Expr<?> expr1, Expr<?> expr2, Expr<?>... rest) {
        addToProjection(expr1, expr2);
        addToProjection(rest);
        String filterString = getFilterString();
        return (List<Object[]>) execute(createQuery(filterString, getMetadata().getModifiers(), false));
    }

    private Object execute(Query query) {
        Object rv;
        if (constants != null) {
            rv = query.executeWithArray(constants.toArray());
        } else {
            rv = query.execute();
        }
        // query.closeAll();
        return rv;
    }

    @SuppressWarnings("unchecked")
    public <RT> List<RT> list(Expr<RT> expr) {
        addToProjection(expr);
        String filterString = getFilterString();
        logger.debug("query : {}", filterString);
        return (List<RT>) execute(createQuery(filterString, getMetadata().getModifiers(), false));
    }

    @SuppressWarnings("unchecked")
    public <RT> SearchResults<RT> listResults(Expr<RT> expr) {
        addToProjection(expr);
        Query countQuery = createQuery(getFilterString(), null, true);
        countQuery.setUnique(true);
        countQuery.setResult("count(this)");
        long total = (Long) execute(countQuery);
        if (total > 0) {
            QueryModifiers modifiers = getMetadata().getModifiers();
            String filterString = getFilterString();
            logger.debug("query : {}", filterString);
            Query query = createQuery(filterString, modifiers, false);
            return new SearchResults<RT>((List<RT>) execute(query), modifiers,
                    total);
        } else {
            return SearchResults.emptyResults();
        }
    }

    private String getFilterString() {
        if (filter == null) {
            filter = buildFilterString(false);
        }
        return filter;
    }

    @SuppressWarnings("unchecked")
    public <RT> RT uniqueResult(Expr<RT> expr) {
        addToProjection(expr);
        String filterString = getFilterString();
        logger.debug("query : {}", filterString);
        Query query = createQuery(filterString, QueryModifiers.limit(1), false);
        query.setUnique(true);
        return (RT) execute(query);
    }

}
