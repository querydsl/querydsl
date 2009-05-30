/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.SimpleTypeVisitor6;

import com.mysema.query.annotations.Literal;
import com.mysema.query.codegen.FieldType;
import com.mysema.query.codegen.TypeModel;

/**
 * TypeInfo is a helper type for determing types of fields and methods
 * 
 * @author tiwe
 * @version $Id$
 */
class APTTypeModel extends SimpleTypeVisitor6<Void,Void> implements TypeModel {

    private static Map<TypeMirror,TypeModel> cache = new HashMap<TypeMirror,TypeModel>();
    
    public static TypeModel get(TypeMirror key){
        if (cache.containsKey(key)){
            return cache.get(key);
        }else{
            TypeModel value = new APTTypeModel(key);
            cache.put(key, value);
            return value;
        }
    }
    
    private FieldType fieldType;

    private String simpleName, fullName, packageName = "", keyTypeName;

    private APTTypeModel(TypeMirror type) {
        type.accept(this,null);
        if (fullName == null) {
            fullName = type.toString();
        }
        setDefaults();
    }

    @Override
    public FieldType getFieldType() {
        return fieldType;
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public String getKeyTypeName() {
        return keyTypeName;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }
    
    @Override
    public String getSimpleName() {
        return simpleName;
    }

    @Override
    public String getValueTypeName() {
        return fullName;
    }

    private void handleCollectionInterface(Iterator<TypeMirror> i) {
        APTTypeModel valueInfo = new APTTypeModel(i.next());
        handleCollection(valueInfo);
    }

    private void handleCollection(APTTypeModel valueInfo) {
        fullName = valueInfo.getFullName();
        packageName = valueInfo.getPackageName();
        if (valueInfo.fieldType == FieldType.ENTITY) {
            fieldType = FieldType.ENTITYCOLLECTION;
        } else {
            fieldType = FieldType.SIMPLECOLLECTION;
        }
    }

    private void handleList(Iterator<TypeMirror> i) {
        APTTypeModel valueInfo = new APTTypeModel(i.next());
        handleList(valueInfo);
    }

    private void handleList(APTTypeModel valueInfo) {
        fullName = valueInfo.getFullName();
        packageName = valueInfo.getPackageName();
        if (valueInfo.fieldType == FieldType.ENTITY) {
            fieldType = FieldType.ENTITYLIST;
        } else {
            fieldType = FieldType.SIMPLELIST;
        }
    }

    private void handleMapInterface(Iterator<TypeMirror> i) {
        APTTypeModel keyInfo = new APTTypeModel(i.next());
        APTTypeModel valueInfo = new APTTypeModel(i.next());
        handleMapInterface(keyInfo, valueInfo);
    }

    private void handleMapInterface(APTTypeModel keyInfo, APTTypeModel valueInfo) {
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

    private void visitArrayComponentType(APTTypeModel valueInfo) {
        fullName = valueInfo.getFullName();
        packageName = valueInfo.getPackageName();
        if (valueInfo.fieldType == FieldType.ENTITY) {
            fieldType = FieldType.ENTITYCOLLECTION;
        } else {
            fieldType = FieldType.SIMPLECOLLECTION;
        }
    }

    @Override
    public Void visitArray(ArrayType arg0, Void v) {
        APTTypeModel valueInfo = new APTTypeModel(arg0.getComponentType());
        visitArrayComponentType(valueInfo);
        return null;
    }

    @Override
    public Void visitDeclared(DeclaredType arg0, Void v) {
        try {
//            fullName = arg0.getDeclaration().getQualifiedName();
//            packageName = arg0.getDeclaration().getPackage().getQualifiedName();
            
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
                
            } else if (arg0.asElement().getAnnotation(Literal.class) != null) {
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
        return null;
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

//    @Override
//    public void visitEnumType(EnumType arg0) {
//        fieldType = FieldType.SIMPLE;
//    }
//
//    @Override
//    public void visitInterfaceType(InterfaceType arg0) {
//        Iterator<TypeMirror> i = arg0.getActualTypeArguments().iterator();
//        String typeName = arg0.getDeclaration().getQualifiedName();
//
//        if (typeName.equals(java.util.Map.class.getName())) {
//            handleMapInterface(i);
//
//        } else if (typeName.equals(java.util.Collection.class.getName())
//                || typeName.equals(java.util.Set.class.getName())
//                || typeName.equals(java.util.SortedSet.class.getName())) {
//            handleCollectionInterface(i);
//
//        } else if (typeName.equals(java.util.List.class.getName())) {
//            handleList(i);
//        }
//    }

    @Override
    public Void visitPrimitive(PrimitiveType arg0, Void v) {
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
        return null;
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
    public Void visitTypeVariable(TypeVariable arg0, Void v) {
        if (arg0.getUpperBound() != null) {
            APTTypeModel lb = new APTTypeModel(arg0.getUpperBound());
            fullName = lb.getFullName();
            packageName = lb.getPackageName();
            simpleName = lb.getSimpleName();
            fieldType = lb.getFieldType();
        }
        return null;
    }

    @Override
    public Void visitWildcard(WildcardType arg0, Void v) {
        if (arg0.getExtendsBound() != null) {
            APTTypeModel lb = new APTTypeModel(arg0.getExtendsBound());
            fullName = lb.getFullName();
            packageName = lb.getPackageName();
            simpleName = lb.getSimpleName();
            fieldType = lb.getFieldType();
        }
        return null;
    }

}
