/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt;

import com.mysema.query.apt.util.TypeInfo;
import com.sun.mirror.declaration.ParameterDeclaration;

/**
 * ParameterDecl provides
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

    public Parameter(ParameterDeclaration pa) {
        name = pa.getSimpleName();
        typeName = new TypeInfo(pa.getType()).getFullName();
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