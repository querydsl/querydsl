/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.util.Collection;

/**
 * ConstructorModel represents a constructor for a DTO query type.
 * 
 * @author tiwe
 * @version $Id$
 */
public class ConstructorModel {
    private final Collection<ParameterModel> parameters;

    public ConstructorModel(Collection<ParameterModel> params) {
        parameters = params;
    }

    public Collection<ParameterModel> getParameters() {
        return parameters;
    }

}