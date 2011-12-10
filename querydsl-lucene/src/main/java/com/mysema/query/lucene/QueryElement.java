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
package com.mysema.query.lucene;

import javax.annotation.Nullable;

import org.apache.lucene.search.Query;

import com.mysema.query.types.Constant;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * QueryElement wraps a Lucene Query
 *
 * @author tiwe
 *
 */
public class QueryElement extends BooleanExpression{

    private static final long serialVersionUID = 470868107363840155L;

    private final Query query;

    @Nullable
    private volatile Constant<String> expr;

    public QueryElement(Query query){
        this.query = query;
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        if (expr == null){
            expr = ConstantImpl.create(query.toString());
        }
        return expr.accept(v, context);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof QueryElement && ((QueryElement)o).query.equals(query);
    }

    @Override
    public int hashCode(){
        return query.hashCode();
    }

    public Query getQuery() {
        return query;
    }

}
