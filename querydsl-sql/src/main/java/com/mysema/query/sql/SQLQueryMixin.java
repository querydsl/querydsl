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
package com.mysema.query.sql;

import com.mysema.query.JoinType;
import com.mysema.query.QueryMetadata;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.Path;

/**
 * SQLQueryMixin extends QueryMixin with module specific functionality
 * 
 * @author tiwe
 *
 * @param <T>
 */
public class SQLQueryMixin<T> extends QueryMixin<T> {
    
    public SQLQueryMixin() {}

    public SQLQueryMixin(QueryMetadata metadata) {
        super(metadata);
    }

    public SQLQueryMixin(T self, QueryMetadata metadata) {
        super(self, metadata);
    }

    public <P> T join(RelationalFunctionCall<P> target, Path<P> alias) {
        getMetadata().addJoin(JoinType.JOIN, createAlias(target, alias));
        return getSelf();
    }
    
    public <P> T innerJoin(RelationalFunctionCall<P> target, Path<P> alias) {
        getMetadata().addJoin(JoinType.INNERJOIN, createAlias(target, alias));
        return getSelf();
    }

    public <P> T leftJoin(RelationalFunctionCall<P> target, Path<P> alias) {
        getMetadata().addJoin(JoinType.LEFTJOIN, createAlias(target, alias));
        return getSelf();
    }

    public <P> T rightJoin(RelationalFunctionCall<P> target, Path<P> alias) {
        getMetadata().addJoin(JoinType.RIGHTJOIN, createAlias(target, alias));
        return getSelf();
    }
    
    public <P> T fullJoin(RelationalFunctionCall<P> target, Path<P> alias) {
        getMetadata().addJoin(JoinType.FULLJOIN, createAlias(target, alias));
        return getSelf();
    }

    
}
