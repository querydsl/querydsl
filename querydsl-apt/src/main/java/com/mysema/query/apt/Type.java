/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import com.sun.mirror.declaration.ClassDeclaration;

/**
 * TypeDecl represents a query domain type.
 * 
 * @author tiwe
 * @version $Id$
 */
public class Type implements Comparable<Type> {

    Set<Field> booleanFields = new TreeSet<Field>();

    Set<Constructor> constructors = new HashSet<Constructor>();

    Set<Field> entityCollections = new TreeSet<Field>();

    Set<Field> entityLists = new TreeSet<Field>();

    Set<Field> entityMaps = new TreeSet<Field>();

    Set<Field> entityFields = new TreeSet<Field>();

    Set<Field> simpleCollections = new TreeSet<Field>();

    Set<Field> simpleLists = new TreeSet<Field>();

    Set<Field> simpleMaps = new TreeSet<Field>();

    Set<Field> simpleFields = new TreeSet<Field>();

    Set<Field> comparableFields = new TreeSet<Field>();

    String simpleName, name;

    Set<Field> stringFields = new TreeSet<Field>();

    String superType;

    public Type(ClassDeclaration d) {
        this.simpleName = d.getSimpleName();
        this.name = d.getQualifiedName();
        this.superType = d.getSuperclass().getDeclaration().getQualifiedName();
    }

    public Type(String superType, String name, String simpleName) {
        this.superType = superType;
        this.name = name;
        this.simpleName = simpleName;
    }

    public void addConstructor(Constructor co) {
        constructors.add(co);
    }

    public void addField(Field fieldDecl) {
        switch (fieldDecl.getFieldType()) {
        case BOOLEAN:
            booleanFields.add(fieldDecl);
            break;
        case STRING:
            stringFields.add(fieldDecl);
            break;
        case SIMPLE:
            simpleFields.add(fieldDecl);
            break;
        case COMPARABLE:
            comparableFields.add(fieldDecl);
            break;
        case ENTITY:
            entityFields.add(fieldDecl);
            break;
        case ENTITYCOLLECTION:
            entityCollections.add(fieldDecl);
            break;
        case SIMPLECOLLECTION:
            simpleCollections.add(fieldDecl);
            break;
        case ENTITYLIST:
            entityLists.add(fieldDecl);
            break;
        case SIMPLELIST:
            simpleLists.add(fieldDecl);
            break;
        case ENTITYMAP:
            entityMaps.add(fieldDecl);
            break;
        case SIMPLEMAP:
            simpleMaps.add(fieldDecl);
            break;
        }
    }

    public int compareTo(Type o) {
        return simpleName.compareTo(o.simpleName);
    }

    public boolean equals(Object o) {
        return o instanceof Type && simpleName.equals(((Type) o).simpleName);
    }

    public Collection<Field> getBooleanFields() {
        return booleanFields;
    }

    public Collection<Constructor> getConstructors() {
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

    public Collection<Field> getComparableFields() {
        return comparableFields;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public Collection<Field> getStringFields() {
        return stringFields;
    }

    public String getSupertypeName() {
        return superType;
    }

    public int hashCode() {
        return name.hashCode();
    }

    public void include(Type decl) {
        booleanFields.addAll(decl.booleanFields);
        entityCollections.addAll(decl.entityCollections);
        entityFields.addAll(decl.entityFields);
        entityLists.addAll(decl.entityLists);
        entityMaps.addAll(decl.entityMaps);
        comparableFields.addAll(decl.comparableFields);
        simpleCollections.addAll(decl.simpleCollections);
        simpleFields.addAll(decl.simpleFields);
        simpleLists.addAll(decl.simpleLists);
        simpleMaps.addAll(decl.simpleMaps);
        stringFields.addAll(decl.stringFields);
    }

}