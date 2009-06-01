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

import org.apache.commons.lang.ClassUtils;

import com.mysema.commons.lang.Assert;
import com.mysema.query.annotations.Literal;
import com.mysema.query.util.TypeUtil;

/**
 * 
 * @author tiwe
 *
 */
public class ReflectionTypeModel extends InspectingTypeModel implements TypeModel{
    
    private static Map<List<Type>,TypeModel> cache = new HashMap<List<Type>,TypeModel>();
    
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
        if (cl.isArray()) {
            TypeModel valueInfo = ReflectionTypeModel.get(cl.getComponentType());
            handleArray(valueInfo);
            
        } else if (cl.isEnum()) {
            setNames(cl);
            fieldType = FieldType.SIMPLE;
            
        } else if (cl.isPrimitive()) {
            handlePrimitiveWrapperType(ClassUtils.primitiveToWrapper(cl));
            
        } else if (cl.isInterface()) {
            if (java.util.Map.class.isAssignableFrom(cl)) {
                TypeModel keyInfo = ReflectionTypeModel.get(TypeUtil.getTypeParameter(genericType, 0));
                TypeModel valueInfo = ReflectionTypeModel.get(TypeUtil.getTypeParameter(genericType, 1));
                handleMapInterface(keyInfo, valueInfo);

            } else if (java.util.List.class.isAssignableFrom(cl)) {
                TypeModel valueInfo = ReflectionTypeModel.get(TypeUtil.getTypeParameter(genericType, 0));
                handleList(valueInfo);

            } else if (java.util.Collection.class.isAssignableFrom(cl)) {
                TypeModel valueInfo = ReflectionTypeModel.get(TypeUtil.getTypeParameter(genericType, 0));
                handleCollection(valueInfo);
            }

        } else {
            setNames(cl);
            if (cl.getAnnotation(Literal.class) != null) {
                if (Comparable.class.isAssignableFrom(cl)) {
                    fieldType = FieldType.COMPARABLE;
                } else {
                    fieldType = FieldType.SIMPLE;
                }
            } else {
                fieldType = getFieldType(name);
            }
        }
    }

}
