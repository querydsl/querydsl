package com.mysema.query.codegen;

/**
 * @author tiwe
 *
 */
public interface TypeModel {

    TypeModel as(TypeCategory category);

    TypeModel getKeyType();

    String getLocalName();

    String getName();

    String getPackageName();

    String getPrimitiveName();

    String getSimpleName();

    TypeCategory getTypeCategory();

    TypeModel getValueType();

    boolean isPrimitive();

    String toString();

}