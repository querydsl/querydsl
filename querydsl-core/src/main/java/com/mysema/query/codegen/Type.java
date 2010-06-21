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

    /**
     * @param context
     * @param builder
     * @param asArgType
     * @return
     * @throws IOException
     */
    void appendLocalGenericName(Type context, Appendable builder, boolean asArgType) throws IOException;

    /**
     * @param context
     * @param builder
     * @return
     * @throws IOException
     */
    void appendLocalRawName(Type context, Appendable builder) throws IOException;

    /**
     * @param category
     * @return
     */
    Type as(TypeCategory category);

    /**
     * @return
     */
    Type asArrayType();

    /**
     * @return
     */
    TypeCategory getCategory();

    /**
     * @return
     */
    String getFullName();

    /**
     * @param context
     * @param asArgType
     * @return
     */
    String getLocalGenericName(Type context, boolean asArgType);

    /**
     * @param context
     * @return
     */
    String getLocalRawName(Type context);

    /**
     * @return
     */
    String getPackageName();

    /**
     * @param i
     * @return
     */
    @Nullable
    Type getParameter(int i);

    /**
     * @return
     */
    int getParameterCount();

    /**
     * @return
     */
    @Nullable
    String getPrimitiveName();

    /**
     * @return
     */
    Type getSelfOrValueType();

    /**
     * @return
     */
    String getSimpleName();

    /**
     * @return
     */
    boolean hasEntityFields();

    /**
     * @return
     */
    boolean isFinal();

    /**
     * @return
     */
    boolean isPrimitive();

    /**
     * @return
     */
    String toString();

}
