/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.IOException;

/**
 * AbstractTypeModel is an abstract base class for TypeModel implementations
 * 
 * @author tiwe
 *
 */
public abstract class AbstractTypeModel implements TypeModel{
    
    @Override
    public String getLocalGenericName(TypeModel context, boolean asArgType){
        try {
            StringBuilder builder = new StringBuilder();
            getLocalGenericName(context, builder, asArgType);
            return builder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    @Override
    public String getLocalRawName(TypeModel context){
        try {
            StringBuilder builder = new StringBuilder();
            getLocalRawName(context, builder);
            return builder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
