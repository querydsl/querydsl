/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

/**
 * @author tiwe
 *
 */
public class SimpleTypeModel implements TypeModel {

    protected FieldType fieldType = FieldType.ENTITY;

    protected String name, packageName, simpleName;

    protected String keyTypeName, valueTypeName;

    protected SimpleTypeModel(){}
    
    public SimpleTypeModel(FieldType fieldType, String fullName,
            String packageName, String simpleName, String keyTypeName,
            String valueTypeName) {
        this.fieldType = fieldType;
        this.name = fullName;
        this.packageName = packageName;
        this.simpleName = simpleName;
        this.keyTypeName = keyTypeName;
        this.valueTypeName = valueTypeName;
    }

    @Override
    public FieldType getFieldType() {
        return fieldType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getKeyTypeName() {
        return keyTypeName;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public String getSimpleName() {
        return simpleName;
    }

    @Override
    public String getValueTypeName() {
        return valueTypeName;
    }
    
    public String toString() {
        return name;
    }


}
