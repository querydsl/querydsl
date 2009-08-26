/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.util.Collection;

import net.jcip.annotations.Immutable;

import com.mysema.commons.lang.Assert;

/**
 * ConstructorModel represents a constructor for a DTO query type.
 * 
 * @author tiwe
 * @version $Id$
 */
@Immutable
public final class ConstructorModel {
    
    private final Collection<ParameterModel> parameters;

    public ConstructorModel(Collection<ParameterModel> params) {
        parameters = Assert.notNull(params,"params was null");
    }

    public Collection<ParameterModel> getParameters() {
        return parameters;
    }

}