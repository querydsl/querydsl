/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import javax.annotation.Nullable;

import net.jcip.annotations.Immutable;

import com.mysema.commons.lang.Assert;


/**
 * SimpleTypeModel represents a java type
 * 
 * @author tiwe
 *
 */
@Immutable
public final class SimpleTypeModel implements TypeModel {

    @Nullable
    private final TypeModel keyType, valueType;

    private final String name, packageName, simpleName, localName;

    private final TypeCategory typeCategory;
    
    public SimpleTypeModel(
            TypeCategory typeCategory, 
            String name,
            String packageName, 
            String simpleName, 
            @Nullable TypeModel keyType,
            @Nullable TypeModel valueType) {
        this.typeCategory = Assert.notNull(typeCategory,"typeCategory is null");
        this.name = Assert.notNull(name,"name is null");
        this.packageName = Assert.notNull(packageName,"packageName is null");
        this.simpleName = Assert.notNull(simpleName,"simpleName is null");
        this.localName = name.substring(packageName.length()+1);
        this.keyType = keyType;
        this.valueType = valueType;
    }

    public SimpleTypeModel as(TypeCategory category) {
        if (typeCategory == category){
            return this;
        }else{
            return new SimpleTypeModel(category, name, packageName, simpleName, keyType, valueType);
        }
    }

    public TypeModel getKeyType() {
        return keyType;
    }

    public String getLocalName(){
        return localName;
    }

    public String getName() {
        return name;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getPrimitiveName(){
        return null;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public TypeCategory getTypeCategory() {
        return typeCategory;
    }

    public TypeModel getValueType() {
        return valueType;
    }

    public boolean isPrimitive() {
        return false;
    }

    public String toString() {
        return name;
    }

    
}