/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.mysema.commons.lang.Assert;


/**
 * TypeModel represents a java type
 * 
 * @author tiwe
 *
 */
public class TypeModel {

    private static final Map<List<Type>,TypeModel> cache = new HashMap<List<Type>,TypeModel>();
    
    public static TypeModel create(Class<?> key){
        return create(key, key, false);
    }
    
    public static TypeModel createLiteralType(Class<?> key){
        return create(key, key, true);
    }
    
    public static TypeModel create(Class<?> type, Type genericType){
        return create(type, genericType, false);
    }
    
    private static TypeModel create(Class<?> type, Type genericType, boolean literal){
        List<Type> key = Arrays.<Type>asList(type, genericType);
        if (cache.containsKey(key)){
            return cache.get(key);
        }else{
            TypeModel value = new InspectingTypeModel(Assert.notNull(type), genericType);
            if (literal && !value.typeCategory.isChildOf(TypeCategory.SIMPLE)){
                if (Comparable.class.isAssignableFrom(type)){
                    value.typeCategory = TypeCategory.COMPARABLE;
                }else{
                    value.typeCategory = TypeCategory.SIMPLE;
                }
            }
            cache.put(key, value);
            return value;
        }
    }
    
    protected TypeCategory typeCategory = TypeCategory.ENTITY;

    protected String name, packageName, simpleName, localName;

    @Nullable
    protected TypeModel keyType, valueType;

    protected TypeModel(){}
    
    public TypeModel(
            TypeCategory typeCategory, 
            String fullName,
            String packageName, 
            String simpleName, 
            @Nullable TypeModel keyType,
            @Nullable TypeModel valueType) {
        this.typeCategory = Assert.notNull(typeCategory,"typeCategory is null");
        this.name = Assert.notNull(fullName,"fullName is null");
        this.packageName = Assert.notNull(packageName,"packageName is null");
        this.simpleName = Assert.notNull(simpleName,"simpleName is null");
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
        if (localName == null){
            localName = name.substring(packageName.length()+1);
        }
        return localName;
    }

    public TypeModel getValueType() {
        return valueType;
    }
    
    public String toString() {
        return name;
    }

}