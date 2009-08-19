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

    TypeModel getKeyType();

    String getLocalName();
    
    String getName();

    String getPackageName();

    String getSimpleName();
    
    TypeModel getValueType();

}