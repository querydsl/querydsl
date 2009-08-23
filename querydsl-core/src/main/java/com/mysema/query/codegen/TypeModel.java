/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;


/**
 * Type model represents a class of field
 * 
 * @author tiwe
 *
 */
public interface TypeModel {

    /**
     * Field type of the Type
     * 
     * @return field type of type
     */
    FieldType getFieldType();

    /**
     * Key type or null
     * 
     * @return key type of Map type
     */
    TypeModel getKeyType();

    /**
     * Local name
     * 
     * @return name including optional enclosing class' simple name and 
     */
    String getLocalName();
    
    /**
     * Fully qualified class name
     * 
     * @return fully qualified class name
     */
    String getName();

    /**
     * Package name
     * 
     * @return package name
     */
    String getPackageName();

    /**
     * Simple name
     * 
     * @return simple class name
     */
    String getSimpleName();
    
    /**
     * Value type or null if not applicable
     * 
     * @return value type of Map and Collection type
     */
    TypeModel getValueType();

}