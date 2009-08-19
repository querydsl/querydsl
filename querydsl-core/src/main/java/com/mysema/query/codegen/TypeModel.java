/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;


/**
 * 
 * @author tiwe
 *
 */
public interface TypeModel {

    FieldType getFieldType();

    String getName();

    TypeModel getKeyType();
    
    TypeModel getValueType();

    String getPackageName();

    String getSimpleName();

}