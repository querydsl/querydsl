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

    String getLocalRawName(BeanModel context);
    
    String getLocalGenericName(BeanModel context);

    String getFullName();

    String getPackageName();

    @Nullable
    TypeModel getParameter(int i);

    int getParameterCount();

    @Nullable
    String getPrimitiveName();

    TypeModel getSelfOrValueType();

    String getSimpleName();

    TypeCategory getTypeCategory();

    boolean isPrimitive();

    String toString();

    boolean isFinal();
}