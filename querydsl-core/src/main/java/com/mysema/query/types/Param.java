/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.expr.ESimple;


/**
 * Param defines a named parameter in a query
 * 
 * @author tiwe
 *
 * @param <T>
 */
public class Param<T> extends ESimple<T> {

    private static final long serialVersionUID = -6872502615009012503L;
    
    private String name;
    
    public Param(Class<? extends T> type, String name) {
        super(type);
        this.name = Assert.notNull(name, "name");
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this){
            return true;
        }else if (o instanceof Param<?>){
            Param<?> other = (Param<?>)o;
            return other.getType().equals(getType()) && other.getName().equals(name);
        }else{
            return false;
        }
    }
    
    @Override
    public int hashCode(){
        return name.hashCode();
    }

    public String getName() {
        return name;
    }
    
}
