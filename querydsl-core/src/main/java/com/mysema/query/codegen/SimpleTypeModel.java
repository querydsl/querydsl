/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import com.mysema.commons.lang.Assert;

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
        this.fieldType = Assert.notNull(fieldType,"fieldType is null");
        this.name = Assert.notNull(fullName,"fullName is null");
        this.packageName = Assert.notNull(packageName,"packageName is null");
        this.simpleName = Assert.notNull(simpleName,"simpleName is null");
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
