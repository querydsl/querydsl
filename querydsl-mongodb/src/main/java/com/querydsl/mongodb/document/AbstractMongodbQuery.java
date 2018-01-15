/*
 * Copyright 2018, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.mongodb.document;

import com.mongodb.ReadPreference;
import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.SimpleQuery;
import com.querydsl.core.support.QueryMixin;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.CollectionPathBase;
import org.bson.Document;

import javax.annotation.Nullable;
import java.util.Collection;

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

    QueryMixin<Q> getQueryMixin() {
        return queryMixin;
    }

    MongodbDocumentSerializer getSerializer() {
        return serializer;
    }

    ReadPreference getReadPreference() {
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
