/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import javax.annotation.Nullable;

import com.mysema.commons.lang.Assert;

/**
 * @author tiwe
 *
 */
public class SimpleTypeModel implements TypeModel {

    protected FieldType fieldType = FieldType.ENTITY;

    protected String name, packageName, simpleName;

    @Nullable
    protected TypeModel keyType, valueType;

    protected SimpleTypeModel(){}
    
    public SimpleTypeModel(
            FieldType fieldType, 
            String fullName,
            String packageName, 
            String simpleName, 
            @Nullable TypeModel keyType,
            @Nullable TypeModel valueType) {
        this.fieldType = Assert.notNull(fieldType,"fieldType is null");
        this.name = Assert.notNull(fullName,"fullName is null");
        this.packageName = Assert.notNull(packageName,"packageName is null");
        this.simpleName = Assert.notNull(simpleName,"simpleName is null");
        this.keyType = keyType;
        this.valueType = valueType;
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
    public TypeModel getKeyType() {
        return keyType;
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
    public TypeModel getValueType() {
        return valueType;
    }
    
    public String toString() {
        return name;
    }


}
