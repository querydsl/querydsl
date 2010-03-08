package com.mysema.util;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;

import com.mysema.commons.lang.CloseableIterator;

/**
 * @author tiwe
 *
 * @param <T>
 */
public class ResultIterator<T> implements CloseableIterator<T>{
    
    private final Closeable closeable;
    
    private final Iterator<T> iterator;
    
    public ResultIterator(Iterator<T> iterator, Closeable closeable){
        this.iterator = iterator;
        this.closeable = closeable;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        return iterator.next();
    }

    @Override
    public void remove() {
        iterator.remove();
    }

    @Override
    public void close() throws IOException {
        closeable.close();
    }

}
