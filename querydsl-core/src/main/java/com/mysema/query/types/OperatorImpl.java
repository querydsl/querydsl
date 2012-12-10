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

import com.google.common.collect.ImmutableList;

/**
 * OperatorImpl is the default implementation of the {@link Operator} interface
 */
public class OperatorImpl<T> implements Operator<T> {

    private static final long serialVersionUID = -2435035383548549877L;

    private final String id;
    
    private final List<Class<?>> types;

    public OperatorImpl(String id, Class<?>... types) {
        this(id, ImmutableList.copyOf(types));
    }

    public OperatorImpl(String id, List<Class<?>> types) {
        this.id = id;
        this.types = ImmutableList.copyOf(types);
    }

    @Override
    public String getId(){
        return id;
    }
    
    @Override
    public List<Class<?>> getTypes() {
        return types;
    }
    
    @Override
    public boolean equals(Object o){
        if (o == this) {
            return true;
        } else if (o instanceof Operator<?>) {
            return ((Operator<?>)o).getId().equals(id);
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode(){
        return id.hashCode();
    }
    
    @Override
    public String toString(){
        return id;
    }
}
