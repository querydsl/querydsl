/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;

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
public class ClassTypeModel extends AbstractTypeModel{
    
    public static final ClassTypeModel BOOLEAN = new ClassTypeModel(TypeCategory.BOOLEAN, Boolean.class, boolean.class);
    
    public static final ClassTypeModel BYTE = new ClassTypeModel(TypeCategory.NUMERIC, Byte.class, byte.class);
    
    public static final ClassTypeModel CHAR = new ClassTypeModel(TypeCategory.COMPARABLE, Character.class, char.class);
    
    public static final ClassTypeModel DOUBLE = new ClassTypeModel(TypeCategory.NUMERIC, Double.class, double.class);
    
    public static final ClassTypeModel FLOAT = new ClassTypeModel(TypeCategory.NUMERIC, Float.class, float.class);
    
    public static final ClassTypeModel INT = new ClassTypeModel(TypeCategory.NUMERIC, Integer.class, int.class);
    
    public static final ClassTypeModel LONG = new ClassTypeModel(TypeCategory.NUMERIC, Long.class, long.class);
    
    public static final ClassTypeModel SHORT = new ClassTypeModel(TypeCategory.NUMERIC, Short.class, short.class);
    
    private final Class<?> clazz;
    
    private final List<TypeModel> parameters;
    
    private final Class<?> primitiveClass;
    
    private final TypeCategory typeCategory;
    
    private final boolean visible;
    
    public ClassTypeModel(TypeCategory typeCategory, Class<?> clazz){
        this(typeCategory, clazz, ClassUtils.wrapperToPrimitive(clazz));
    }
    
    public ClassTypeModel(TypeCategory typeCategory, Class<?> clazz, Class<?> primitiveClass){
        this.typeCategory = Assert.notNull(typeCategory);
        this.clazz = Assert.notNull(clazz);
        this.primitiveClass = primitiveClass;
        this.parameters = Collections.emptyList();
        this.visible = clazz.getPackage().getName().equals("java.lang");
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
    public <T extends Appendable> T getLocalGenericName(TypeModel context, T builder, boolean asArgType) throws IOException {
        return getLocalRawName(context, builder);
    }

    @Override
    public <T extends Appendable> T getLocalRawName(TypeModel context, T builder) throws IOException {
        if (visible || context.getPackageName().equals(clazz.getPackage().getName())){
            builder.append(clazz.getName().substring(clazz.getPackage().getName().length()+1));    
        }else{
            builder.append(clazz.getName());
        }        
        return builder;
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
