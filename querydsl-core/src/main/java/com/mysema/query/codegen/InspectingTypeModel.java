/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.commons.lang.ClassUtils;

import com.mysema.commons.lang.Assert;
import com.mysema.query.annotations.Literal;
import com.mysema.query.util.TypeUtil;



/**
 * @author tiwe
 *
 */
public class InspectingTypeModel extends TypeModel {
   
    public InspectingTypeModel(){}
    
    public InspectingTypeModel(Class<?> cl, java.lang.reflect.Type genericType){
        if (cl.isPrimitive()) {
            cl = ClassUtils.primitiveToWrapper(cl);
        } 
        
        if (cl.isArray()) {
            TypeModel valueInfo = create(cl.getComponentType());
            handleArray(valueInfo);

        } else if (cl.isEnum()) {
            setNames(cl);
            typeCategory = TypeCategory.SIMPLE;

        } else if (Map.class.isAssignableFrom(cl)) {
            TypeModel keyInfo = create(TypeUtil.getTypeParameter(genericType, 0));
            TypeModel valueInfo = create(TypeUtil.getTypeParameter(genericType, 1));
            handleMap(keyInfo, valueInfo);

        } else if (List.class.isAssignableFrom(cl)) {
            TypeModel valueInfo = create(TypeUtil.getTypeParameter(genericType, 0));
            handleList(valueInfo);

        } else if (Collection.class.isAssignableFrom(cl)) {
            TypeModel valueInfo = create(TypeUtil.getTypeParameter(genericType, 0));
            handleCollection(valueInfo);

        } else if (cl.getAnnotation(Literal.class) != null) {
            setNames(cl);
            if (Comparable.class.isAssignableFrom(cl)) {
                typeCategory = TypeCategory.COMPARABLE;
            } else {
                typeCategory = TypeCategory.SIMPLE;
            }
        } else {
            setNames(cl);
            typeCategory = TypeCategory.get(cl.getName());
        }
    }
        
    private void handle(@Nullable TypeModel key, TypeModel value, TypeCategory entity, TypeCategory simple){
        setNames(value);        
        this.keyType = key;
        this.valueType = value;
        if (value.getTypeCategory() == TypeCategory.ENTITY) {
            this.typeCategory = entity;
        } else {
            this.typeCategory = simple;
        }
    }
    
    protected final void handleArray(TypeModel valueType) {
        handle(null, valueType, TypeCategory.ENTITYCOLLECTION, TypeCategory.SIMPLECOLLECTION);
    }
    
    protected final void handleCollection(TypeModel valueType) {
        handle(null, valueType, TypeCategory.ENTITYCOLLECTION, TypeCategory.SIMPLECOLLECTION);
    }
    
    protected final void handleList(TypeModel valueType) {
        handle(null, valueType, TypeCategory.ENTITYLIST, TypeCategory.SIMPLELIST);
    }
    
    protected final void handleMap(TypeModel keyType, TypeModel valueType) {       
        handle(keyType, valueType, TypeCategory.ENTITYMAP, TypeCategory.SIMPLEMAP);
    }
    
    protected final void setNames(Class<?> cl){
        packageName = Assert.notNull(cl.getPackage().getName());
        name = cl.getName();
        simpleName = cl.getSimpleName();
    }
    
    protected final void setNames(TypeModel valueInfo) {
        packageName = valueInfo.getPackageName();
        name = valueInfo.getName();
        simpleName = valueInfo.getSimpleName();
    }
    
}
