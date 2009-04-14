/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt.model;


/**
 * Field represents a field / property in a query domain type.
 * 
 * @author tiwe
 * @version $Id$
 */
public class Field implements Comparable<Field> {

    /**
     * The Enum Type.
     */
    public enum Type {
        BOOLEAN, 
        COMPARABLE, 
        ENTITY, 
        ENTITYLIST, 
        ENTITYCOLLECTION, 
        ENTITYMAP, 
        NUMERIC,
        SIMPLE, 
        SIMPLELIST, 
        SIMPLECOLLECTION, 
        SIMPLEMAP, 
        STRING
    }

    private final Type fieldType;

    private String name, realName, keyTypeName, typeName, typePackage, simpleTypeName;

    /**
     * Construct a new Field instance
     * @param name normalized field name
     * @param realName real fieldName
     * @param keyTypeName key type name for Map types
     * @param typePackage
     * @param typeName full type name (with package)
     * @param simpleTypeName simple type name (local)
     * @param fieldType
     */
    public Field(String name, String realName, String keyTypeName, String typePackage, String typeName,
            String simpleTypeName, Type fieldType){
        this.name = name;
        this.realName = realName;
        this.keyTypeName = keyTypeName;
        this.typePackage = typePackage;
        this.typeName = typeName;
        this.simpleTypeName = simpleTypeName;
        this.fieldType = fieldType;
    }
    
    public int compareTo(Field o) {
        return name.compareTo(o.name);
    }

    public boolean equals(Object o) {
        return o instanceof Field && name.equals(((Field) o).name);
    }

    public Type getFieldType() {
        return fieldType;
    }

    /**
     * Returns the type name of key element for Map fields
     * @return
     */
    public String getKeyTypeName() {
        return keyTypeName;
    }

    public String getName() {
        return name;
    }
    
    public String getRealName(){
        return realName;
    }

    /**
     * Returns the simple name of the field type
     * @return
     */
    public String getSimpleTypeName() {
        return simpleTypeName;
    }

    /**
     * Returns the full name of the field type
     * @return
     */
    public String getTypeName() {
        return typeName;
    }
    
    public String getTypePackage(){
        return typePackage;
    }

    public int hashCode() {
        return name.hashCode();
    }

}