/*
 * Copyright 2013, Mysema Ltd
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

import com.mysema.query.QueryFlag;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.Expression;
import com.mysema.query.types.OperationImpl;

/**
 * @author tiwe
 *
 * @param <R>
 */
public class WithBuilder<R> {

    private final QueryMixin<R> queryMixin;

    private final Expression<?> alias;

    public WithBuilder(QueryMixin<R> queryMixin, Expression<?> alias) {
        this.queryMixin = queryMixin;
        this.alias = alias;
    }

    public R as(Expression<?> expr) {
        Expression<?> flag = OperationImpl.create(alias.getType(), SQLTemplates.WITH_ALIAS, alias, expr);
        return queryMixin.addFlag(new QueryFlag(QueryFlag.Position.WITH, flag));
    }

}
