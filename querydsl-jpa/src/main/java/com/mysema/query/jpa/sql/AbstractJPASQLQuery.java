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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.IteratorAdapter;
import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.NonUniqueResultException;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.jpa.AbstractSQLQuery;
import com.mysema.query.jpa.NativeSQLSerializer;
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
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.query.ListSubQuery;

/**
 * @author tiwe
 *
 * @param <Q>
 */
//TODO : add support for constructor projections
public abstract class AbstractJPASQLQuery<Q extends AbstractJPASQLQuery<Q> & com.mysema.query.Query> extends AbstractSQLQuery<Q> {
    
    private static final Logger logger = LoggerFactory.getLogger(AbstractJPASQLQuery.class);
    
    @Nullable
    private Map<Object,String> constants;

    private final EntityManager entityManager;

    protected final SQLTemplates templates;
    
    protected final Map<String,Object> hints = new HashMap<String,Object>();

    @Nullable
    protected SubQueryExpression<?>[] union;
    
    private boolean unionAll;
    
    @Nullable
    protected LockModeType lockMode;
    
    @Nullable
    protected FlushModeType flushMode;
    
    protected boolean factoryExpressionUsed = false;

    public AbstractJPASQLQuery(EntityManager entityManager, SQLTemplates sqlTemplates) {
        this(entityManager, sqlTemplates, new DefaultQueryMetadata());
    }

    public AbstractJPASQLQuery(EntityManager entityManager, SQLTemplates sqlTemplates, QueryMetadata metadata) {
        super(metadata);
        this.entityManager = entityManager;
        this.templates = sqlTemplates;
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
        queryMixin.addToProjection(args);
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
        
        for (Map.Entry<String, Object> entry : hints.entrySet()) {
            query.setHint(entry.getKey(), entry.getValue());
        }
        

        // set constants
        JPAUtil.setConstants(query, constants, queryMixin.getMetadata().getParams());
        
        FactoryExpression<?> wrapped = projection.size() > 1 ? FactoryExpressionUtils.wrap(projection) : null;        
        if ((projection.size() == 1 && projection.get(0) instanceof FactoryExpression) || wrapped != null) {
            
            // TODO : add conversion logic like in AbstractJPAQuery
            
            factoryExpressionUsed = true;
            if (wrapped != null) {
                getMetadata().clearProjection();
                getMetadata().addProjection(wrapped);
            }
        }
        
        return query;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> list(Expression<?>[] args) {
        Query query = createQuery(args);
        //reset();
        return (List<Object[]>) getResultList(query);
    }
    
    /**
     * Transforms results using FactoryExpression if ResultTransformer can't be used
     * 
     * @param query
     * @return
     */
    private List<?> getResultList(Query query) {
        // TODO : use lazy list here?
        if (factoryExpressionUsed) {
            List<?> results = query.getResultList();
            List<Object> rv = new ArrayList<Object>(results.size());
            FactoryExpression<?> expr = (FactoryExpression<?>)getMetadata().getProjection().get(0);
            for (Object o : results) {
                if (o != null) {
                    if (!o.getClass().isArray()) {
                        o = new Object[]{o};
                    }   
                    rv.add(expr.newInstance((Object[])o));
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
        if (factoryExpressionUsed) {
            Object result = query.getSingleResult();
            FactoryExpression<?> expr = (FactoryExpression<?>)getMetadata().getProjection().get(0);
            if (result != null) {
                if (!result.getClass().isArray()) {
                    result = new Object[]{result};
                }
                return expr.newInstance((Object[])result);    
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
    public CloseableIterator<Object[]> iterate(Expression<?>[] args) {
        return new IteratorAdapter<Object[]>(list(args).iterator());
    }

    @Override
    public <RT> CloseableIterator<RT> iterate(Expression<RT> projection) {
        return new IteratorAdapter<RT>(list(projection).iterator());
    }

    @Override
    public <RT> SearchResults<RT> listResults(Expression<RT> projection) {
        // TODO : handle entity projections as well
        queryMixin.addToProjection(projection);
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
        return from(UnionUtils.combineUnion(sq, alias, templates, false));
    }
    
    public <RT> Q union(Path<?> alias, SubQueryExpression<RT>... sq) {
        return from(UnionUtils.combineUnion(sq, alias, templates, false));
    }
        
    public <RT> Q unionAll(Path<?> alias, ListSubQuery<RT>... sq) {
        return from(UnionUtils.combineUnion(sq, alias, templates, true));
    }
    
    public <RT> Q unionAll(Path<?> alias, SubQueryExpression<RT>... sq) {
        return from(UnionUtils.combineUnion(sq, alias, templates, true));
    }    
    
    private <RT> Union<RT> innerUnion(SubQueryExpression<?>... sq) {
        queryMixin.getMetadata().setValidate(false);
        if (!queryMixin.getMetadata().getJoins().isEmpty()) {
            throw new IllegalArgumentException("Don't mix union and from");
        }
        this.union = sq;
        return new UnionImpl<Q, RT>((Q)this, union[0].getMetadata().getProjection());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <RT> RT uniqueResult(Expression<RT> expr) {
        Query query = createQuery(expr);
        return (RT)uniqueResult(query);
    }
    
    @Override
    public Object[] uniqueResult(Expression<?>[] args) {
        Query query = createQuery(args);
        Object obj = uniqueResult(query);
        if (obj != null) {
            return obj.getClass().isArray() ? (Object[])obj : new Object[]{obj};    
        } else {
            return null;
        }        
    }

    @Nullable
    private Object uniqueResult(Query query) {        
        try{
            return getSingleResult(query);
        } catch(javax.persistence.NoResultException e) {
            logger.debug(e.getMessage(),e);
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
