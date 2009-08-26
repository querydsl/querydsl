/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import net.jcip.annotations.Immutable;

import com.mysema.commons.lang.Assert;

/**
 * ParameterModel represents a parameter in a Constructor
 * 
 * @author tiwe
 * @version $Id$
 */
@Immutable
public final class ParameterModel implements Comparable<ParameterModel> {
    
    private final String name, typeName;

    public ParameterModel(String name, String typeName) {
        this.name = Assert.notNull(name,"name was null");
        this.typeName = Assert.notNull(typeName,"typeName was null");
    }

    public int compareTo(ParameterModel o) {
        return name.compareTo(o.name);
    }

    public boolean equals(Object o) {
        return o instanceof ParameterModel && name.equals(((ParameterModel) o).name);
    }

    public String getName() {
        return name;
    }

    public String getTypeName() {
        return typeName;
    }

    public int hashCode() {
        return name.hashCode();
    }
}