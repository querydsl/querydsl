/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.codegen;

import java.io.IOException;

import javax.annotation.Nullable;

/**
 * Type represents Java types
 *
 * @author tiwe
 *
 */
public interface Type {

    void appendLocalGenericName(Type context, Appendable builder, boolean asArgType) throws IOException;

    void appendLocalRawName(Type context, Appendable builder) throws IOException;

    Type as(TypeCategory category);

    Type asArrayType();

    TypeCategory getCategory();

    String getFullName();

    String getLocalGenericName(Type context, boolean asArgType);

    String getLocalRawName(Type context);

    String getPackageName();

    @Nullable
    Type getParameter(int i);

    int getParameterCount();

    @Nullable
    String getPrimitiveName();

    Type getSelfOrValueType();

    String getSimpleName();

    boolean hasEntityFields();

    boolean isFinal();

    boolean isPrimitive();

    String toString();

}
