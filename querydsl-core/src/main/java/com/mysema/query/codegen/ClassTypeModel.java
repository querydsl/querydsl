/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import net.jcip.annotations.Immutable;

import org.apache.commons.lang.ClassUtils;

import com.mysema.commons.lang.Assert;

/**
 * ClassTypeModel is a minimal implementation of the TypeModel interface
 * 
 * @author tiwe
 *
 */
@Immutable
public final class ClassTypeModel extends AbstractTypeModel{
        
    private final Class<?> clazz;
    
    private final List<TypeModel> parameters;
    
    @Nullable
    private final Class<?> primitiveClass;
    
    private final TypeCategory typeCategory;
    
    private final boolean visible;
    
    public ClassTypeModel(TypeCategory typeCategory, Class<?> clazz, TypeModel... params){
        this(typeCategory, clazz, ClassUtils.wrapperToPrimitive(clazz), params);
    }
    
    public ClassTypeModel(TypeCategory typeCategory, Class<?> clazz, @Nullable Class<?> primitiveClass, TypeModel... params){
        this.typeCategory = Assert.notNull(typeCategory);
        this.clazz = Assert.notNull(clazz);
        this.primitiveClass = primitiveClass;
        this.parameters = Arrays.asList(params);
        if (clazz.isArray()){
            this.visible = clazz.getComponentType().getPackage().getName().equals("java.lang");
        }else{
            this.visible = clazz.getPackage().getName().equals("java.lang");    
        }        
    }
    
    @Override
    public TypeModel asArrayType() {        
        return new ClassTypeModel(TypeCategory.ARRAY, Array.newInstance(clazz, 0).getClass(), this);
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
    public boolean equals(Object o){
        if (o instanceof TypeModel){
            TypeModel t = (TypeModel)o;
            return clazz.getName().equals(t.getFullName());
        }else{
            return false;
        }
    }

    @Override
    public TypeCategory getCategory() {
        return typeCategory;
    }

    @Override
    public String getFullName() {
        return clazz.getName();
    }

    @Override
    public void appendLocalGenericName(TypeModel context, Appendable builder, boolean asArgType) throws IOException {
        appendLocalRawName(context, builder);
    }

    @Override
    public void appendLocalRawName(TypeModel context, Appendable builder) throws IOException {
        String packageName; 
        String name;
        if (clazz.isArray()){
            packageName = clazz.getComponentType().getPackage().getName();
            name = clazz.getComponentType().getName();
        }else{
            packageName = clazz.getPackage().getName();
            name = clazz.getName();
        }
        if (visible || context.getPackageName().equals(packageName)){
            builder.append(name.substring(packageName.length()+1));    
        }else{
            builder.append(name);
        }     
        if (clazz.isArray()){
            builder.append("[]");
        }
    }
        

    @Override
    public String getPackageName() {
        return clazz.getPackage().getName();
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
    public String getPrimitiveName() {
        return primitiveClass != null ? primitiveClass.getSimpleName() : null;
    }

    @Override
    public TypeModel getSelfOrValueType() {
        if (typeCategory.isSubCategoryOf(TypeCategory.COLLECTION) 
         || typeCategory.isSubCategoryOf(TypeCategory.MAP)){
            return parameters.get(parameters.size()-1);
        }else{
            return this;
        }
    }
    
    @Override
    public String getSimpleName() {
        return clazz.getSimpleName();
    }

    @Override
    public boolean hasEntityFields() {
        return false;
    }

    public int hashCode(){
        return clazz.getName().hashCode();
    }

    @Override
    public boolean isFinal() {
        return Modifier.isFinal(clazz.getModifiers());
    }

    @Override
    public boolean isPrimitive() {
        return primitiveClass != null;
    }

}
