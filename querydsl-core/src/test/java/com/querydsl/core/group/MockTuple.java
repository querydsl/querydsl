package com.querydsl.core.group;

import java.util.Arrays;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;

public class MockTuple implements Tuple{

    private final Object[] a;
    
    public MockTuple(Object[] a) {
        this.a = a;
    }
    
    @Override
    public <T> T get(int index, Class<T> type) {
        return (T) a[index];
    }

    @Override
    public <T> T get(Expression<T> expr) {
        return null;
    }
    
    @Override
    public int size() {
        return a.length;
    }

    @Override
    public Object[] toArray() {
        return a;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof Tuple) {
            return Arrays.equals(a, ((Tuple) obj).toArray());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(a);
    }
    
    @Override
    public String toString() {
        return Arrays.toString(a);
    }
    
}
