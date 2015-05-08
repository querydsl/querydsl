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
package com.mysema.query.lucene;

import org.apache.lucene.search.Query;

import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.expr.BooleanOperation;

/**
 * QueryElement wraps a Lucene Query
 *
 * @author tiwe
 *
 */
public class QueryElement extends BooleanOperation {

    private static final long serialVersionUID = 470868107363840155L;

    public QueryElement(Query query) {
        super(LuceneOps.LUCENE_QUERY, ConstantImpl.create(query));
    }

}
