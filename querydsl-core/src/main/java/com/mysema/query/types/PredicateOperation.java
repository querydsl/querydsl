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
package com.mysema.query.types;

import java.util.List;

import javax.annotation.Nullable;

/**
 * PredicateOperation provides a Boolean typed Operation implementation 
 * 
 * @author tiwe
 *
 */
public class PredicateOperation extends OperationImpl<Boolean> implements Predicate{
    
    private static final long serialVersionUID = -5371430939203772072L;

    @Nullable
    private volatile Predicate not;
    
    public PredicateOperation(Operator<Boolean> operator, Expression<?>... args){
        super(Boolean.class, operator, args);
    }

    public PredicateOperation(Operator<Boolean> operator, List<Expression<?>> args){
        super(Boolean.class, operator, args);
    }
    
    @Override
    public Predicate not() {
        if (not == null) {
            not = new PredicateOperation(Ops.NOT, this);
        }
        return not;
    }

}
