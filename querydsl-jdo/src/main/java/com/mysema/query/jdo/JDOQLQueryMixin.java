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
package com.mysema.query.jdo;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.QueryMetadata;
import com.mysema.query.support.CollectionAnyVisitor;
import com.mysema.query.support.Context;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.CollectionExpression;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;

/**
 * JDOQLQueryMixin extends {@link QueryMixin} to provide module specific extensions 
 * 
 * @author tiwe
 *
 * @param <T>
 */
public class JDOQLQueryMixin<T> extends QueryMixin<T> {
    
    public JDOQLQueryMixin() {}

    public JDOQLQueryMixin(QueryMetadata metadata) {
        super(metadata);
    }

    public JDOQLQueryMixin(T self, QueryMetadata metadata) {
        super(self, metadata);
    }
        
    @Override
    protected Predicate normalize(Predicate predicate, boolean where) {
        if (predicate instanceof BooleanBuilder && ((BooleanBuilder)predicate).getValue() == null) {
            return predicate;
        } else {
            Context context = new Context();            
            Predicate transformed = (Predicate) predicate.accept(CollectionAnyVisitor.DEFAULT, context);
            for (int i = 0; i < context.paths.size(); i++) {
                Path<?> path = context.paths.get(i);            
                addCondition(context, i, path, where);
            }
            return transformed;    
        }        
    }

    @SuppressWarnings("unchecked")
    private void addCondition(Context context, int i, Path<?> path, boolean where) {
        EntityPath<?> alias = context.replacements.get(i);                 
        from(alias);
        Predicate condition = ExpressionUtils.in(alias, (CollectionExpression)path.getMetadata().getParent());
        if (where) {
            super.where(condition);
        } else {
            super.having(condition);
        }
    }
    
}
