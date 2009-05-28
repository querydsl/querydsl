/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

/**
 * FieldModel represents a field / property in a query domain type.
 * 
 * @author tiwe
 * @version $Id$
 */
public class FieldModel implements Comparable<FieldModel> {

    /**
     * The Enum Type.
     */

    private final FieldType fieldType;

    private String name, keyTypeName, typeName, typePackage, simpleTypeName;

    /**
     * Construct a new Field instance
     * 
     * @param name
     *            normalized field name
     * @param realName
     *            real fieldName
     * @param keyTypeName
     *            key type name for Map types
     * @param typePackage
     * @param typeName
     *            full type name (with package)
     * @param simpleTypeName
     *            simple type name (local)
     * @param fieldType
     */
    public FieldModel(String name, String keyTypeName,
            String typePackage, String typeName, String simpleTypeName,
            FieldType fieldType) {
        this.name = name;
        this.keyTypeName = keyTypeName;
        this.typePackage = typePackage;
        this.typeName = typeName;
        this.simpleTypeName = simpleTypeName;
        this.fieldType = fieldType;
    }

    public int compareTo(FieldModel o) {
        return name.compareTo(o.name);
    }

    public boolean equals(Object o) {
        return o instanceof FieldModel && name.equals(((FieldModel) o).name);
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    /**
     * Returns the type name of key element for Map fields
     * 
     * @return
     */
    public String getKeyTypeName() {
        return keyTypeName;
    }

    public String getName() {
        return name;
    }

    /**
     * Returns the simple name of the field type
     * 
     * @return
     */
    public String getSimpleTypeName() {
        return simpleTypeName;
    }

    /**
     * Returns the full name of the field type
     * 
     * @return
     */
    public String getTypeName() {
        return typeName;
    }

    public String getTypePackage() {
        return typePackage;
    }

    public int hashCode() {
        return name.hashCode();
    }

    public String toString() {
        return typeName + " " + name;
    }

}