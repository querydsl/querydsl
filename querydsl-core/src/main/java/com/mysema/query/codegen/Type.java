/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

/**
 * Type represents a query domain type.
 * 
 * @author tiwe
 * @version $Id$
 */
public class Type implements Comparable<Type> {

    private Set<Field> booleanFields = new TreeSet<Field>();

    private Set<Field> comparableFields = new TreeSet<Field>();

    private Set<Constructor> constructors = new HashSet<Constructor>();

    private Set<Field> entityCollections = new TreeSet<Field>();

    private Set<Field> entityFields = new TreeSet<Field>();

    private Set<Field> entityLists = new TreeSet<Field>();

    private Set<Field> entityMaps = new TreeSet<Field>();

    private Set<Field> numericFields = new TreeSet<Field>();

    private Set<Field> simpleCollections = new TreeSet<Field>();

    private Set<Field> simpleFields = new TreeSet<Field>();

    private Set<Field> simpleLists = new TreeSet<Field>();

    private Set<Field> simpleMaps = new TreeSet<Field>();

    private String simpleName, uncapSimpleName, name, packageName;

    private int escapeSuffix = 1;

    private Set<Field> stringFields = new TreeSet<Field>();

    private String superType;

    public Type(String superType, String packageName, String name,
            String simpleName) {
        this.superType = superType;
        this.packageName = packageName;
        this.name = name;
        this.simpleName = simpleName;
        this.uncapSimpleName = StringUtils.uncapitalize(simpleName);
    }

    public void addConstructor(Constructor co) {
        constructors.add(co);
    }

    public void addField(Field fieldDecl) {
        validateField(fieldDecl);
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
        case NUMERIC:
            numericFields.add(fieldDecl);
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

    private Field validateField(Field field) {
        if (field.getName().equals(this.uncapSimpleName)) {
            uncapSimpleName = StringUtils.uncapitalize(simpleName)
                    + (escapeSuffix++);
        }
        return field;
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

    public Collection<Field> getComparableFields() {
        return comparableFields;
    }

    public Collection<Constructor> getConstructors() {
        return constructors;
    }

    public Collection<Field> getEntityCollections() {
        return entityCollections;
    }

    public Collection<Field> getEntityFields() {
        return entityFields;
    }

    public Collection<Field> getEntityLists() {
        return entityLists;
    }

    public Collection<Field> getEntityMaps() {
        return entityMaps;
    }

    public String getName() {
        return name;
    }

    public Collection<Field> getNumericFields() {
        return numericFields;
    }

    public String getPackageName() {
        return packageName;
    }

    public Collection<Field> getSimpleCollections() {
        return simpleCollections;
    }

    public Collection<Field> getSimpleFields() {
        return simpleFields;
    }

    public Collection<Field> getSimpleLists() {
        return simpleLists;
    }

    public Collection<Field> getSimpleMaps() {
        return simpleMaps;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public String getUncapSimpleName() {
        return uncapSimpleName;
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
        addAll(booleanFields, decl.booleanFields);
        addAll(entityCollections, decl.entityCollections);
        addAll(entityFields, decl.entityFields);
        addAll(entityLists, decl.entityLists);
        addAll(entityMaps, decl.entityMaps);
        addAll(comparableFields, decl.comparableFields);
        addAll(numericFields, decl.numericFields);
        addAll(simpleCollections, decl.simpleCollections);
        addAll(simpleFields, decl.simpleFields);
        addAll(simpleLists, decl.simpleLists);
        addAll(simpleMaps, decl.simpleMaps);
        addAll(stringFields, decl.stringFields);
    }

    private void addAll(Set<Field> target, Set<Field> source) {
        for (Field field : source) {
            target.add(validateField(field));
        }
    }

}