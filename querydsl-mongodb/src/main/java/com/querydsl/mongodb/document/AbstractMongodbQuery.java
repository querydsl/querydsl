/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.mongodb.document;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.ReadPreference;
import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.JoinExpression;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.SimpleQuery;
import com.querydsl.core.support.QueryMixin;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.Operation;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.ParamExpression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.CollectionPathBase;
import org.jetbrains.annotations.Nullable;

/**
 * {@code AbstractMongodbQuery} provides a base class for general Querydsl query implementation.
 *
 * @author Mark Paluch
 *
 * @param <Q> concrete subtype
 */
public abstract class AbstractMongodbQuery<Q extends AbstractMongodbQuery<Q>> implements SimpleQuery<Q> {

    @SuppressWarnings("serial")
    static class NoResults extends RuntimeException { }

    private final MongodbDocumentSerializer serializer;

    private final QueryMixin<Q> queryMixin;

    private ReadPreference readPreference;

    /**
     * Create a new MongodbQuery instance
     *
     * @param serializer serializer
     */
    @SuppressWarnings("unchecked")
    public AbstractMongodbQuery(MongodbDocumentSerializer serializer) {
        @SuppressWarnings("unchecked") // Q is this plus subclass
        Q query = (Q) this;
        this.queryMixin = new QueryMixin<Q>(query, new DefaultQueryMetadata(), false);
        this.serializer = serializer;
    }

    /**
     * Define a join
     *
     * @param ref reference
     * @param target join target
     * @return join builder
     */
    public <T> JoinBuilder<Q, T> join(Path<T> ref, Path<T> target) {
        return new JoinBuilder<Q, T>(queryMixin, ref, target);
    }

    /**
     * Define a join
     *
     * @param ref reference
     * @param target join target
     * @return join builder
     */
    public <T> JoinBuilder<Q, T> join(CollectionPathBase<?,T,?> ref, Path<T> target) {
        return new JoinBuilder<Q, T>(queryMixin, ref, target);
    }

    /**
     * Define a constraint for an embedded object
     *
     * @param collection collection
     * @param target target
     * @return builder
     */
    public <T> AnyEmbeddedBuilder<Q> anyEmbedded(Path<? extends Collection<T>> collection, Path<T> target) {
        return new AnyEmbeddedBuilder<Q>(queryMixin, collection);
    }

    @Nullable
    protected Predicate createFilter(QueryMetadata metadata) {
        Predicate filter;
        if (!metadata.getJoins().isEmpty()) {
            filter = ExpressionUtils.allOf(metadata.getWhere(), createJoinFilter(metadata));
        } else {
            filter = metadata.getWhere();
        }
        return filter;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    protected Predicate createJoinFilter(QueryMetadata metadata) {
        Map<Expression<?>, Predicate> predicates = new HashMap<>();
        List<JoinExpression> joins = metadata.getJoins();
        for (int i = joins.size() - 1; i >= 0; i--) {
            JoinExpression join = joins.get(i);
            Path<?> source = (Path) ((Operation<?>) join.getTarget()).getArg(0);
            Path<?> target = (Path) ((Operation<?>) join.getTarget()).getArg(1);

            final Predicate extraFilters = predicates.get(target.getRoot());
            Predicate filter = ExpressionUtils.allOf(join.getCondition(), extraFilters);
            List<? extends Object> ids = getIds(target.getType(), filter);
            if (ids.isEmpty()) {
                throw new NoResults();
            }
            Path<?> path = ExpressionUtils.path(String.class, source, "$id");
            predicates.merge(source.getRoot(),  ExpressionUtils.in((Path<Object>) path, ids), ExpressionUtils::and);
        }
        Path<?> source = (Path) ((Operation) joins.get(0).getTarget()).getArg(0);
        return predicates.get(source.getRoot());
    }

    private Predicate allOf(Collection<Predicate> predicates) {
        return predicates != null ? ExpressionUtils.allOf(predicates) : null;
    }

    protected abstract List<Object> getIds(Class<?> targetType, Predicate condition);

    @Override
    public Q distinct() {
        return queryMixin.distinct();
    }

    public Q where(Predicate e) {
        return queryMixin.where(e);
    }

    @Override
    public Q where(Predicate... e) {
        return queryMixin.where(e);
    }

    @Override
    public Q limit(long limit) {
        return queryMixin.limit(limit);
    }

    @Override
    public Q offset(long offset) {
        return queryMixin.offset(offset);
    }

    @Override
    public Q restrict(QueryModifiers modifiers) {
        return queryMixin.restrict(modifiers);
    }

    public Q orderBy(OrderSpecifier<?> o) {
        return queryMixin.orderBy(o);
    }

    @Override
    public Q orderBy(OrderSpecifier<?>... o) {
        return queryMixin.orderBy(o);
    }

    @Override
    public <T> Q set(ParamExpression<T> param, T value) {
        return queryMixin.set(param, value);
    }

    protected Document createProjection(Expression<?> projection) {
        if (projection instanceof FactoryExpression) {
            Document obj = new Document();
            for (Object expr : ((FactoryExpression) projection).getArgs()) {
                if (expr instanceof Expression) {
                    obj.put((String) serializer.handle((Expression) expr), 1);
                }
            }
            return obj;
        }
        return null;
    }


    protected Document createQuery(@Nullable Predicate predicate) {
        if (predicate != null) {
            return (Document) serializer.handle(predicate);
        } else {
            return new Document();
        }
    }


    /**
     * Sets the read preference for this query
     *
     * @param readPreference read preference
     */
    public void setReadPreference(ReadPreference readPreference) {
        this.readPreference = readPreference;
    }

    protected QueryMixin<Q> getQueryMixin() {
        return queryMixin;
    }

    protected MongodbDocumentSerializer getSerializer() {
        return serializer;
    }

    protected ReadPreference getReadPreference() {
        return readPreference;
    }

    /**
     * Get the where definition as a Document instance
     *
     * @return
     */
    public Document asDocument() {
        return createQuery(queryMixin.getMetadata().getWhere());
    }

    @Override
    public String toString() {
        return asDocument().toString();
    }
}
