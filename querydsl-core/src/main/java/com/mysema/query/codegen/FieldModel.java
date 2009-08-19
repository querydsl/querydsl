/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import net.jcip.annotations.Immutable;

import com.mysema.commons.lang.Assert;

/**
 * FieldModel represents a field / property in a query domain type.
 * 
 * @author tiwe
 * @version $Id$
 */
@Immutable
public class FieldModel implements Comparable<FieldModel> {
    
    private final ClassModel model;
    
    private final String name, docs, typeName, queryTypeName, keyTypeName, valueTypeName;
    
    private final TypeModel type;
    
    public FieldModel(ClassModel model, String name, TypeModel type, String docs){
        this.model = model;
        this.name = Assert.notNull(name,"name is null");
        this.type = Assert.notNull(type,"type is null");
        this.docs = Assert.notNull(docs,"docs is null").replace("@return", "").trim();
        if (type.getSimpleName() == null){
            throw new IllegalArgumentException("Field with name " + name + " got no valid type : " + type);
        }
        this.typeName = getLocalName(type);
        this.keyTypeName = type.getKeyType() != null ? getLocalName(type.getKeyType()) : null;
        this.valueTypeName = type.getValueType() != null ? getLocalName(type.getValueType()) : null;
        
        if (isVisible(type)){
            this.queryTypeName = "Q"  + type.getSimpleName();
        }else{
            this.queryTypeName = type.getPackageName() + ".Q" + type.getSimpleName();
        }
    }
    
    private boolean isVisible(TypeModel type){
        return model.getPackageName().equals(type.getPackageName()) 
            || type.getPackageName().equals("java.lang");
    }
    
    private String getLocalName(TypeModel type){        
        return isVisible(type) ? type.getSimpleName() : type.getName();
    }
    
    public int compareTo(FieldModel o) {
        return name.compareToIgnoreCase(o.name);
    }

    public FieldModel createCopy(ClassModel model){
        return new FieldModel(model, name, type, docs);
    }

    public boolean equals(Object o) {
        return o instanceof FieldModel && name.equals(((FieldModel) o).name);
    }

    public String getDocString() {
        return docs;
    }

    public FieldType getFieldType() {
        return type.getFieldType();
    }

    public String getKeyTypeName() {
        return keyTypeName;
    }
    
    public String getValueTypeName() {
        return valueTypeName;
    }


    public String getName() {
        return name;
    }
    
    public String getQueryTypeName() {
        return queryTypeName;
    }

    public String getSimpleTypeName() {
        return type.getSimpleName();
    }

    public String getTypeName() {
        return typeName;
    }
    
    public String getTypePackage() {
        return type.getPackageName();
    }

    public int hashCode() {
        return name.hashCode();
    }

    public String toString() {
        return type.getName() + " " + name;
    }

}