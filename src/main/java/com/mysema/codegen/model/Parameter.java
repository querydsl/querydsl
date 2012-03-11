/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.codegen.model;

/**
 * Parameter represents a parameter in a Constructor
 * 
 * @author tiwe
 * @version $Id$
 */
public final class Parameter {

    private final String name;

    private final Type type;

    public Parameter(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof Parameter) {
            return type.equals(((Parameter) o).type);
        } else {
            return false;
        }
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    @Override
    public String toString() {
        return type + " " + name;
    }
}
