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
package com.querydsl.core.types;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableList;

/**
 * PredicateOperation provides a Boolean typed Operation implementation 
 * 
 * @author tiwe
 *
 */
@Immutable
public final class PredicateOperation extends OperationImpl<Boolean> implements Predicate{
    
    private static final long serialVersionUID = -5371430939203772072L;

    @Nullable
    private volatile Predicate not;
    
    public static PredicateOperation create(Operator<Boolean> operator, Expression<?> one) {
        return new PredicateOperation(operator, ImmutableList.<Expression<?>>of(one));
    }
    
    public static PredicateOperation create(Operator<Boolean> operator, Expression<?> one, Expression<?> two) {
        return new PredicateOperation(operator, ImmutableList.of(one, two));
    }
    
    public PredicateOperation(Operator<Boolean> operator, ImmutableList<Expression<?>> args) {
        super(Boolean.class, operator, args);
    }
    
    @Override
    public Predicate not() {
        if (not == null) {
            not = PredicateOperation.create(Ops.NOT, this);
        }
        return not;
    }

}
