/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt.util;

import java.util.Iterator;

import com.mysema.query.apt.FieldType;
import com.sun.mirror.type.*;
import com.sun.mirror.util.SimpleTypeVisitor;

/**
 * TypeVisitorUtil provides
 *
 * @author tiwe
 * @version $Id$
 */
public class TypeInfo {

    private FieldType fieldType;
    
    private String simpleName, fullName, keyTypeName;
    
    private final TypeInfoVisitor visitor = new TypeInfoVisitor();
    
    public TypeInfo(TypeMirror type){
        type.accept(visitor);
        if (fieldType == null){
            fieldType = FieldType.ENTITY;
        }
        if (fullName == null){
            fullName = type.toString();
        }
        if (simpleName == null){
            simpleName = fullName.substring(fullName.lastIndexOf('.')+1);    
        }
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

    public String getSimpleName() {
        return simpleName;
    }

    public String getValueTypeName() {
        return fullName;
    }

    public String toString(){
        return fullName;
    }
    
    private class TypeInfoVisitor extends SimpleTypeVisitor{
        public void visitAnnotationType(AnnotationType arg0) {
            //            
        }

        public void visitArrayType(ArrayType arg0) {            
            TypeInfo valueInfo = new TypeInfo(arg0.getComponentType());
            fullName = valueInfo.getFullName();
            if (valueInfo.fieldType == FieldType.ENTITY){
                fieldType = FieldType.ENTITYCOLLECTION;
            }else{
                fieldType = FieldType.SIMPLECOLLECTION;
            }
        }

        public void visitClassType(ClassType arg0) {
            fullName = arg0.toString();
            if (fullName.equals(String.class.getName())){
                fieldType = FieldType.STRING;
            }else if (fullName.equals(Boolean.class.getName())){
                fieldType = FieldType.BOOLEAN;
            }else if (fullName.startsWith("java")){
                fieldType = FieldType.SIMPLE;
            }
        }

        public void visitEnumType(EnumType arg0) {
            fieldType = FieldType.SIMPLE;
        }

        public void visitInterfaceType(InterfaceType arg0) {
            Iterator<TypeMirror> i = arg0.getActualTypeArguments().iterator();
            // map
            if (arg0.getActualTypeArguments().size() == 2){                
                TypeInfo keyInfo = new TypeInfo(i.next());
                keyTypeName = keyInfo.getFullName();
                TypeInfo valueInfo = new TypeInfo(i.next());
                fullName = valueInfo.getFullName();
                if (valueInfo.fieldType == FieldType.ENTITY){
                    fieldType = FieldType.ENTITYMAP;
                }else{
                    fieldType = FieldType.SIMPLEMAP;
                }
                
            // collection
            }else{
                TypeInfo valueInfo = new TypeInfo(i.next());
                fullName = valueInfo.getFullName();
                if (valueInfo.fieldType == FieldType.ENTITY){
                    fieldType = FieldType.ENTITYCOLLECTION;
                }else{
                    fieldType = FieldType.SIMPLECOLLECTION;
                }
            }
        }

        public void visitPrimitiveType(PrimitiveType arg0) {
            Class<?> cl = null;
            switch (arg0.getKind()){
            case BOOLEAN:  cl = Boolean.class; break;
            case BYTE:     cl = Byte.class; break;
            case CHAR:     cl = Character.class; break;
            case DOUBLE:   cl = Double.class; break;
            case FLOAT:    cl = Float.class; break;
            case INT:      cl = Integer.class; break;
            case LONG:     cl = Long.class; break;
            case SHORT:    cl = Short.class; break;
            }
            if (cl.equals(Boolean.class)){
                fieldType = FieldType.BOOLEAN;
            }else{
                fieldType = FieldType.SIMPLE;
            }
            fullName = cl.getName();
            simpleName = cl.getSimpleName();
            
        }

    }
    
}
