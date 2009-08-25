/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ClassUtils;

import com.mysema.commons.lang.Assert;
import com.mysema.query.annotations.Literal;
import com.mysema.query.util.TypeUtil;

/**
 * 
 * @author tiwe
 *
 */
public final class ReflectionTypeModel extends InspectingTypeModel implements TypeModel{
    
    private static final Map<List<Type>,TypeModel> cache = new HashMap<List<Type>,TypeModel>();
    
    public static TypeModel get(Class<?> key){
        return get(key, key);
    }
    
    public static TypeModel get(Class<?> type, Type genericType){
        List<Type> key = Arrays.<Type>asList(type, genericType);
        if (cache.containsKey(key)){
            return cache.get(key);
        }else{
            TypeModel value = new ReflectionTypeModel(Assert.notNull(type), genericType);
            cache.put(key, value);
            return value;
        }
    }
    
    private ReflectionTypeModel(Class<?> cl, java.lang.reflect.Type genericType) {
        setNames(cl);
        typeCategory = getTypeCategory(name);
        if (cl.isArray()) {
            TypeModel valueInfo = get(cl.getComponentType());
            handleArray(valueInfo);
            
        } else if (cl.isEnum()) {            
            typeCategory = TypeCategory.SIMPLE;
            
        } else if (cl.isPrimitive()) {
            handlePrimitiveWrapperType(ClassUtils.primitiveToWrapper(cl));
            
        } else if (cl.isInterface()) {
            handleInterface(cl, genericType);

        } else {
            if (cl.getAnnotation(Literal.class) != null) {
                if (Comparable.class.isAssignableFrom(cl)) {
                    typeCategory = TypeCategory.COMPARABLE;
                } else {
                    typeCategory = TypeCategory.SIMPLE;
                }
            }
        }
    }

    private void handleInterface(Class<?> cl, java.lang.reflect.Type genericType) {
        if (Serializable.class.isAssignableFrom(cl)){
            setNames(Serializable.class);
            typeCategory = TypeCategory.SIMPLE;
        
        }else if (java.util.Map.class.isAssignableFrom(cl)) {
            TypeModel keyInfo = get(TypeUtil.getTypeParameter(genericType, 0));
            TypeModel valueInfo = get(TypeUtil.getTypeParameter(genericType, 1));
            handleMapInterface(keyInfo, valueInfo);

        } else if (java.util.List.class.isAssignableFrom(cl)) {
            TypeModel valueInfo = get(TypeUtil.getTypeParameter(genericType, 0));
            handleList(valueInfo);

        } else if (java.util.Collection.class.isAssignableFrom(cl)) {
            TypeModel valueInfo = get(TypeUtil.getTypeParameter(genericType, 0));
            handleCollection(valueInfo);            
        }
    }

}
