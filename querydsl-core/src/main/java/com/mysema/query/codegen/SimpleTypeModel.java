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

    protected FieldType fieldType;

    protected String fullName, packageName, simpleName;

    protected String keyTypeName, valueTypeName;

    protected SimpleTypeModel(){}
    
    public SimpleTypeModel(FieldType fieldType, String fullName,
            String packageName, String simpleName, String keyTypeName,
            String valueTypeName) {
        this.fieldType = fieldType;
        this.fullName = fullName;
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
    public String getFullName() {
        return fullName;
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

}
