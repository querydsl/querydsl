/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.lang.reflect.Type;
import java.util.Locale;

import org.apache.commons.lang.ClassUtils;

import com.mysema.query.annotations.Literal;
import com.mysema.query.util.TypeUtil;

/**
 * 
 * @author tiwe
 *
 */
public class ClassModelBuilder {

    private FieldType fieldType;

    private String simpleName, fullName, packageName = "", keyTypeName;

    public ClassModelBuilder(Class<?> cl) {
        this(cl, cl);
    }
    
    public static ClassModel createType(Class<?> clazz) {
        ClassModel type = new ClassModel(clazz.getSuperclass().getName(), clazz
                .getPackage().getName(), clazz.getName(), clazz.getSimpleName());
        for (java.lang.reflect.Field f : clazz.getDeclaredFields()) {
            ClassModelBuilder typeHelper = new ClassModelBuilder(f.getType(), f.getGenericType());
            FieldModel field = new FieldModel(
                    f.getName(), 
                    typeHelper.getKeyTypeName(), typeHelper.getPackageName(),
                    typeHelper.getFullName(), typeHelper.getSimpleName(),
                    typeHelper.getFieldType());
            type.addField(field);
        }
        return type;
    }


    public ClassModelBuilder(Class<?> cl, java.lang.reflect.Type genericType) {
        if (cl == null) {
            throw new IllegalArgumentException("cl was null");
        } else if (cl.isArray()) {
            visitArrayType(cl);
        } else if (cl.isEnum()) {
            visitEnumType(cl);
        } else if (cl.isPrimitive()) {
            visitPrimitiveType(cl);
        } else if (cl.isInterface()) {
            visitInterfaceType(cl, genericType);
        } else {
            visitClassType(cl);
        }
        if (fullName == null) {
            fullName = cl.getName();
        }

        setDefaults();
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public String getFullName() {
        return fullName;
    }

    public String getKeyTypeName() {
        return keyTypeName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public String getValueTypeName() {
        return fullName;
    }

    private void handleCollectionInterface(Class<?> type, Type genericType) {
        ClassModelBuilder valueInfo = new ClassModelBuilder(TypeUtil.getTypeParameter(genericType, 0));
        handleCollection(valueInfo);
    }

    private void handleCollection(ClassModelBuilder valueInfo) {
        fullName = valueInfo.getFullName();
        packageName = valueInfo.getPackageName();
        if (valueInfo.fieldType == FieldType.ENTITY) {
            fieldType = FieldType.ENTITYCOLLECTION;
        } else {
            fieldType = FieldType.SIMPLECOLLECTION;
        }
    }

    private void handleList(ClassModelBuilder valueInfo) {
        fullName = valueInfo.getFullName();
        packageName = valueInfo.getPackageName();
        if (valueInfo.fieldType == FieldType.ENTITY) {
            fieldType = FieldType.ENTITYLIST;
        } else {
            fieldType = FieldType.SIMPLELIST;
        }
    }

    private void handleListInterface(Class<?> type, Type genericType) {
        ClassModelBuilder valueInfo = new ClassModelBuilder(TypeUtil.getTypeParameter(genericType, 0));
        handleList(valueInfo);
    }

    private void handleMapInterface(Class<?> type, Type genericType) {
        ClassModelBuilder keyInfo = new ClassModelBuilder(TypeUtil.getTypeParameter(genericType, 0));
        ClassModelBuilder valueInfo = new ClassModelBuilder(TypeUtil.getTypeParameter(genericType, 1));
        handleMapInterface(keyInfo, valueInfo);
    }

    private void handleMapInterface(ClassModelBuilder keyInfo, ClassModelBuilder valueInfo) {
        keyTypeName = keyInfo.getFullName();
        fullName = valueInfo.getFullName();
        packageName = valueInfo.getPackageName();
        if (valueInfo.fieldType == FieldType.ENTITY) {
            fieldType = FieldType.ENTITYMAP;
        } else {
            fieldType = FieldType.SIMPLEMAP;
        }
    }

    private void setDefaults() {
        if (fieldType == null) {
            fieldType = FieldType.ENTITY;
        }
        if (simpleName == null) {
            simpleName = fullName.substring(fullName.lastIndexOf('.') + 1);
        }
    }

    public String toString() {
        return fullName;
    }

    private void visitArrayComponentType(ClassModelBuilder valueInfo) {
        fullName = valueInfo.getFullName();
        packageName = valueInfo.getPackageName();
        if (valueInfo.fieldType == FieldType.ENTITY) {
            fieldType = FieldType.ENTITYCOLLECTION;
        } else {
            fieldType = FieldType.SIMPLECOLLECTION;
        }
    }

    public void visitArrayType(Class<?> clazz) {
        ClassModelBuilder valueInfo = new ClassModelBuilder(clazz.getComponentType());
        visitArrayComponentType(valueInfo);
    }

    public void visitClassType(Class<?> type) {
        fullName = type.getName();
        packageName = type.getPackage().getName();

        if (type.equals(String.class)) {
            fieldType = FieldType.STRING;

        } else if (type.equals(Boolean.class)) {
            fieldType = FieldType.BOOLEAN;

        } else if (type.equals(Locale.class) || type.equals(Class.class)
                || type.equals(Object.class)) {
            fieldType = FieldType.SIMPLE;

        } else if (isNumericSupported(fullName)
                && Number.class.isAssignableFrom(type)) {
            fieldType = FieldType.NUMERIC;

        } else if (type.getAnnotation(Literal.class) != null) {
            if (Comparable.class.isAssignableFrom(type)) {
                fieldType = FieldType.COMPARABLE;
            } else {
                fieldType = FieldType.SIMPLE;
            }

        } else if (isComparableSupported(fullName)
                && Comparable.class.isAssignableFrom(type)) {
            fieldType = FieldType.COMPARABLE;

        } else if (asSimpleType(fullName)) {
            fieldType = FieldType.SIMPLE;

        }
    }

    private boolean isNumericSupported(String fullName) {
        return isComparableSupported(fullName);
    }

    private boolean isComparableSupported(String fullName) {
        return fullName.startsWith("java.") || fullName.startsWith("javax.")
                || fullName.startsWith("org.joda.time");
    }

    private boolean asSimpleType(String fullName) {
        return false;
    }

    public void visitEnumType(Class<?> type) {
        fieldType = FieldType.SIMPLE;
    }

    public void visitInterfaceType(Class<?> type, Type genericType) {
        if (java.util.Map.class.isAssignableFrom(type)) {
            handleMapInterface(type, genericType);

        } else if (java.util.List.class.isAssignableFrom(type)) {
            handleListInterface(type, genericType);

        } else if (java.util.Collection.class.isAssignableFrom(type)) {
            handleCollectionInterface(type, genericType);
        }

    }

    public void visitPrimitiveType(Class<?> cl) {
        visitPrimitiveWrapperType(ClassUtils.primitiveToWrapper(cl));
    }

    private void visitPrimitiveWrapperType(Class<?> cl) {
        if (cl.equals(Boolean.class)) {
            fieldType = FieldType.BOOLEAN;
        } else if (Number.class.isAssignableFrom(cl)) {
            fieldType = FieldType.NUMERIC;
        } else if (Comparable.class.isAssignableFrom(cl)) {
            fieldType = FieldType.COMPARABLE;
        } else {
            fieldType = FieldType.SIMPLE;
        }
        fullName = cl.getName();
        simpleName = cl.getSimpleName();
    }

}
