/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.IOException;

import javax.annotation.Nullable;

/**
 * TypeModel represents Java types
 * 
 * @author tiwe
 * 
 */
public interface TypeModel {

    /**
     * @param category
     * @return
     */
    TypeModel as(TypeCategory category);

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
    String getLocalGenericName(TypeModel context, boolean asArgType);
    
    /**
     * @param context
     * @param builder
     * @param asArgType
     * @return
     * @throws IOException 
     */
    void getLocalGenericName(TypeModel context, Appendable builder, boolean asArgType) throws IOException;

    /**
     * @param context
     * @return
     */
    String getLocalRawName(TypeModel context);

    /**
     * @param context
     * @param builder
     * @return
     * @throws IOException 
     */
    void getLocalRawName(TypeModel context, Appendable builder) throws IOException;

    /**
     * @return
     */
    String getPackageName();

    /**
     * @param i
     * @return
     */
    @Nullable
    TypeModel getParameter(int i);

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
    TypeModel getSelfOrValueType();
    
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

    /**
     * @return
     */
    TypeModel asArrayType();
    
}