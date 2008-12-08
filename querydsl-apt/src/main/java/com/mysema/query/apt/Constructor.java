/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt;

import java.util.ArrayList;
import java.util.Collection;

import com.sun.mirror.declaration.ConstructorDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;

/**
 * ConstructorDecl represents a constructor for a DTO query type.
 * 
 * @author tiwe
 * @version $Id$
 */
public class Constructor {
    private final Collection<Parameter> parameters;

    public Constructor(Collection<Parameter> params) {
        parameters = params;
    }

    public Constructor(ConstructorDeclaration co) {
        parameters = new ArrayList<Parameter>(co.getParameters().size());
        for (ParameterDeclaration pa : co.getParameters()) {
            parameters.add(new Parameter(pa));
        }
    }

    public Collection<Parameter> getParameters() {
        return parameters;
    }

}