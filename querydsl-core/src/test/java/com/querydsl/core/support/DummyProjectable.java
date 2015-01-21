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
package com.querydsl.core.support;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.EmptyCloseableIterator;
import com.querydsl.core.SearchResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;

public class DummyProjectable extends ProjectableQuery<DummyProjectable>{

    public DummyProjectable(QueryMixin<DummyProjectable> queryMixin) {
        super(queryMixin);
    }

    public DummyProjectable() {
        super(new QueryMixin<DummyProjectable>());
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public CloseableIterator<Tuple> iterate(Expression<?>... args) {
        return new EmptyCloseableIterator<Tuple>();
    }

    @Override
    public <RT> CloseableIterator<RT> iterate(Expression<RT> projection) {
        return new EmptyCloseableIterator<RT>();
    }

    @Override
    public SearchResults<Tuple> listResults(Expression<?>... args) {
        return SearchResults.emptyResults();
    }
    
    @Override
    public <RT> SearchResults<RT> listResults(Expression<RT> projection) {
        return SearchResults.emptyResults();
    }

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public Tuple uniqueResult(Expression<?>... args) {
        if (queryMixin.getMetadata().getModifiers().getLimit() == null) {
            limit(2);
        }
        return null;
    }

    @Override
    public <RT> RT uniqueResult(Expression<RT> projection) {
        if (queryMixin.getMetadata().getModifiers().getLimit() == null) {
            limit(2);
        }
        return null;
    }

}
