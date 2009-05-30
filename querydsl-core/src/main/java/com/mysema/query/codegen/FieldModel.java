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

    private String name;
    
    private TypeModel type;
    
    public FieldModel(String name, TypeModel type){
        this.name = name;
        this.type = type;
    }

    public int compareTo(FieldModel o) {
        return name.compareTo(o.name);
    }

    public boolean equals(Object o) {
        return o instanceof FieldModel && name.equals(((FieldModel) o).name);
    }

    public FieldType getFieldType() {
        return type.getFieldType();
    }

    public String getKeyTypeName() {
        return type.getKeyTypeName();
    }

    public String getName() {
        return name;
    }

    public String getSimpleTypeName() {
        return type.getSimpleName();
    }

    public String getTypeName() {
        return type.getFullName();
    }

    public String getTypePackage() {
        return type.getPackageName();
    }

    public int hashCode() {
        return name.hashCode();
    }

    public String toString() {
        return type.getFullName() + " " + name;
    }

}