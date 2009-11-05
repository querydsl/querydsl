/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ClassUtils;

import com.mysema.commons.lang.Assert;

/**
 * @author tiwe
 *
 */
public class ClassTypeModel implements TypeModel{
    
    private final TypeCategory typeCategory;
    
    private final Class<?> clazz;
    
    private final Class<?> primitiveClass;
    
    private final List<TypeModel> parameters;
    
    public ClassTypeModel(TypeCategory typeCategory, Class<?> clazz){
        this(typeCategory, clazz, ClassUtils.wrapperToPrimitive(clazz));
    }
    
    public ClassTypeModel(TypeCategory typeCategory, Class<?> clazz, Class<?> primitiveClass){
        this.typeCategory = Assert.notNull(typeCategory);
        this.clazz = Assert.notNull(clazz);
        this.primitiveClass = primitiveClass;
        // TODO
        this.parameters = Collections.emptyList();
    }
    
    @Override
    public TypeModel as(TypeCategory category) {
        if (typeCategory == category){
            return this;
        }else{
            return new ClassTypeModel(category, clazz);
        }
    }

    @Override
    public String getLocalName() {
        return clazz.getName().substring(clazz.getPackage().getName().length()+1);
    }

    @Override
    public String getName() {
        return clazz.getName();
    }

    @Override
    public String getPackageName() {
        return clazz.getPackage().getName();
    }

    @Override
    public String getPrimitiveName() {
        return primitiveClass != null ? primitiveClass.getSimpleName() : null;
    }

    @Override
    public String getSimpleName() {
        return clazz.getSimpleName();
    }

    @Override
    public TypeCategory getTypeCategory() {
        return typeCategory;
    }

    @Override
    public boolean isPrimitive() {
        return primitiveClass != null;
    }

    @Override
    public TypeModel getParameter(int i) {
        return parameters.get(i);
    }

    @Override
    public int getParameterCount() {
        return parameters.size();
    }

    @Override
    public TypeModel getSelfOrValueType() {
        if (parameters.isEmpty()){
            return this;
        }else{
            TypeModel rv = parameters.get(parameters.size()-1);
            return rv != null ? rv : this;
        }
    }

}
