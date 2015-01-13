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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.EmptyCloseableIterator;
import com.querydsl.core.Projectable;
import com.querydsl.core.ResultTransformer;
import com.querydsl.core.SearchResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;

/**
 * @author tiwe
 *
 */
public class AbstractProjectable implements Projectable {

    @Override
    public long count() {
        return 0;
    }

    @Override
    public boolean exists() {
        return 0 < count();
    }

    @Override
    public boolean notExists() {
        return !exists();
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
    public List<Tuple> list(Expression<?>... args) {
        return Collections.emptyList();
    }

    @Override
    public <RT> List<RT> list(Expression<RT> projection) {
        return Collections.emptyList();
    }

    @Override
    public SearchResults<Tuple> listResults(Expression<?>... args) {
        return null;
    }
    
    @Override
    public <RT> SearchResults<RT> listResults(Expression<RT> projection) {
        return new SearchResults<RT>(Collections.<RT>emptyList(), null, null, 0l);
    }

    @Override
    public <K, V> Map<K, V> map(Expression<K> key, Expression<V> value) {
        return Collections.emptyMap();
    }

    @Override
    public Tuple singleResult(Expression<?>... args) {
        return null;
    }

    @Override
    public <RT> RT singleResult(Expression<RT> projection) {
        return null;
    }    

    @Override
    public <T> T transform(ResultTransformer<T> transformer) {
        return transformer.transform(this);
    }

    @Override
    public Tuple uniqueResult(Expression<?>... args) {
        return null;
    }

    @Override
    public <RT> RT uniqueResult(Expression<RT> projection) {
        return null;
    }



}
