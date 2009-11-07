/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import net.jcip.annotations.Immutable;

/**
 * ParameterModel represents a parameter in a Constructor
 * 
 * @author tiwe
 * @version $Id$
 */
@Immutable
public final class ParameterModel implements Comparable<ParameterModel> {
    
    private final String name;
    
    private final TypeModel type;

    public ParameterModel(BeanModel context, String name, TypeModel type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public int compareTo(ParameterModel o) {
        return name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ParameterModel && name.equals(((ParameterModel) o).name);
    }

    public String getName() {
        return name;
    }

    public TypeModel getType(){
        return type;
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
    
}