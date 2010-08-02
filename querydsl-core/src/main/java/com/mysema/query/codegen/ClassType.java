/*
 * Copyright (c) 2010 Mysema Ltd.
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
 * ClassType is a minimal {@link Class} based implementation of the {@link Type} interface
 *
 * @author tiwe
 *
 */
@Immutable
public final class ClassType extends AbstractType{

    private final Class<?> clazz;

    private final List<Type> parameters;

    @Nullable
    private final Class<?> primitiveClass;

    private final TypeCategory typeCategory;

    private final boolean visible;

    public ClassType(TypeCategory typeCategory, Class<?> clazz, Type... params){
        this(typeCategory, clazz, ClassUtils.wrapperToPrimitive(clazz), params);
    }

    public ClassType(TypeCategory typeCategory, Class<?> clazz, @Nullable Class<?> primitiveClass, Type... params){
        this.typeCategory = Assert.notNull(typeCategory,"typeCategory");
        this.clazz = Assert.notNull(clazz,"clazz");
        this.primitiveClass = primitiveClass;
        this.parameters = Arrays.asList(params);
        if (clazz.isArray()){
            this.visible = clazz.getComponentType().getPackage().getName().equals("java.lang");
        }else{
            this.visible = clazz.getPackage().getName().equals("java.lang");
        }
    }

    @Override
    public Type asArrayType() {
        return new ClassType(TypeCategory.ARRAY, Array.newInstance(clazz, 0).getClass(), this);
    }

    @Override
    public Type as(TypeCategory category) {
        if (typeCategory == category){
            return this;
        }else{
            return new ClassType(category, clazz);
        }
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof Type){
            Type t = (Type)o;
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
    public void appendLocalGenericName(Type context, Appendable builder, boolean asArgType) throws IOException {        
        appendLocalRawName(context, builder);
        if (!parameters.isEmpty()){
            String fullName = clazz.getName();
            builder.append("<");
            for (int i = 0; i < parameters.size(); i++){
                if (i > 0){
                    builder.append(",");
                }
                if (parameters.get(i) != null && !parameters.get(i).getFullName().equals(fullName)){
                    parameters.get(i).appendLocalGenericName(context, builder, false);
                }else{
                    builder.append("?");
                }
            }
            builder.append(">");
        }
    }

    @Override
    public void appendLocalRawName(Type context, Appendable builder) throws IOException {
        String packageName;
        String name;
        if (clazz.isArray()){
            packageName = clazz.getComponentType().getPackage().getName();
            name = clazz.getComponentType().getName();
        }else{
            packageName = clazz.getPackage().getName();
            name = clazz.getName();
        }
        if ((visible || context.getPackageName().equals(packageName)) && !packageName.isEmpty()){
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
    public Type getParameter(int i) {
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
    public Type getSelfOrValueType() {
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
