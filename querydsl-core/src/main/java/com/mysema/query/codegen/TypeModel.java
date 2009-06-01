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

    String getKeyTypeName();

    String getPackageName();

    String getSimpleName();

    String getValueTypeName();

}