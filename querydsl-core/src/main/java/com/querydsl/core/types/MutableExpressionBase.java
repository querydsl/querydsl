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
package com.querydsl.core.types;


/**
 * MutableExpressionBase is the base class for mutable Expression implementations
 * 
 * @author tiwe
 *
 * @param <T>
 */
public abstract class MutableExpressionBase<T> implements Expression<T> {

    private static final long serialVersionUID = -6830426684911919114L;

    private final Class<? extends T> type;

    public MutableExpressionBase(Class<? extends T> type) {
        this.type = type;
    }
    
    public final Class<? extends T> getType() {
        return type;
    }
    
    public final int hashCode() {
        return accept(HashCodeVisitor.DEFAULT, null);
    }
    
    @Override
    public final String toString() {
        return accept(ToStringVisitor.DEFAULT, Templates.DEFAULT);
    }

}
