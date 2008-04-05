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
public class Type implements Comparable<Type>{
        
    private final Set<Field> booleanFields = new TreeSet<Field>();
    
    // not sorted
    private final Set<Constructor> constructors = new HashSet<Constructor>();
    
    private final Set<Field> entityCollections = new TreeSet<Field>();
    
    private final Set<Field> entityLists = new TreeSet<Field>();
    
    private final Set<Field> entityMaps = new TreeSet<Field>();
    
    private final Set<Field> entityFields = new TreeSet<Field>();

    private final Set<Field> simpleCollections = new TreeSet<Field>();
    
    private final Set<Field> simpleLists = new TreeSet<Field>();
    
    private final Set<Field> simpleMaps = new TreeSet<Field>();
    
    private final Set<Field> simpleFields = new TreeSet<Field>();
    
    private final String simpleName, name;

    private final Set<Field> stringFields = new TreeSet<Field>();
    
    private final String superType;
    
    public Type(ClassDeclaration d) {
        this.simpleName = d.getSimpleName();
        this.name = d.getQualifiedName();
        this.superType = d.getSuperclass().getDeclaration().getQualifiedName();
    }
    
    public Type(String superType, String name, String simpleName){
        this.superType = superType;
        this.name = name;
        this.simpleName = simpleName;
    }
    
    public void addConstructor(ConstructorDeclaration co) {
        addConstructor(new Constructor(co));
    }
    
    public void addConstructor(Constructor co){
        constructors.add(co);     
    }

    public void addField(FieldDeclaration field){
        addField(new Field(field));
    }
    
    public void addField(Field fieldDecl){
        switch(fieldDecl.getFieldType()){
        case BOOLEAN : booleanFields.add(fieldDecl); break;
        case STRING : stringFields.add(fieldDecl); break;
        case SIMPLE : simpleFields.add(fieldDecl); break;
        case ENTITY : entityFields.add(fieldDecl); break;         
        case ENTITYCOLLECTION : entityCollections.add(fieldDecl); break;
        case SIMPLECOLLECTION : simpleCollections.add(fieldDecl); break;
        case ENTITYLIST : entityLists.add(fieldDecl); break;
        case SIMPLELIST : simpleLists.add(fieldDecl); break;
        case ENTITYMAP : entityMaps.add(fieldDecl); break;
        case SIMPLEMAP : simpleMaps.add(fieldDecl); break;
      }
    }
    
    public int compareTo(Type o) {
        return simpleName.compareTo(o.simpleName);
    }
    
    public boolean equals(Object o){
        return o instanceof Type && simpleName.equals(((Type)o).simpleName);
    }
    
    public Collection<Field> getBooleanFields() {
        return booleanFields;
    }

    public Collection<Constructor> getConstructors(){
        return constructors;
    }
    
    public Collection<Field> getEntityCollections() {
        return entityCollections;
    }
    
    public Collection<Field> getEntityLists() {
        return entityLists;
    }
    
    public Collection<Field> getEntityMaps() {
        return entityMaps;
    }
        
    public Collection<Field> getEntityFields() {
        return entityFields;
    }
    
    public String getName() {
        return name;
    }

    public Collection<Field> getSimpleCollections() {
        return simpleCollections;
    }
    
    public Collection<Field> getSimpleLists() {
        return simpleLists;
    }
    
    public Collection<Field> getSimpleMaps() {
        return simpleMaps;
    }

    public Collection<Field> getSimpleFields() {
        return simpleFields;
    }
    
    public String getSimpleName() {
        return simpleName;
    }
    
    public Collection<Field> getStringFields() {
        return stringFields;
    }

    public String getSupertypeName(){
        return superType;
    }
    
    public int hashCode(){
        return name.hashCode();
    }
    
    public void include(Type decl) {
        booleanFields.addAll(decl.booleanFields);
        entityCollections.addAll(decl.entityCollections);
        simpleCollections.addAll(decl.simpleCollections);
        entityLists.addAll(decl.entityLists);
        simpleLists.addAll(decl.simpleLists);
        entityMaps.addAll(decl.entityMaps);
        simpleMaps.addAll(decl.simpleMaps);
        entityFields.addAll(decl.entityFields);
        simpleFields.addAll(decl.simpleFields);
        stringFields.addAll(decl.stringFields);
    }
    
}