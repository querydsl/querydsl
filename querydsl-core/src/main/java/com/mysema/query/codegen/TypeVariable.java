/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

/**
 * @author tiwe
 *
 */
public class TypeVariable {
    
    private final String name;
    
    private final Type upperBound;
    
    private final Type lowerBound;
    
    public TypeVariable(String name, Type lower, Type upper) {
        this.name = name;
        this.lowerBound = lower;
        this.upperBound = upper;
    }

    public String getName() {
        return name;
    }

    public Type getUpperBound() {
        return upperBound;
    }

    public Type getLowerBound() {
        return lowerBound;
    }
    
    

}
