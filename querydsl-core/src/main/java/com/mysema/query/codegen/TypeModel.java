/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import javax.annotation.Nullable;

/**
 * @author tiwe
 * 
 */
public interface TypeModel {

    TypeModel as(TypeCategory category);

    String getLocalRawName(TypeModel context);
    
    StringBuilder getLocalRawName(TypeModel context, StringBuilder builder);
    
    String getLocalGenericName(TypeModel context, boolean asArgType);
    
    StringBuilder getLocalGenericName(TypeModel context, StringBuilder builder, boolean asArgType);

    String getFullName();

    String getPackageName();

    @Nullable
    TypeModel getParameter(int i);

    int getParameterCount();

    @Nullable
    String getPrimitiveName();

    TypeModel getSelfOrValueType();

    String getSimpleName();

    TypeCategory getCategory();

    boolean isPrimitive();

    String toString();

    boolean isFinal();
    
    boolean hasEntityFields();
    
}