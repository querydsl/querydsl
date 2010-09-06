/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.expr.ESimple;

/**
 * Param defines a parameter in a query with an optional name
 *
 * @author tiwe
 *
 * @param <T>
 */
public class Param<T> extends ESimple<T> {

    private static final long serialVersionUID = -6872502615009012503L;

    private static volatile long counter = 0;

    private final String name;

    private final boolean anon;

    public Param(Class<? extends T> type, String name) {
        super(type);
        this.name = Assert.notNull(name, "name");
        this.anon = false;
    }

    public Param(Class<? extends T> type) {
        super(type);
        this.name = "param" + (++counter);
        this.anon = true;
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this){
            return true;
        }else if (o instanceof Param<?>){
            Param<?> other = (Param<?>)o;
            return other.getType().equals(getType())
                && other.getName().equals(name)
                && other.anon == anon;
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

    public boolean isAnon(){
        return anon;
    }

    public String getNotSetMessage() {
        if (!anon){
            return "The parameter " + name + " needs to be set";
        }else{
            return "A parameter of type " + getType().getName() + " was not set";
        }
    }
}
