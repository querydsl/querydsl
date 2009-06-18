/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import com.mysema.commons.lang.Assert;

/**
 * FieldModel represents a field / property in a query domain type.
 * 
 * @author tiwe
 * @version $Id$
 */
public class FieldModel implements Comparable<FieldModel> {

    private String name;
    
    private TypeModel type;
    
    private String docs;
    
    public FieldModel(String name, TypeModel type, String docs){
        this.name = Assert.notNull(name);
        this.type = Assert.notNull(type);
        this.docs = Assert.notNull(docs).replace("@return", "").trim();
        if (type.getSimpleName() == null){
            throw new IllegalArgumentException("Field with name " + name + " got no valid type : " + type);
        }
    }

    public int compareTo(FieldModel o) {
        return name.compareToIgnoreCase(o.name);
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
        return type.getName();
    }

    public String getTypePackage() {
        return type.getPackageName();
    }
    
    public String getDocString() {
        return docs;
    }

    public int hashCode() {
        return name.hashCode();
    }

    public String toString() {
        return type.getName() + " " + name;
    }

}