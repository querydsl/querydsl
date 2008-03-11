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
 * 
 * ConstructorDecl provides
 * 
 * @author tiwe
 * @version $Id$
 * 
 */
public class ConstructorDecl {
    private final Collection<ParameterDecl> parameters;

    public ConstructorDecl(Collection<ParameterDecl> params) {
        parameters = params;
    }

    public ConstructorDecl(ConstructorDeclaration co) {
        parameters = new ArrayList<ParameterDecl>(co.getParameters().size());
        for (ParameterDeclaration pa : co.getParameters()) {
            parameters.add(new ParameterDecl(pa));
        }
    }

    public Collection<ParameterDecl> getParameters() {
        return parameters;
    }

}