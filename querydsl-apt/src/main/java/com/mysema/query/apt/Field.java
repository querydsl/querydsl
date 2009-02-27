/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt;

import com.mysema.query.apt.util.TypeInfo;
import com.sun.mirror.declaration.FieldDeclaration;

/**
 * FieldDecl represents a field / property in a query domain type.
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
     * Construct a Field instance from the given FieldDeclaration instance
     * @param field
     */
    public Field(FieldDeclaration field) {
        TypeInfo typeInfo = new TypeInfo(field.getType());
        this.name = javaSafe(field.getSimpleName());
        this.realName = realName(name);
        this.keyTypeName = typeInfo.getKeyTypeName();        
        this.typeName = typeInfo.getFullName();
        this.typePackage = typeInfo.getPackageName();
        this.simpleTypeName = typeInfo.getSimpleName();
        this.fieldType = typeInfo.getFieldType();        
    }

    /**
     * Construct a new Field instance
     * @param name normalized field name
     * @param realName real fieldName
     * @param keyTypeName key type name for Map types
     * @param typeName full type name (with package)
     * @param simpleTypeName simple type name (local)
     * @param fieldType
     */
    public Field(String name, String realName, String keyTypeName, String typeName,
            String simpleTypeName, Type fieldType){
        this.name = javaSafe(name);
        this.realName = realName(realName);
        this.keyTypeName = keyTypeName;
        this.typeName = typeName;
        this.simpleTypeName = simpleTypeName;
        this.fieldType = fieldType;
    }
    
    private static String javaSafe(String name){
        if (name.equals("private")){
            return "prvate";
        }else if (name.equals("public")){
            return "pblic";
        }else{
            return name;
        }
    }
    
    private static String realName(String name){
        if (name.equals("prvate")){
            return "private";
        }else if (name.equals("pblic")){
            return "public";
        }else{
            return name;
        }
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