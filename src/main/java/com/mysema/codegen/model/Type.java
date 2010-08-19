/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.codegen.model;

import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

/**
 * @author tiwe
 */
public interface Type {
    
    Type as(TypeCategory category);
    
    Type asArrayType();
    
    @Nullable
    Type getComponentType();
    
    TypeCategory getCategory();
    
    String getFullName();
    
    String getGenericName(boolean asArgType);

    String getGenericName(boolean asArgType, Set<String> packages, Set<String> classes);
    
    String getPackageName();

    List<Type> getParameters();
    
    @Nullable
    String getPrimitiveName();
    
    String getRawName(Set<String> packages, Set<String> classes);
    
    String getSimpleName();

    boolean isFinal();
    
    boolean isPrimitive();
    
}