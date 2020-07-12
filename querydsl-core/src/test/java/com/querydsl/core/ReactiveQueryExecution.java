/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.core;

import com.querydsl.core.support.QueryBase;
import com.querydsl.core.types.CollectionExpression;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.MapExpression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.*;
import org.junit.Assert;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The Class StandardTest.
 *
 * @author tiwe
 */
public abstract class ReactiveQueryExecution {

    private final List<String> errors = new ArrayList<String>();

    private final List<String> failures = new ArrayList<String>();

    private final MatchingFiltersFactory matchers;

    private final ProjectionsFactory projections;

    private final FilterFactory filters;

    private boolean runFilters = true;

    private boolean runProjections = true;

    private boolean counts = true;

    private int total;

    public ReactiveQueryExecution(Module module, Target target) {
        projections = new ProjectionsFactory(module, target);
        filters = new FilterFactory(projections, module, target);
        matchers = new MatchingFiltersFactory(module, target);
    }

    public ReactiveQueryExecution(ProjectionsFactory p, FilterFactory f, MatchingFiltersFactory m) {
        projections = p;
        filters = f;
        matchers = m;
    }

    public void reset() {
        errors.clear();
        failures.clear();
        total = 0;
    }

    private void runProjectionQueries(Collection<? extends Expression<?>> projections) {
        if (this.runProjections) {
            for (Expression<?> pr : projections) {
                total++;
                try {
                    // projection
                    runProjection(pr);

                    // projection distinct
                    runProjectionDistinct(pr);
                } catch (Throwable t) {
                    t.printStackTrace();
                    t = addError(pr, t);
                }
            }
        }

    }

    private Throwable addError(Expression<?> expr, Throwable throwable) {
        StringBuilder error = new StringBuilder();
        error.append(expr + " failed : \n");
        error.append(" " + throwable.getClass().getName() + " : " + throwable.getMessage() + "\n");
        if (throwable.getCause() != null) {
            throwable = throwable.getCause();
            error.append(" " + throwable.getClass().getName() + " : " + throwable.getMessage() + "\n");
        }
        errors.add(error.toString());
        return throwable;
    }

    private void runFilterQueries(Collection<Predicate> filters, boolean matching) {
        if (this.runFilters) {
            for (Predicate f : filters) {
                total++;
                try {
                    // filter
                    int results = runFilter(f);

                    // filter distinct
                    runFilterDistinct(f);

                    if (counts) {
                        // count
                        runCount(f);

                        // count distinct
                        runCountDistinct(f);
                    }

                    if (matching && results == 0) {
                        failures.add(f + " failed");
                    }

                } catch (Throwable t) {
                    t.printStackTrace();
                    t = addError(f, t);
                }
            }
        }
    }

    protected abstract ReactiveFetchable<?> createQuery();

    protected abstract ReactiveFetchable<?> createQuery(Predicate filter);

    private long runCount(Predicate f) {
        ReactiveFetchable<?> p = createQuery(f);
        return p.fetchCount().block();
    }

    private long runCountDistinct(Predicate f) {
        ReactiveFetchable<?> p = createQuery(f);
        ((QueryBase) p).distinct();
        return p.fetchCount().block();
    }

    private int runFilter(Predicate f) {
        ReactiveFetchable<?> p = createQuery(f);

        return p.fetch().collectList().block().size();
    }

    private int runFilterDistinct(Predicate f) {
        ReactiveFetchable<?> p = createQuery(f);
        ((QueryBase) p).distinct();
        return p.fetch().collectList().block().size();
    }

    private int runProjection(Expression<?> pr) {
        ReactiveFetchable<?> p = createQuery();

        ((ReactiveFetchableQuery) p).select(pr);
        return p.fetch().collectList().block().size();
    }

    private int runProjectionDistinct(Expression<?> pr) {
        ReactiveFetchable<?> p = createQuery();

        ((QueryBase) p).distinct();
        ((ReactiveFetchableQuery) p).select(pr);
        return p.fetch().collectList().block().size();
    }

    public final void report() {
        if (!failures.isEmpty() || !errors.isEmpty()) {
            // System.err logging
            System.err.println(failures.size() + " failures");
            for (String f : failures) {
                System.err.println(f);
            }
            System.err.println();
            System.err.println(errors.size() + " errors");
            for (String e : errors) {
                System.err.println(e);
            }

            // construct String for Assert.fail()
            StringBuffer buffer = new StringBuffer("Failed with ");
            if (!failures.isEmpty()) {
                buffer.append(failures.size()).append(" failure(s) ");
                if (!errors.isEmpty()) {
                    buffer.append("and ");
                }
            }
            if (!errors.isEmpty()) {
                buffer.append(errors.size()).append(" error(s) ");
            }
            buffer.append("of ").append(total).append(" tests\n");
            for (String f : failures) {
                buffer.append(f + "\n");
            }
            for (String e : errors) {
                buffer.append(e + "\n");
            }
            Assert.fail(buffer.toString());
        } else {
            System.out.println("Success with " + total + " tests");
        }
    }

    public final ReactiveQueryExecution noFilters() {
        runFilters = false;
        return this;
    }

    public final ReactiveQueryExecution noProjections() {
        runProjections = false;
        return this;
    }

    public final ReactiveQueryExecution noCounts() {
        counts = false;
        return this;
    }

    public final void runBooleanTests(BooleanExpression expr, BooleanExpression other) {
        runFilterQueries(filters.booleanFilters(expr, other), false);
    }

    public final <A> void runCollectionTests(CollectionExpressionBase<?, A> expr, CollectionExpression<?, A> other, A knownElement, A missingElement) {
        runFilterQueries(matchers.collection(expr, other, knownElement, missingElement), true);
        runFilterQueries(filters.collection(expr, other, knownElement), false);
        runProjectionQueries(projections.collection(expr, other, knownElement));
    }

    public final void runDateTests(DateExpression<Date> expr, DateExpression<java.sql.Date> other, java.sql.Date knownValue) {
        runFilterQueries(matchers.date(expr, other, knownValue), true);
        runFilterQueries(filters.date(expr, other, knownValue), false);
        runProjectionQueries(projections.date(expr, other, knownValue));
    }

    public final void runDateTimeTests(DateTimeExpression<java.util.Date> expr, DateTimeExpression<java.util.Date> other, java.util.Date knownValue) {
        runFilterQueries(matchers.dateTime(expr, other, knownValue), true);
        runFilterQueries(filters.dateTime(expr, other, knownValue), false);
        runProjectionQueries(projections.dateTime(expr, other, knownValue));
    }

    public final <A, Q extends SimpleExpression<A>> void runListTests(ListPath<A, Q> expr, ListExpression<A, Q> other, A knownElement, A missingElement) {
        runFilterQueries(matchers.list(expr, other, knownElement, missingElement), true);
        runFilterQueries(filters.list(expr, other, knownElement), false);
        runProjectionQueries(projections.list(expr, other, knownElement));
    }

    public final <K, V> void runMapTests(MapExpressionBase<K, V, ?> expr, MapExpression<K, V> other, K knownKey, V knownValue, K missingKey, V missingValue) {
        runFilterQueries(matchers.map(expr, other, knownKey, knownValue, missingKey, missingValue), true);
        runFilterQueries(filters.map(expr, other, knownKey, knownValue), false);
        runProjectionQueries(projections.map(expr, other, knownKey, knownValue));
    }

    public final <A extends Number & Comparable<A>> void runNumericCasts(NumberExpression<A> expr, NumberExpression<A> other, A knownValue) {
        runProjectionQueries(projections.numericCasts(expr, other, knownValue));
    }

    public final <A extends Number & Comparable<A>> void runNumericTests(NumberExpression<A> expr, NumberExpression<A> other, A knownValue) {
        runFilterQueries(matchers.numeric(expr, other, knownValue), true);
        runFilterQueries(filters.numeric(expr, other, knownValue), false);
        runProjectionQueries(projections.numeric(expr, other, knownValue, false));
    }

    public final void runStringTests(StringExpression expr, StringExpression other, String knownValue) {
        runFilterQueries(matchers.string(expr, other, knownValue), true);
        runFilterQueries(filters.string(expr, other, knownValue), false);
        runProjectionQueries(projections.string(expr, other, knownValue));
    }

    public final void runTimeTests(TimeExpression<java.sql.Time> expr, TimeExpression<java.sql.Time> other, java.sql.Time knownValue) {
        runFilterQueries(matchers.time(expr, other, knownValue), true);
        runFilterQueries(filters.time(expr, other, knownValue), false);
        runProjectionQueries(projections.time(expr, other, knownValue));
    }
}
