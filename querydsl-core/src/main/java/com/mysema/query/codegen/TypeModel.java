package com.mysema.query.codegen;

import javax.annotation.Nullable;

/**
 * @author tiwe
 *
 */
public interface TypeModel {

    TypeModel as(TypeCategory category);

    @Nullable
    TypeModel getParameter(int i);

    int getParameterCount();
    
    String getLocalName();

    String getName();

    String getPackageName();

    @Nullable
    String getPrimitiveName();

    String getSimpleName();

    TypeCategory getTypeCategory();

    boolean isPrimitive();

    String toString();

}