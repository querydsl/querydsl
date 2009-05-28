/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

/**
 * ParameterModel represents a parameter in a Constructor
 * 
 * @author tiwe
 * @version $Id$
 */
public class ParameterModel implements Comparable<ParameterModel> {
    private final String name, typeName;

    public ParameterModel(String name, String typeName) {
        this.name = name;
        this.typeName = typeName;
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