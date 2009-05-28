/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt.general;

import java.util.Iterator;
import java.util.Locale;

import com.mysema.query.annotations.Literal;
import com.mysema.query.codegen.FieldType;
import com.sun.mirror.type.AnnotationType;
import com.sun.mirror.type.ArrayType;
import com.sun.mirror.type.ClassType;
import com.sun.mirror.type.EnumType;
import com.sun.mirror.type.InterfaceType;
import com.sun.mirror.type.PrimitiveType;
import com.sun.mirror.type.TypeMirror;
import com.sun.mirror.type.TypeVariable;
import com.sun.mirror.type.WildcardType;
import com.sun.mirror.util.SimpleTypeVisitor;

/**
 * TypeInfo is a helper type for determing types of fields and methods
 * 
 * @author tiwe
 * @version $Id$
 */
public class TypeHelper extends SimpleTypeVisitor {

    private FieldType fieldType;

    private String simpleName, fullName, packageName = "", keyTypeName;

    public TypeHelper(TypeMirror type) {
        type.accept(this);
        if (fullName == null) {
            fullName = type.toString();
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

    private void handleCollectionInterface(Iterator<TypeMirror> i) {
        TypeHelper valueInfo = new TypeHelper(i.next());
        handleCollection(valueInfo);
    }

    private void handleCollection(TypeHelper valueInfo) {
        fullName = valueInfo.getFullName();
        packageName = valueInfo.getPackageName();
        if (valueInfo.fieldType == FieldType.ENTITY) {
            fieldType = FieldType.ENTITYCOLLECTION;
        } else {
            fieldType = FieldType.SIMPLECOLLECTION;
        }
    }

    private void handleList(Iterator<TypeMirror> i) {
        TypeHelper valueInfo = new TypeHelper(i.next());
        handleList(valueInfo);
    }

    private void handleList(TypeHelper valueInfo) {
        fullName = valueInfo.getFullName();
        packageName = valueInfo.getPackageName();
        if (valueInfo.fieldType == FieldType.ENTITY) {
            fieldType = FieldType.ENTITYLIST;
        } else {
            fieldType = FieldType.SIMPLELIST;
        }
    }

    private void handleMapInterface(Iterator<TypeMirror> i) {
        TypeHelper keyInfo = new TypeHelper(i.next());
        TypeHelper valueInfo = new TypeHelper(i.next());
        handleMapInterface(keyInfo, valueInfo);
    }

    private void handleMapInterface(TypeHelper keyInfo, TypeHelper valueInfo) {
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

    @Override
    public void visitAnnotationType(AnnotationType arg0) {
        //            
    }

    private void visitArrayComponentType(TypeHelper valueInfo) {
        fullName = valueInfo.getFullName();
        packageName = valueInfo.getPackageName();
        if (valueInfo.fieldType == FieldType.ENTITY) {
            fieldType = FieldType.ENTITYCOLLECTION;
        } else {
            fieldType = FieldType.SIMPLECOLLECTION;
        }
    }

    @Override
    public void visitArrayType(ArrayType arg0) {
        TypeHelper valueInfo = new TypeHelper(arg0.getComponentType());
        visitArrayComponentType(valueInfo);
    }

    @Override
    public void visitClassType(ClassType arg0) {
        try {
            fullName = arg0.getDeclaration().getQualifiedName();
            packageName = arg0.getDeclaration().getPackage().getQualifiedName();

            if (fullName.equals(String.class.getName())) {
                fieldType = FieldType.STRING;

            } else if (fullName.equals(Boolean.class.getName())) {
                fieldType = FieldType.BOOLEAN;

            } else if (fullName.equals(Locale.class.getName())
                    || fullName.equals(Class.class.getName())
                    || fullName.equals(Object.class.getName())) {
                fieldType = FieldType.SIMPLE;

            } else if (isNumericSupported(fullName)
                    && Number.class.isAssignableFrom(Class.forName(fullName))) {
                fieldType = FieldType.NUMERIC;

            } else if (arg0.getDeclaration().getAnnotation(Literal.class) != null) {
                if (Comparable.class.isAssignableFrom(Class.forName(fullName))) {
                    fieldType = FieldType.COMPARABLE;
                } else {
                    fieldType = FieldType.SIMPLE;
                }

            } else if (isComparableSupported(fullName)
                    && Comparable.class.isAssignableFrom(Class
                            .forName(fullName))) {
                fieldType = FieldType.COMPARABLE;

            } else if (asSimpleType(fullName)) {
                fieldType = FieldType.SIMPLE;

            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private boolean isNumericSupported(String fullName) {
        return isComparableSupported(fullName);
    }

    private boolean isComparableSupported(String fullName) {
        return fullName.startsWith("java.") || fullName.startsWith("javax.") || fullName.startsWith("org.joda.time");
    }

    private boolean asSimpleType(String fullName) {
        return false;
    }

    public void visitEnumType(Class<?> type) {
        fieldType = FieldType.SIMPLE;
    }

    @Override
    public void visitEnumType(EnumType arg0) {
        fieldType = FieldType.SIMPLE;
    }

    @Override
    public void visitInterfaceType(InterfaceType arg0) {
        Iterator<TypeMirror> i = arg0.getActualTypeArguments().iterator();
        String typeName = arg0.getDeclaration().getQualifiedName();

        if (typeName.equals(java.util.Map.class.getName())) {
            handleMapInterface(i);

        } else if (typeName.equals(java.util.Collection.class.getName())
                || typeName.equals(java.util.Set.class.getName())
                || typeName.equals(java.util.SortedSet.class.getName())) {
            handleCollectionInterface(i);

        } else if (typeName.equals(java.util.List.class.getName())) {
            handleList(i);
        }
    }

    @Override
    public void visitPrimitiveType(PrimitiveType arg0) {
        Class<?> cl = null;
        switch (arg0.getKind()) {
        case BOOLEAN:
            cl = Boolean.class;
            break;
        case BYTE:
            cl = Byte.class;
            break;
        case CHAR:
            cl = Character.class;
            break;
        case DOUBLE:
            cl = Double.class;
            break;
        case FLOAT:
            cl = Float.class;
            break;
        case INT:
            cl = Integer.class;
            break;
        case LONG:
            cl = Long.class;
            break;
        case SHORT:
            cl = Short.class;
            break;
        }

        visitPrimitiveWrapperType(cl);
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

    @Override
    public void visitTypeVariable(TypeVariable arg0) {
        if (!arg0.getDeclaration().getBounds().isEmpty()) {
            TypeHelper lb = new TypeHelper(arg0.getDeclaration().getBounds()
                    .iterator().next());
            fullName = lb.getFullName();
            packageName = lb.getPackageName();
            simpleName = lb.getSimpleName();
            fieldType = lb.getFieldType();
        }
    }

    @Override
    public void visitWildcardType(WildcardType arg0) {
        if (!arg0.getUpperBounds().isEmpty()) {
            TypeHelper lb = new TypeHelper(arg0.getUpperBounds().iterator().next());
            fullName = lb.getFullName();
            packageName = lb.getPackageName();
            simpleName = lb.getSimpleName();
            fieldType = lb.getFieldType();
        }
    }

}
