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
package com.mysema.query.jpa;

import com.mysema.query.support.CollectionAnyVisitor;
import com.mysema.query.support.Context;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.PredicateOperation;

/**
 * JPQLCollectionAnyVisitor extends the {@link CollectionAnyVisitor} class with module specific
 * extensions
 * 
 * @author tiwe
 *
 */
public final class JPQLCollectionAnyVisitor extends CollectionAnyVisitor {
    
    public static final JPQLCollectionAnyVisitor DEFAULT = new JPQLCollectionAnyVisitor();
    
    @Override
    protected Predicate exists(Context c, Predicate condition) {
        JPQLSubQuery query = new JPQLSubQuery();
        for (int i = 0; i < c.paths.size(); i++) {
            query.from(c.replacements.get(i));
            query.where(new PredicateOperation(Ops.IN, 
                    c.replacements.get(i), c.paths.get(i).getMetadata().getParent()));    
        }        
        c.clear();
        query.where(condition);
        return query.exists();
    }
    
    private JPQLCollectionAnyVisitor() {}

}
