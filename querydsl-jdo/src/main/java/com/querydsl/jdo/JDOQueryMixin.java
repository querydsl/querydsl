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
package com.querydsl.jdo;

import com.querydsl.core.QueryMetadata;
import com.querydsl.core.support.Context;
import com.querydsl.core.support.QueryMixin;
import com.querydsl.core.types.*;

/**
 * {@code JDOQueryMixin} extends {@link QueryMixin} to provide module-specific extensions
 * 
 * @author tiwe
 *
 * @param <T>
 */
public class JDOQueryMixin<T> extends QueryMixin<T> {
    
    public JDOQueryMixin() {}

    public JDOQueryMixin(QueryMetadata metadata) {
        super(metadata);
    }

    public JDOQueryMixin(T self, QueryMetadata metadata) {
        super(self, metadata);
    }
        
    @Override
    protected Predicate convert(Predicate predicate, Role role) {
        predicate = (Predicate)ExpressionUtils.extract(predicate);
        if (predicate != null) {
            Context context = new Context();            
            Predicate transformed = (Predicate) predicate.accept(collectionAnyVisitor, context);
            for (int i = 0; i < context.paths.size(); i++) {
                Path<?> path = context.paths.get(i);            
                addCondition(context, i, path, role);
            }
            return transformed;    
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private void addCondition(Context context, int i, Path<?> path, Role role) {
        EntityPath<?> alias = context.replacements.get(i);                 
        from(alias);
        Predicate condition = ExpressionUtils.predicate(Ops.IN, alias, path.getMetadata().getParent());
        if (role == Role.WHERE) {
            super.where(condition);
        } else {
            super.having(condition);
        }
    }
    
}
