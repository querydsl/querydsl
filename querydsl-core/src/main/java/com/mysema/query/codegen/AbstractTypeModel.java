/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

/**
 * @author tiwe
 *
 */
public abstract class AbstractTypeModel implements TypeModel{
    
    @Override
    public String getLocalRawName(TypeModel context){
        return getLocalRawName(context, new StringBuilder()).toString();
    }
    
    @Override
    public String getLocalGenericName(TypeModel context, boolean asArgType){
        return getLocalGenericName(context, new StringBuilder(), asArgType).toString();
    }

}
