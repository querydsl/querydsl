package com.mysema.query.codegen;

import javax.annotation.Nullable;

/**
 * @author tiwe
 *
 */
public interface TypeModel {

    TypeModel as(TypeCategory category);

    @Nullable
    TypeModel getKeyType();

    String getLocalName();

    String getName();

    String getPackageName();

    @Nullable
    String getPrimitiveName();

    String getSimpleName();

    TypeCategory getTypeCategory();

    @Nullable
    TypeModel getValueType();

    boolean isPrimitive();

    String toString();

}