/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt;

import java.util.*;

import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.ConstructorDeclaration;
import com.sun.mirror.declaration.FieldDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;

/**
 * 
 * TypeDecl provides
 *
 * @author tiwe
 * @version $Id$
 *
 */
public class TypeDecl implements Comparable<TypeDecl>{
    
    private static Map<String,Class<?>> primitiveTypes = new HashMap<String,Class<?>>();
    
    static {
        primitiveTypes.put("byte", Byte.class);
        primitiveTypes.put("short", Short.class);
        primitiveTypes.put("int", Integer.class);
        primitiveTypes.put("long", Long.class);
        primitiveTypes.put("float", Float.class);
        primitiveTypes.put("double", Double.class);
        primitiveTypes.put("char", Character.class);
        primitiveTypes.put("boolean", Boolean.class);        
    }
    
    private final Set<FieldDecl> booleanFields = new TreeSet<FieldDecl>();
    
    private final Set<FieldDecl> collectionFields = new TreeSet<FieldDecl>();
    
    // not sorted
    private final Set<ConstructorDecl> constructors = new HashSet<ConstructorDecl>();

    private final Set<FieldDecl> entityFields = new TreeSet<FieldDecl>();
    
    private final Set<FieldDecl> simpleFields = new TreeSet<FieldDecl>();
    
    private final String simpleName, name;

    private final Set<FieldDecl> stringFields = new TreeSet<FieldDecl>();
    
    private final String superType;
    
    public TypeDecl(ClassDeclaration d) {
        this.simpleName = d.getSimpleName();
        this.name = d.getQualifiedName();
        this.superType = d.getSuperclass().getDeclaration().getQualifiedName();
    }
    
    public void addConstructor(ConstructorDeclaration co) {
        constructors.add(new ConstructorDecl(co));          
    }

    public void addField(FieldDeclaration field){
        FieldDecl fieldDecl = new FieldDecl(field);
        switch(fieldDecl.fieldType){
          case BOOLEAN : booleanFields.add(fieldDecl); break;
          case STRING : stringFields.add(fieldDecl); break;
          case SIMPLE : simpleFields.add(fieldDecl); break;
          case ENTITY : entityFields.add(fieldDecl); break;                   
          case COLLECTION : collectionFields.add(fieldDecl); break;
        }
    }
    
    public int compareTo(TypeDecl o) {
        return simpleName.compareTo(o.simpleName);
    }

    public Collection<FieldDecl> getBooleanFields() {
        return booleanFields;
    }
        
    public Collection<FieldDecl> getCollectionFields() {
        return collectionFields;
    }
    
    public Collection<ConstructorDecl> getConstructors(){
        return constructors;
    }

    public Collection<FieldDecl> getEntityFields() {
        return entityFields;
    }

    public String getName() {
        return name;
    }
    
    public Collection<FieldDecl> getSimpleFields() {
        return simpleFields;
    }
    
    public String getSimpleName() {
        return simpleName;
    }

    public Collection<FieldDecl> getStringFields() {
        return stringFields;
    }
    
    public String getSupertypeName(){
        return superType;
    }
    
    public void include(TypeDecl decl) {
        booleanFields.addAll(decl.booleanFields);
        collectionFields.addAll(decl.collectionFields);
        entityFields.addAll(decl.entityFields);
        simpleFields.addAll(decl.simpleFields);
        stringFields.addAll(decl.stringFields);
    }
    
    public static class ConstructorDecl{
        private Collection<ParameterDecl> parameters;
        public ConstructorDecl(ConstructorDeclaration co) {
            parameters = new ArrayList<ParameterDecl>(co.getParameters().size());
            for (ParameterDeclaration pa : co.getParameters()){
                parameters.add(new ParameterDecl(pa));
            }
        }
        
        public Collection<ParameterDecl> getParameters(){
            return parameters;
        }
        
    }
    
    public static class FieldDecl implements Comparable<FieldDecl>{
        private final FieldType fieldType;
        private String name, typeName, simpleTypeName;
        public FieldDecl(FieldDeclaration field) {
            name = field.getSimpleName();     
            String type = field.getType().toString();
            if (!primitiveTypes.containsKey(type)){
                typeName = field.getType().toString();                
                if (typeName.contains("<")){
                    fieldType = FieldType.COLLECTION;
                    typeName = typeName.substring(typeName.lastIndexOf('<')+1, typeName.length()-1);
                }else{
                    fieldType = forType(typeName);
                }
                simpleTypeName = typeName.substring(typeName.lastIndexOf('.')+1);
                
            }else{
                Class<?> cl = primitiveTypes.get(type);
                typeName = cl.getName();
                simpleTypeName = cl.getSimpleName();
                fieldType = forType(cl.getName());             
            }
            
        }
        
        public int compareTo(FieldDecl o) {
            return name.compareTo(o.name);
        }
        
        private FieldType forType(String cl){
            if (cl.equals(Boolean.class.getName())) return FieldType.BOOLEAN;
            else if (cl.equals(String.class.getName())) return FieldType.STRING;
            else if (cl.startsWith("java")) return FieldType.SIMPLE;
            else return FieldType.ENTITY;
        }
        
        public FieldType getFieldType(){
            return fieldType;
        }
        
        public String getName(){
            return name;
        }
        
        public String getSimpleTypeName(){
            return simpleTypeName;
        }

        public String getTypeName(){
            return typeName;
        }
        
    }
    
    public enum FieldType{
        BOOLEAN, COLLECTION,ENTITY,SIMPLE,STRING
    }
    
    public static class ParameterDecl implements Comparable<ParameterDecl>{
        private final String name, typeName;
        public ParameterDecl(ParameterDeclaration pa) {
            name = pa.getSimpleName();
            String type = pa.getType().toString();
            if (primitiveTypes.containsKey(type)){
                typeName = primitiveTypes.get(type).getSimpleName();
            }else{
                typeName = pa.getType().toString();    
            }            
        }
        
        public int compareTo(ParameterDecl o) {
            return name.compareTo(o.name);
        }
        public String getName(){
            return name;
        }

        public String getTypeName(){
            return typeName;
        }
    }



}