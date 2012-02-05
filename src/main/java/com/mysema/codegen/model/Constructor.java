/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.codegen.model;

import java.util.Collection;

/**
 * @author tiwe
 *
 */
public final class Constructor {

    private final Collection<Parameter> parameters;

    public Constructor(Collection<Parameter> params) {
        parameters = params;
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
