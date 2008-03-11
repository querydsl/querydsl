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

/**
 * 
 * TypeDecl provides
 *
 * @author tiwe
 * @version $Id$
 *
 */
public class TypeDecl implements Comparable<TypeDecl>{
        
    private final Set<FieldDecl> booleanFields = new TreeSet<FieldDecl>();
    
    // not sorted
    private final Set<ConstructorDecl> constructors = new HashSet<ConstructorDecl>();
    
    private final Set<FieldDecl> entityCollections = new TreeSet<FieldDecl>();
    
    private final Set<FieldDecl> entityMaps = new TreeSet<FieldDecl>();
    
    private final Set<FieldDecl> entityFields = new TreeSet<FieldDecl>();

    private final Set<FieldDecl> simpleCollections = new TreeSet<FieldDecl>();
    
    private final Set<FieldDecl> simpleMaps = new TreeSet<FieldDecl>();
    
    private final Set<FieldDecl> simpleFields = new TreeSet<FieldDecl>();
    
    private final String simpleName, name;

    private final Set<FieldDecl> stringFields = new TreeSet<FieldDecl>();
    
    private final String superType;
    
    public TypeDecl(ClassDeclaration d) {
        this.simpleName = d.getSimpleName();
        this.name = d.getQualifiedName();
        this.superType = d.getSuperclass().getDeclaration().getQualifiedName();
    }
    
    public TypeDecl(String superType, String name, String simpleName){
        this.superType = superType;
        this.name = name;
        this.simpleName = simpleName;
    }
    
    public void addConstructor(ConstructorDeclaration co) {
        addConstructor(new ConstructorDecl(co));
    }
    
    public void addConstructor(ConstructorDecl co){
        constructors.add(co);     
    }

    public void addField(FieldDeclaration field){
        addField(new FieldDecl(field));
    }
    
    public void addField(FieldDecl fieldDecl){
        switch(fieldDecl.getFieldType()){
        case BOOLEAN : booleanFields.add(fieldDecl); break;
        case STRING : stringFields.add(fieldDecl); break;
        case SIMPLE : simpleFields.add(fieldDecl); break;
        case ENTITY : entityFields.add(fieldDecl); break;         
        case ENTITYCOLLECTION : entityCollections.add(fieldDecl); break;
        case SIMPLECOLLECTION : simpleCollections.add(fieldDecl); break;
        case ENTITYMAP : entityMaps.add(fieldDecl); break;
        case SIMPLEMAP : simpleMaps.add(fieldDecl); break;
      }
    }
    
    public int compareTo(TypeDecl o) {
        return simpleName.compareTo(o.simpleName);
    }
    
    public boolean equals(Object o){
        return o instanceof TypeDecl && simpleName.equals(((TypeDecl)o).simpleName);
    }
    
    public Collection<FieldDecl> getBooleanFields() {
        return booleanFields;
    }

    public Collection<ConstructorDecl> getConstructors(){
        return constructors;
    }
    
    public Collection<FieldDecl> getEntityCollections() {
        return entityCollections;
    }
    
    public Collection<FieldDecl> getEntityMaps() {
        return entityMaps;
    }
        
    public Collection<FieldDecl> getEntityFields() {
        return entityFields;
    }
    
    public String getName() {
        return name;
    }

    public Collection<FieldDecl> getSimpleCollections() {
        return simpleCollections;
    }
    
    public Collection<FieldDecl> getSimpleMaps() {
        return simpleMaps;
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
    
    public int hashCode(){
        return name.hashCode();
    }
    
    public void include(TypeDecl decl) {
        booleanFields.addAll(decl.booleanFields);
        entityCollections.addAll(decl.entityCollections);
        simpleCollections.addAll(decl.simpleCollections);
        entityMaps.addAll(decl.entityMaps);
        simpleMaps.addAll(decl.simpleMaps);
        entityFields.addAll(decl.entityFields);
        simpleFields.addAll(decl.simpleFields);
        stringFields.addAll(decl.stringFields);
    }
    
}