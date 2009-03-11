/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt.model;

import java.util.Collection;

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

    public Collection<Parameter> getParameters() {
        return parameters;
    }

}