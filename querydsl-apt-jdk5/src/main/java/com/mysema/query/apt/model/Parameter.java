/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt.model;

/**
 * Parameter represents a parameter in a Constructor
 * 
 * @author tiwe
 * @version $Id$
 */
public class Parameter implements Comparable<Parameter> {
    private final String name, typeName;

    public Parameter(String name, String typeName) {
        this.name = name;
        this.typeName = typeName;
    }

    public int compareTo(Parameter o) {
        return name.compareTo(o.name);
    }

    public boolean equals(Object o) {
        return o instanceof Parameter && name.equals(((Parameter) o).name);
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