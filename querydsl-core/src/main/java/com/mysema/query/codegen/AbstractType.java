/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.IOException;

/**
 * AbstractType is an abstract base class for Type implementations
 * 
 * @author tiwe
 *
 */
public abstract class AbstractType implements Type{
    
    @Override
    public String getLocalGenericName(Type context, boolean asArgType){
        try {
            StringBuilder builder = new StringBuilder();
            appendLocalGenericName(context, builder, asArgType);
            return builder.toString();
        } catch (IOException e) {
            throw new CodeGenerationException(e.getMessage(), e);
        }
    }
    
    @Override
    public String getLocalRawName(Type context){
        try {
            StringBuilder builder = new StringBuilder();
            appendLocalRawName(context, builder);
            return builder.toString();
        } catch (IOException e) {
            throw new CodeGenerationException(e.getMessage(), e);
        }
    }

}
