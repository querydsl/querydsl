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
    
    private final String name, typeName, realTypeName;

    public ParameterModel(BeanModel context, String name, TypeModel type) {
        this.name = name;
        this.typeName = type.getLocalRawName(context);
        this.realTypeName = type.isPrimitive() ? type.getPrimitiveName() : typeName;
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

    public String getRealTypeName() {
        return realTypeName;
    }

    public int hashCode() {
        return name.hashCode();
    }
}