/*
 * Copyright 2012, Mysema Ltd
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
package com.mysema.query.types.expr;

import com.mysema.query.types.ExpressionBase;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathImpl;

/**
 * DslExpression is the base class for DSL expressions, but SimpleExpression is the base class
 * for scalar Expressions 
 * 
 * @author tiwe
 *
 */
public abstract class DslExpression<T> extends ExpressionBase<T>{

    private static final long serialVersionUID = -3383063447710753290L;
    
    public DslExpression(Class<? extends T> type) {
        super(type);
    }

    /**
     * Create an alias for the expression
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public DslExpression<T> as(Path<T> alias) {
        return DslOperation.create((Class<T>)getType(),Ops.ALIAS, this, alias);
    }

    /**
     * Create an alias for the expression
     *
     * @return
     */
    public DslExpression<T> as(String alias) {
        return as(new PathImpl<T>(getType(), alias));
    }
    
}
