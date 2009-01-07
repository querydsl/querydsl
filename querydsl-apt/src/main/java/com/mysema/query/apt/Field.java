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
        BOOLEAN, COMPARABLE, ENTITY, ENTITYLIST, ENTITYCOLLECTION, ENTITYMAP, SIMPLE, SIMPLELIST, SIMPLECOLLECTION, SIMPLEMAP, STRING
    }

    private final Type fieldType;

    private String name, keyTypeName, typeName, simpleTypeName;

    public Field(FieldDeclaration field) {
        TypeInfo typeInfo = new TypeInfo(field.getType());
        this.name = field.getSimpleName();
        this.keyTypeName = typeInfo.getKeyTypeName();
        this.typeName = typeInfo.getFullName();
        this.simpleTypeName = typeInfo.getSimpleName();
        this.fieldType = typeInfo.getFieldType();
    }

    public Field(String name, String keyTypeName, String typeName,
            String simpleTypeName, Type fieldType) {
        this.name = name;
        this.keyTypeName = keyTypeName;
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

    public String getKeyTypeName() {
        return keyTypeName;
    }

    public String getName() {
        return name;
    }
    
    // TODO : improve this name normalization
    public String getRealName(){
        if (name.equals("prvate")){
            return "private";
        }else if (name.equals("pblic")){
            return "public";
        }else{
            return name;
        }
    }

    public String getSimpleTypeName() {
        return simpleTypeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public int hashCode() {
        return name.hashCode();
    }

}