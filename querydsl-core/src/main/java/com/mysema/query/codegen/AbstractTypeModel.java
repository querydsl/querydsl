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
            return getLocalGenericName(context, new StringBuilder(), asArgType).toString();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    @Override
    public String getLocalRawName(TypeModel context){
        try {
            return getLocalRawName(context, new StringBuilder()).toString();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
