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
 * TypeModel represents a java type
 * 
 * @author tiwe
 *
 */
@Immutable
public final class TypeModel {

    private final TypeCategory typeCategory;

    private final String name, packageName, simpleName, localName;

    @Nullable
    private final TypeModel keyType, valueType;
    
    public TypeModel(TypeCategory typeCategory, Class<?> clazz){
        this(typeCategory, clazz.getName(), clazz.getPackage().getName(), clazz.getSimpleName(), null, null);
    }

    public TypeModel(
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

    public TypeCategory getTypeCategory() {
        return typeCategory;
    }

    public String getName() {
        return name;
    }

    public TypeModel getKeyType() {
        return keyType;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getSimpleName() {
        return simpleName;
    }
    
    public String getLocalName(){
        return localName;
    }

    public TypeModel getValueType() {
        return valueType;
    }
    
    public String toString() {
        return name;
    }

    public TypeModel as(TypeCategory category) {
        if (typeCategory == category){
            return this;
        }else{
            return new TypeModel(category, name, packageName, simpleName, keyType, valueType);
        }
    }
    
//    public TypeModel as(TypeCategory category) {
//        if (typeCategory.isSubCategoryOf(category)){
//            return this;
//        }else{
//            return new TypeModel(category, name, packageName, simpleName, keyType, valueType);
//        }
//    }

}