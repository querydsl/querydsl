/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.codegen;

import java.util.Collection;

import net.jcip.annotations.Immutable;

import com.mysema.commons.lang.Assert;

/**
 * Constructor represents a constructor for a DTO query type.
 *
 * @author tiwe
 * @version $Id$
 */
@Immutable
public final class Constructor {

    private final Collection<Parameter> parameters;

    public Constructor(Collection<Parameter> params) {
        parameters = Assert.notNull(params,"params was null");
    }

    @Override
    public boolean equals(Object o){
        if (o == this){
            return true;
        }else if (o instanceof Constructor){
            return ((Constructor)o).parameters.equals(parameters);
        }else{
            return false;
        }
    }

    public Collection<Parameter> getParameters() {
        return parameters;
    }

    @Override
    public int hashCode(){
        return parameters.hashCode();
    }

}
