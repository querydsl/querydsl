/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt.general;

import java.util.Iterator;
import java.util.Locale;

import com.mysema.query.apt.model.Field;
import com.sun.mirror.type.*;
import com.sun.mirror.util.SimpleTypeVisitor;

/**
 * TypeInfo represents a single type
 * 
 * @author tiwe
 * @version $Id$
 */
public class TypeHelper {

    private Field.Type fieldType;

    private String simpleName, fullName, packageName = "", keyTypeName;

    private final TypeInfoVisitor visitor = new TypeInfoVisitor();

    public TypeHelper(TypeMirror type) {
        type.accept(visitor);
        if (fieldType == null) {
            fieldType = Field.Type.ENTITY;
        }
        if (fullName == null) {
            fullName = type.toString();
        }
        if (simpleName == null) {
            simpleName = fullName.substring(fullName.lastIndexOf('.') + 1);
        }
    }

    public Field.Type getFieldType() {
        return fieldType;
    }

    public String getFullName() {
        return fullName;
    }
    
    public String getKeyTypeName() {
        return keyTypeName;
    }

    public String getPackageName(){
        return packageName;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public String getValueTypeName() {
        return fullName;
    }

    public String toString() {
        return fullName;
    }

    private class TypeInfoVisitor extends SimpleTypeVisitor {
        
        @Override
        public void visitAnnotationType(AnnotationType arg0) {
            //            
        }

        @Override
        public void visitArrayType(ArrayType arg0) {
            TypeHelper valueInfo = new TypeHelper(arg0.getComponentType());
            fullName = valueInfo.getFullName();
            packageName = valueInfo.getPackageName();
            if (valueInfo.fieldType == Field.Type.ENTITY) {
                fieldType = Field.Type.ENTITYCOLLECTION;
            } else {
                fieldType = Field.Type.SIMPLECOLLECTION;
            }
        }

        @Override
        public void visitClassType(ClassType arg0){
            try {                               
                fullName = arg0.getDeclaration().getQualifiedName();
                packageName = arg0.getDeclaration().getPackage().getQualifiedName();                    
                if (fullName.equals(String.class.getName())) {
                    fieldType = Field.Type.STRING;
                } else if (fullName.equals(Boolean.class.getName())) {
                    fieldType = Field.Type.BOOLEAN;
                } else if (fullName.equals(Locale.class.getName()) || fullName.equals(Class.class.getName())) {
                    fieldType = Field.Type.SIMPLE;
                } else if (fullName.startsWith("java") && Number.class.isAssignableFrom(Class.forName(fullName))) {
                    fieldType = Field.Type.NUMERIC;
                } else if (fullName.startsWith("java") && Comparable.class.isAssignableFrom(Class.forName(fullName))) {
                    fieldType = Field.Type.COMPARABLE;
                }    
            }catch(Exception e){
                throw new RuntimeException("error", e);
            }            
        }

        @Override
        public void visitEnumType(EnumType arg0) {
            fieldType = Field.Type.SIMPLE;
        }

        @Override
        public void visitInterfaceType(InterfaceType arg0) {
            Iterator<TypeMirror> i = arg0.getActualTypeArguments().iterator();
            String typeName = arg0.getDeclaration().getQualifiedName();

            if (typeName.equals(java.util.Map.class.getName())) {
                TypeHelper keyInfo = new TypeHelper(i.next());
                keyTypeName = keyInfo.getFullName();
                TypeHelper valueInfo = new TypeHelper(i.next());
                fullName = valueInfo.getFullName();
                packageName = valueInfo.getPackageName();
                if (valueInfo.fieldType == Field.Type.ENTITY) {
                    fieldType = Field.Type.ENTITYMAP;
                } else {
                    fieldType = Field.Type.SIMPLEMAP;
                }

            } else if (typeName.equals(java.util.Collection.class.getName())
                    || typeName.equals(java.util.Set.class.getName()) 
                    || typeName.equals(java.util.SortedSet.class.getName())) {
                TypeHelper valueInfo = new TypeHelper(i.next());
                fullName = valueInfo.getFullName();
                packageName = valueInfo.getPackageName();
                if (valueInfo.fieldType == Field.Type.ENTITY) {
                    fieldType = Field.Type.ENTITYCOLLECTION;
                } else {
                    fieldType = Field.Type.SIMPLECOLLECTION;
                }

            } else if (typeName.equals(java.util.List.class.getName())) {
                TypeHelper valueInfo = new TypeHelper(i.next());
                fullName = valueInfo.getFullName();
                packageName = valueInfo.getPackageName();
                if (valueInfo.fieldType == Field.Type.ENTITY) {
                    fieldType = Field.Type.ENTITYLIST;
                } else {
                    fieldType = Field.Type.SIMPLELIST;
                }
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
            if (cl.equals(Boolean.class)) {
                fieldType = Field.Type.BOOLEAN;
            }else if (Number.class.isAssignableFrom(cl)){
                fieldType = Field.Type.NUMERIC;
            }else if (Comparable.class.isAssignableFrom(cl)){
                fieldType = Field.Type.COMPARABLE;
            } else {                               
                fieldType = Field.Type.SIMPLE;
            }
            fullName = cl.getName();
            simpleName = cl.getSimpleName();

        }
        
        @Override
        public void visitTypeVariable(TypeVariable arg0){
            if (!arg0.getDeclaration().getBounds().isEmpty()){
                TypeHelper lb = new TypeHelper(arg0.getDeclaration().getBounds().iterator().next());
                fullName = lb.getFullName();
                packageName = lb.getPackageName();     
                simpleName = lb.getSimpleName();
            }
        }
        
        @Override
        public void visitWildcardType(WildcardType arg0){
            if (!arg0.getUpperBounds().isEmpty()){
                TypeHelper lb = new TypeHelper(arg0.getUpperBounds().iterator().next());
                fullName = lb.getFullName();
                packageName = lb.getPackageName();     
                simpleName = lb.getSimpleName();
            }
        }

    }

}
