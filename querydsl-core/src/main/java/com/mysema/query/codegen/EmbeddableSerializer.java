/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;


public class EmbeddableSerializer extends EntitySerializer{
    
    @Override
    protected void introDefaultInstance(StringBuilder builder, ClassModel model) {
        // no default instance
    }
    
    @Override
    protected void constructorsForVariables(StringBuilder builder, ClassModel model) {
        // no root constructors
    }


}
