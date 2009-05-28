/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.util.Collection;

/**
 * Constructor represents a constructor for a DTO query type.
 * 
 * @author tiwe
 * @version $Id$
 */
public class ConstructorModel {
    private final Collection<Parameter> parameters;

    public ConstructorModel(Collection<Parameter> params) {
        parameters = params;
    }

    public Collection<Parameter> getParameters() {
        return parameters;
    }

}