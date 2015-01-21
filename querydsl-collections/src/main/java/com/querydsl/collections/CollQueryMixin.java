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
package com.querydsl.collections;

import com.querydsl.core.QueryMetadata;
import com.querydsl.core.support.CollectionAnyVisitor;
import com.querydsl.core.support.Context;
import com.querydsl.core.support.QueryMixin;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.template.BooleanTemplate;

/**
 * CollQueryMixin extends {@link QueryMixin} to provide normalization logic specific to this module
 *
 * @author tiwe
 *
 * @param <T>
 */
public class CollQueryMixin<T> extends QueryMixin<T> {

    private static final Predicate ANY = BooleanTemplate.create("any");

    public CollQueryMixin() {}

    public CollQueryMixin(QueryMetadata metadata) {
        super(metadata);
    }

    public CollQueryMixin(T self, QueryMetadata metadata) {
        super(self, metadata);
    }

    @Override
    protected Predicate normalize(Predicate predicate, boolean where) {
        predicate = (Predicate)ExpressionUtils.extract(predicate);
        if (predicate != null) {
            Context context = new Context();
            Predicate transformed = (Predicate) predicate.accept(CollectionAnyVisitor.DEFAULT, context);
            for (int i = 0; i < context.paths.size(); i++) {
                leftJoin(
                    (Path)context.paths.get(i).getMetadata().getParent(),
                    (Path)context.replacements.get(i));
                on(ANY);
            }
            return transformed;
        } else {
            return predicate;
        }
    }
}
