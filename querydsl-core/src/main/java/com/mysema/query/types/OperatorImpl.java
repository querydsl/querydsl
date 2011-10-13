/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import static java.util.Collections.unmodifiableList;

import java.util.Arrays;
import java.util.List;

import net.jcip.annotations.Immutable;

import com.mysema.commons.lang.Assert;

/**
 * OperatorImpl is the default implementation of the {@link Operator}  interface
 */
@Immutable
public class OperatorImpl<T> implements Operator<T> {

    private static final long serialVersionUID = -2435035383548549877L;

    private final String id;
    
    private final List<Class<?>> types;

    public OperatorImpl(String id, Class<?>... types) {
        this(id, Arrays.<Class<?>> asList(types));
    }

    public OperatorImpl(String id, List<Class<?>> types) {
        this.id = Assert.notNull(id,"id");
        this.types = unmodifiableList(types);
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
