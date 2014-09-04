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
package com.mysema.query;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.ejb.HibernateQuery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.runner.RunWith;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mysema.query.jpa.NativeSQLSerializer;
import com.mysema.query.jpa.QueryHandler;
import com.mysema.query.jpa.domain.Cat;
import com.mysema.query.jpa.domain.Color;
import com.mysema.query.jpa.domain.QCat;
import com.mysema.query.jpa.domain.QMultipleLongProjection;
import com.mysema.query.jpa.domain.sql.SAnimal;
import com.mysema.query.jpa.impl.JPAUtil;
import com.mysema.query.jpa.sql.AbstractJPASQLQuery;
import com.mysema.query.jpa.sql.JPASQLQuery;
import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.types.Expression;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.FactoryExpressionUtils;
import com.mysema.testutil.ExcludeIn;
import com.mysema.testutil.JPATestRunner;

@RunWith(JPATestRunner.class)
public class JPASQLBase extends AbstractSQLTest {

    @Rule
    public static MethodRule targetRule = new TargetRule();

    @Rule
    public static MethodRule hibernateOnly = new JPAProviderRule();

    private final SQLTemplates templates = Mode.getSQLTemplates();

    private EntityManager entityManager;

    @Override
    protected JPASQLQuery query() {
        return new JPASQLQuery(entityManager, templates);
    }


    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Before
    public void setUp() {
        if (query().from(cat).notExists()) {
            entityManager.persist(new Cat("Beck", 1, Color.BLACK));
            entityManager.persist(new Cat("Kate", 2, Color.BLACK));
            entityManager.persist(new Cat("Kitty", 3, Color.BLACK));
            entityManager.persist(new Cat("Bobby", 4, Color.BLACK));
            entityManager.persist(new Cat("Harold", 5, Color.BLACK));
            entityManager.persist(new Cat("Tim", 6, Color.BLACK));
            entityManager.flush();
        }
    }

    @Test
    public void EntityQueries_CreateQuery() {
        SAnimal cat = new SAnimal("cat");
        QCat catEntity = QCat.cat;

        Query query = query().from(cat).createQuery(catEntity);
        assertEquals(6, query.getResultList().size());
    }

    @Test
    @ExcludeIn(Target.MYSQL)
    public void EntityQueries_CreateQuery2() {
        SAnimal cat = new SAnimal("CAT");
        QCat catEntity = QCat.cat;

        Query query = query().from(cat).createQuery(catEntity);
        assertEquals(6, query.getResultList().size());
    }

    @Test
    public void Projections_DuplicateColumns() {
        SAnimal cat = new SAnimal("cat");
        Query query = query().from(cat).createQuery(new QMultipleLongProjection(cat.count(), cat.count()));
        assertEquals(1, query.getResultList().size());
    }

    @Test
    @NoBatooJPA @NoEclipseLink @NoOpenJPA
    public void Projections_DuplicateColumns_Fix() {
        SAnimal cat = new SAnimal("cat");
        FixedJPASQLQuery fixedQuery = new FixedJPASQLQuery(entityManager, templates);
        Query query = fixedQuery.from(cat).createQuery(new QMultipleLongProjection(cat.count(), cat.count()));
        assertEquals(1, query.getResultList().size());
    }

    public class FixedJPASQLQuery extends AbstractJPASQLQuery<FixedJPASQLQuery> {

        private final EntityManager entityManager;

        public FixedJPASQLQuery(EntityManager entityManager, SQLTemplates sqlTemplates) {
            super(entityManager, new Configuration(sqlTemplates));
            this.entityManager = entityManager;
        }

        public FixedJPASQLQuery(EntityManager entityManager, Configuration conf, QueryHandler queryHandler, QueryMetadata metadata) {
            super(entityManager, conf, queryHandler, metadata);
            this.entityManager = entityManager;
        }

        @Override
        public FixedJPASQLQuery clone(EntityManager entityManager) {
            FixedJPASQLQuery q = new FixedJPASQLQuery(entityManager, configuration, queryHandler, getMetadata().clone());
            q.clone(this);
            return q;
        }

        @Override
        public Query createQuery(Expression<?>... args) {
            queryMixin.getMetadata().setValidate(false);
            queryMixin.addProjection(args);
            return fixedCreateQuery(false);
        }

        private Query fixedCreateQuery(boolean forCount) {
            NativeSQLSerializer serializer = (NativeSQLSerializer) serialize(forCount);
            String queryString = serializer.toString();
            logQuery(queryString);
            List<? extends Expression<?>> projection = queryMixin.getMetadata().getProjection();
            Query query;

            Expression<?> proj = projection.get(0);
            if (!FactoryExpression.class.isAssignableFrom(proj.getClass()) && isEntityExpression(proj)) {
                if (projection.size() == 1) {
                    if (queryHandler.createNativeQueryTyped()) {
                        query = entityManager.createNativeQuery(queryString, proj.getType());
                    } else {
                        query = entityManager.createNativeQuery(queryString);
                    }
                } else {
                    throw new IllegalArgumentException("Only single element entity projections are supported");
                }

            } else {
                query = entityManager.createNativeQuery(queryString);
            }
            if (!forCount) {
                Map<Expression<?>, String> aliases = serializer.getAliases();
                if (proj instanceof FactoryExpression) {
                    Set<Expression<?>> handledExpressions = Sets.newHashSet(); // FIX
                    for (Expression<?> expr : ((FactoryExpression<?>)proj).getArgs()) {
                        if (!handledExpressions.contains(expr)) { // FIX
                            if (isEntityExpression(expr)) {
                                handledExpressions.add(expr); // FIX
                                queryHandler.addEntity(query, extractEntityExpression(expr).toString(), expr.getType());
                            } else if (aliases.containsKey(expr)) {
                                handledExpressions.add(expr); // FIX
                                queryHandler.addScalar(query, aliases.get(expr), expr.getType());
                            }
                        } // FIX
                    }
                } else if (isEntityExpression(proj)) {
                    queryHandler.addEntity(query, extractEntityExpression(proj).toString(), proj.getType());
                } else if (aliases.containsKey(proj)) {
                    queryHandler.addScalar(query, aliases.get(proj), proj.getType());
                }
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
            JPAUtil.setConstants(query, serializer.getConstantToLabel(), queryMixin.getMetadata().getParams());

            FactoryExpression<?> wrapped = projection.size() > 1 ? FactoryExpressionUtils.wrap(projection) : null;
            if ((projection.size() == 1 && projection.get(0) instanceof FactoryExpression) || wrapped != null) {
                Expression<?> expr = wrapped != null ? wrapped : projection.get(0);

                // ---------------------------------------------- FIX
                // (copied and adapted from HibernateHandler#transform)
                if (query instanceof HibernateQuery) {
                    final FactoryExpression<?> factoryExpr = (FactoryExpression<?>)expr;
                    org.hibernate.transform.ResultTransformer transformer = new FixedFactoryResultTransformer(factoryExpr, serializer.getAliases());
                    ((HibernateQuery)query).getHibernateQuery().setResultTransformer(transformer);
                } else {
                    this.projection = (FactoryExpression<?>)projection.get(0);
                    if (wrapped != null) {
                        this.projection = wrapped;
                        getMetadata().clearProjection();
                        getMetadata().addProjection(wrapped);
                    }
                }
                // ---------------------------------------------- END OF FIX
            }

            return query;
        }

    // ---------------------------------------------- FIX
        /*
         * A FactoryResultTransformer handling duplication of result columns for expressions that are used multiple times.
         */
        private class FixedFactoryResultTransformer implements org.hibernate.transform.ResultTransformer {
            private static final long serialVersionUID = -3625957233853100239L;

            private final transient FactoryExpression<?> projection;

            private final transient Map<Expression<?>, String> expressionToAlias;

            public FixedFactoryResultTransformer(FactoryExpression<?> projection, Map<Expression<?>, String> aliases) {
                super();
                this.projection = projection;
                this.expressionToAlias = aliases;
            }

            @SuppressWarnings("rawtypes")
            @Override
            public List transformList(List collection) {
                return collection;
            }

            @Override
            public Object transformTuple(Object[] tuple, String[] aliases) {
                if (projection.getArgs().size() < tuple.length) {
                    Object[] shortened = new Object[projection.getArgs().size()];
                    System.arraycopy(tuple, 0, shortened, 0, shortened.length);
                    tuple = shortened;
                }
                Map<String, Object> aliasToValue = Maps.newHashMap();
                for (int i = 0 ; i < tuple.length ; ++i) {
                    aliasToValue.put(aliases[i], tuple[i]);
                }
                List<Object> actualTuple = Lists.newArrayList();
                for (Expression<?> expression : projection.getArgs()) {
                    String alias = expressionToAlias.get(expression);
                    actualTuple.add(aliasToValue.get(alias));
                }
                return projection.newInstance(Iterables.toArray(actualTuple, Object.class));
            }

        }
    // ---------------------------------------------- END OF FIX

    }
}
