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
package com.mysema.query.jdo;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;
import com.mysema.query.support.DetachableQuery;
import com.mysema.query.types.CollectionExpression;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Expression;

/**
 * Abstract superclass for SubQuery implementations
 *
 * @author tiwe
 *
 * @param <Q>
 */
public class AbstractJDOSubQuery<Q extends AbstractJDOSubQuery<Q>> extends DetachableQuery<Q> implements JDOCommonQuery<Q> {

    public AbstractJDOSubQuery() {
        this(new DefaultQueryMetadata().noValidate());
    }

    @SuppressWarnings("unchecked")
    public AbstractJDOSubQuery(QueryMetadata metadata) {
        super(new JDOQueryMixin<Q>(metadata));
        this.queryMixin.setSelf((Q)this);
    }

    public Q from(EntityPath<?> arg) {
        return queryMixin.from(arg);
    }
    
    public Q from(EntityPath<?>... args) {
        return queryMixin.from(args);
    }

    public <P> Q from(CollectionExpression<?,P> target, EntityPath<P> alias) {
        return queryMixin.join(target, alias);
    }

    @Override
    public String toString() {
        if (!queryMixin.getMetadata().getJoins().isEmpty()) {
            Expression<?> source = queryMixin.getMetadata().getJoins().get(0).getTarget();
            JDOQLSerializer serializer = new JDOQLSerializer(JDOQLTemplates.DEFAULT, source);
            serializer.setStrict(false);
            serializer.serialize(queryMixin.getMetadata(), false, false);
            return serializer.toString().trim();
        } else {
            return super.toString();
        }
    }

}
