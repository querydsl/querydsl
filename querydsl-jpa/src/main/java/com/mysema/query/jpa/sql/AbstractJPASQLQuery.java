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
package com.mysema.query.jpa.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.IteratorAdapter;
import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.NonUniqueResultException;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.Tuple;
import com.mysema.query.jpa.AbstractSQLQuery;
import com.mysema.query.jpa.NativeSQLSerializer;
import com.mysema.query.jpa.QueryHandler;
import com.mysema.query.jpa.impl.JPAProvider;
import com.mysema.query.jpa.impl.JPAUtil;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.sql.Union;
import com.mysema.query.sql.UnionImpl;
import com.mysema.query.sql.UnionUtils;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Expression;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.FactoryExpressionUtils;
import com.mysema.query.types.Path;
import com.mysema.query.types.QTuple;
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.query.ListSubQuery;

/**
 * AbstractJPASQLQuery is the base class for JPA Native SQL queries
 * 
 * @author tiwe
 *
 * @param <Q>
 */
public abstract class AbstractJPASQLQuery<Q extends AbstractJPASQLQuery<Q> & com.mysema.query.Query> extends AbstractSQLQuery<Q> {
    
    private static final Logger logger = LoggerFactory.getLogger(AbstractJPASQLQuery.class);
    
    @Nullable
    private Map<Object,String> constants;

    private final EntityManager entityManager;

    protected final SQLTemplates templates;
    
    protected final Multimap<String,Object> hints = HashMultimap.create();
    
    protected final QueryHandler queryHandler;

    @Nullable
    protected Expression<?> union;
    
    private boolean unionAll;
    
    @Nullable
    protected LockModeType lockMode;
    
    @Nullable
    protected FlushModeType flushMode;
    
    @Nullable
    protected FactoryExpression<?> projection;

    public AbstractJPASQLQuery(EntityManager em, SQLTemplates sqlTemplates) {
        this(em, sqlTemplates, new DefaultQueryMetadata());
    }

    public AbstractJPASQLQuery(EntityManager em, SQLTemplates sqlTemplates, QueryMetadata metadata) {
        super(metadata);
        this.entityManager = em;
        this.templates = sqlTemplates;
        this.queryHandler = JPAProvider.getTemplates(em).getQueryHandler();
    }

    private String buildQueryString(boolean forCountRow) {
        NativeSQLSerializer serializer = new NativeSQLSerializer(templates);
        if (union != null) {
            serializer.serializeUnion(union, queryMixin.getMetadata(), unionAll);
        } else {
            if (queryMixin.getMetadata().getJoins().isEmpty()) {
                throw new IllegalArgumentException("No joins given");
            }
            serializer.serialize(queryMixin.getMetadata(), forCountRow);    
        }        
        constants = serializer.getConstantToLabel();
        return serializer.toString();
    }

    public Query createQuery(Expression<?>... args) {
        queryMixin.getMetadata().setValidate(false);
        queryMixin.addProjection(args);
        return createQuery(toQueryString());
    }

    private Query createQuery(String queryString) {
        logQuery(queryString);
        List<? extends Expression<?>> projection = queryMixin.getMetadata().getProjection();
        Query query;
        if (projection.get(0) instanceof EntityPath) {
            if (projection.size() == 1) {
                query = entityManager.createNativeQuery(queryString, projection.get(0).getType());
            } else {
                throw new IllegalArgumentException("Only single element entity projections are supported");
            }

        } else {
            query = entityManager.createNativeQuery(queryString);
        }
        
        if (lockMode != null) {
            query.setLockMode(lockMode);
        }
        if (flushMode != null) {
            query.setFlushMode(flushMode);
        }
        
        for (Map.Entry<String, Object> entry : hints.entries()) {
            query.setHint(entry.getKey(), entry.getValue());
        }
        

        // set constants
        JPAUtil.setConstants(query, constants, queryMixin.getMetadata().getParams());
        
        FactoryExpression<?> wrapped = projection.size() > 1 ? FactoryExpressionUtils.wrap(projection) : null;        
        if ((projection.size() == 1 && projection.get(0) instanceof FactoryExpression) || wrapped != null) {
            
            // TODO : add conversion logic like in AbstractJPAQuery
            
            this.projection = (FactoryExpression)projection.get(0);
            if (wrapped != null) {
                this.projection = wrapped;
                getMetadata().clearProjection();
                getMetadata().addProjection(wrapped);
            }
        }
        
        return query;
    }

    @Override
    public List<Tuple> list(Expression<?>... args) {
        return list(new QTuple(args));
    }
    
    /**
     * Transforms results using FactoryExpression if ResultTransformer can't be used
     * 
     * @param query
     * @return
     */
    private List<?> getResultList(Query query) {
        // TODO : use lazy list here?
        if (projection != null) {
            List<?> results = query.getResultList();
            List<Object> rv = new ArrayList<Object>(results.size());
            for (Object o : results) {
                if (o != null) {
                    if (!o.getClass().isArray()) {
                        o = new Object[]{o};
                    }   
                    rv.add(projection.newInstance((Object[])o));
                } else {
                    rv.add(null);
                }                
            }
            return rv;
        } else {
            return query.getResultList();
        }
    }
    
    /**
     * Transforms results using FactoryExpression if ResultTransformer can't be used
     * 
     * @param query
     * @return
     */
    @Nullable
    private Object getSingleResult(Query query) {
        if (projection != null) {
            Object result = query.getSingleResult();
            if (result != null) {
                if (!result.getClass().isArray()) {
                    result = new Object[]{result};
                }
                return projection.newInstance((Object[])result);    
            } else {
                return null;
            }            
        } else {
            return query.getSingleResult();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT> List<RT> list(Expression<RT> projection) {
        Query query = createQuery(projection);
        return (List<RT>) getResultList(query);
    }

    @Override
    public CloseableIterator<Tuple> iterate(Expression<?>... args) {
        return iterate(new QTuple(args));
    }

    @Override
    public <RT> CloseableIterator<RT> iterate(Expression<RT> expr) {
        Query query = createQuery(expr);
        return queryHandler.<RT>iterate(query, null);
    }

    @Override
    public SearchResults<Tuple> listResults(Expression<?>... args) {
        return listResults(new QTuple(args));
    }
    
    @Override
    public <RT> SearchResults<RT> listResults(Expression<RT> projection) {
        // TODO : handle entity projections as well
        queryMixin.addProjection(projection);
        Query query = createQuery(toCountRowsString());
        long total = ((Number)query.getSingleResult()).longValue();
        if (total > 0) {
            QueryModifiers modifiers = queryMixin.getMetadata().getModifiers();
            String queryString = toQueryString();
            query = createQuery(queryString);
            @SuppressWarnings("unchecked")
            List<RT> list = (List<RT>) getResultList(query);
            reset();
            return new SearchResults<RT>(list, modifiers, total);
        } else {
            reset();
            return SearchResults.emptyResults();
        }
    }

    protected void logQuery(String queryString) {
        if (logger.isDebugEnabled()) {
            logger.debug(queryString.replace('\n', ' '));
        }
    }

    protected void reset() {
        queryMixin.getMetadata().reset();
//        entityPaths = null;
        constants = null;
    }

    protected String toCountRowsString() {
        return buildQueryString(true);
    }

    protected String toQueryString() {
        return buildQueryString(false);
    }
    
    public <RT> Union<RT> union(ListSubQuery<RT>... sq) {
        return innerUnion(sq);
    }

    public <RT> Union<RT> union(SubQueryExpression<RT>... sq) {
        return innerUnion(sq);
    }
    
    public <RT> Union<RT> unionAll(ListSubQuery<RT>... sq) {
        unionAll = true;
        return innerUnion(sq);
    }

    public <RT> Union<RT> unionAll(SubQueryExpression<RT>... sq) {
        unionAll = true;
        return innerUnion(sq);
    }
    
    public <RT> Q union(Path<?> alias, ListSubQuery<RT>... sq) {
        return from(UnionUtils.union(sq, alias, false));
    }
    
    public <RT> Q union(Path<?> alias, SubQueryExpression<RT>... sq) {
        return from(UnionUtils.union(sq, alias, false));
    }
        
    public <RT> Q unionAll(Path<?> alias, ListSubQuery<RT>... sq) {
        return from(UnionUtils.union(sq, alias, true));
    }
    
    public <RT> Q unionAll(Path<?> alias, SubQueryExpression<RT>... sq) {
        return from(UnionUtils.union(sq, alias, true));
    }    
    
    private <RT> Union<RT> innerUnion(SubQueryExpression<?>... sq) {
        queryMixin.getMetadata().setValidate(false);
        if (!queryMixin.getMetadata().getJoins().isEmpty()) {
            throw new IllegalArgumentException("Don't mix union and from");
        }
        this.union = UnionUtils.union(sq, unionAll);
        return new UnionImpl<Q, RT>((Q)this, sq[0].getMetadata().getProjection());
    }

    @Override
    public Tuple uniqueResult(Expression<?>... args) {
        return uniqueResult(new QTuple(args));
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <RT> RT uniqueResult(Expression<RT> expr) {
        Query query = createQuery(expr);
        return (RT)uniqueResult(query);
    }
    
    @Nullable
    private Object uniqueResult(Query query) {        
        try{
            return getSingleResult(query);
        } catch(javax.persistence.NoResultException e) {
            logger.trace(e.getMessage(),e);
            return null;
        } catch(javax.persistence.NonUniqueResultException e) {
            throw new NonUniqueResultException();
        } finally {
            reset();
        }
    }

    @SuppressWarnings("unchecked")
    public Q setLockMode(LockModeType lockMode) {
        this.lockMode = lockMode;
        return (Q)this;
    }
    
    @SuppressWarnings("unchecked")
    public Q setFlushMode(FlushModeType flushMode) {
        this.flushMode = flushMode;
        return (Q)this;
    }

    @SuppressWarnings("unchecked")
    public Q setHint(String name, Object value) {
        hints.put(name, value);
        return (Q)this;
    }
    
}
