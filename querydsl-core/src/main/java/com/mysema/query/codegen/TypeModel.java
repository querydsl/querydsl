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

    /**
     * @return field type of type
     */
    FieldType getFieldType();

    /**
     * @return key type of Map type
     */
    TypeModel getKeyType();

    /**
     * @return name including optional enclosing class' simple name and 
     */
    String getLocalName();
    
    /**
     * @return fully qualified class name
     */
    String getName();

    /**
     * @return package name
     */
    String getPackageName();

    /**
     * @return simple class name
     */
    String getSimpleName();
    
    /**
     * @return value type of Map and Collection type
     */
    TypeModel getValueType();

}