/*
 * Copyright 2013, Mysema Ltd
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
package com.querydsl.jpa;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;

import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.types.FactoryExpression;

/**
 * @author tiwe
 *
 * @param <T>
 */
public class TransformingIterator<T> implements CloseableIterator<T> {

    private final Iterator<T> iterator;
    
    private final Closeable closeable;
    
    private final FactoryExpression<?> projection;
    
    public TransformingIterator(Iterator<T> iterator, FactoryExpression<?> projection) {
        this.iterator = iterator;
        this.projection = projection;
        this.closeable = iterator instanceof Closeable ? (Closeable)iterator : null;
    }
    
    public TransformingIterator(Iterator<T> iterator, Closeable closeable, FactoryExpression<?> projection) {
        this.iterator = iterator;
        this.projection = projection;
        this.closeable = closeable;
    }
    
    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        Object result = iterator.next();
        if (result != null) {
            if (!result.getClass().isArray()) {
                result = new Object[]{result};
            }
            return (T)projection.newInstance((Object[])result);    
        } else {
            return null;
        }  
    }

    @Override
    public void remove() {
        iterator.remove();
    }

    @Override
    public void close() {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }        
    }

    
}
