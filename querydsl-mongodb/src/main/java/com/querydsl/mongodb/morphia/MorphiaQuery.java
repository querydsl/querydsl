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
package com.querydsl.mongodb.morphia;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.cache.DefaultEntityCache;
import org.mongodb.morphia.mapping.cache.EntityCache;

import com.google.common.base.Function;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.querydsl.core.types.EntityPath;
import com.querydsl.mongodb.AbstractMongodbQuery;

/**
 * {@code MorphiaQuery} extends {@link AbstractMongodbQuery} with Morphia specific transformations
 *
 * <p>Example</p>
 *
 * <pre>{@code
 * QUser user = QUser.user;
 * MorphiaQuery<User> query = new MorphiaQuery<User>(morphia, datastore, user);
 * List<User> list = query
 *     .where(user.firstName.eq("Bob"))
 *     .fetch();
 * }</pre>
 *
 * @param <K> result type
 *
 * @author laimw
 * @author tiwe
 *
 */
public class MorphiaQuery<K> extends AbstractMongodbQuery<K, MorphiaQuery<K>> {

    private final EntityCache cache;

    private final Datastore datastore;

    public MorphiaQuery(Morphia morphia, Datastore datastore, EntityPath<K> entityPath) {
        this(morphia, datastore, new DefaultEntityCache(), entityPath);
    }

    public MorphiaQuery(Morphia morphia, Datastore datastore, Class<? extends K> entityType) {
        this(morphia, datastore, new DefaultEntityCache(), entityType);
    }

    public MorphiaQuery(Morphia morphia, Datastore datastore,
            EntityCache cache, EntityPath<K> entityPath) {
        this(morphia, datastore, cache, entityPath.getType());
    }

    public MorphiaQuery(final Morphia morphia, final Datastore datastore,
                        final EntityCache cache, final Class<? extends K> entityType) {
        super(datastore.getCollection(entityType), new Function<DBObject, K>() {
            @Override
            public K apply(DBObject dbObject) {
                return morphia.fromDBObject(entityType, dbObject, cache);
            }
        }, new MorphiaSerializer(morphia));
        this.datastore = datastore;
        this.cache = cache;
    }


    @Override
    protected DBCursor createCursor() {
        cache.flush();
        return super.createCursor();
    }

    @Override
    protected DBCollection getCollection(Class<?> type) {
        return datastore.getCollection(type);
    }

}