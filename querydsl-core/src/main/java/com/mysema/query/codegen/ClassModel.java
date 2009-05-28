/*

 * 
 * 
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
public class ClassModel implements Comparable<ClassModel> {

    private Set<FieldModel> booleanFields = new TreeSet<FieldModel>();

    private Set<FieldModel> comparableFields = new TreeSet<FieldModel>();

    private Set<ConstructorModel> constructors = new HashSet<ConstructorModel>();

    private Set<FieldModel> entityCollections = new TreeSet<FieldModel>();

    private Set<FieldModel> entityFields = new TreeSet<FieldModel>();

    private Set<FieldModel> entityLists = new TreeSet<FieldModel>();

    private Set<FieldModel> entityMaps = new TreeSet<FieldModel>();

    private Set<FieldModel> numericFields = new TreeSet<FieldModel>();

    private Set<FieldModel> simpleCollections = new TreeSet<FieldModel>();

    private Set<FieldModel> simpleFields = new TreeSet<FieldModel>();

    private Set<FieldModel> simpleLists = new TreeSet<FieldModel>();

    private Set<FieldModel> simpleMaps = new TreeSet<FieldModel>();

    private String simpleName, uncapSimpleName, name, packageName;

    private int escapeSuffix = 1;

    private Set<FieldModel> stringFields = new TreeSet<FieldModel>();

    private String superType;

    public ClassModel(String superType, String packageName, String name,
            String simpleName) {
        this.superType = superType;
        this.packageName = packageName;
        this.name = name;
        this.simpleName = simpleName;
        this.uncapSimpleName = StringUtils.uncapitalize(simpleName);
    }

    public void addConstructor(ConstructorModel co) {
        constructors.add(co);
    }

    public void addField(FieldModel fieldDecl) {
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

    private FieldModel validateField(FieldModel field) {
        if (field.getName().equals(this.uncapSimpleName)) {
            uncapSimpleName = StringUtils.uncapitalize(simpleName)+ (escapeSuffix++);
        }
        return field;
    }

    public int compareTo(ClassModel o) {
        return simpleName.compareTo(o.simpleName);
    }

    public boolean equals(Object o) {
        return o instanceof ClassModel && simpleName.equals(((ClassModel) o).simpleName);
    }

    public Collection<FieldModel> getBooleanFields() {
        return booleanFields;
    }

    public Collection<FieldModel> getComparableFields() {
        return comparableFields;
    }

    public Collection<ConstructorModel> getConstructors() {
        return constructors;
    }

    public Collection<FieldModel> getEntityCollections() {
        return entityCollections;
    }

    public Collection<FieldModel> getEntityFields() {
        return entityFields;
    }

    public Collection<FieldModel> getEntityLists() {
        return entityLists;
    }

    public Collection<FieldModel> getEntityMaps() {
        return entityMaps;
    }

    public String getName() {
        return name;
    }

    public Collection<FieldModel> getNumericFields() {
        return numericFields;
    }

    public String getPackageName() {
        return packageName;
    }

    public Collection<FieldModel> getSimpleCollections() {
        return simpleCollections;
    }

    public Collection<FieldModel> getSimpleFields() {
        return simpleFields;
    }

    public Collection<FieldModel> getSimpleLists() {
        return simpleLists;
    }

    public Collection<FieldModel> getSimpleMaps() {
        return simpleMaps;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public String getUncapSimpleName() {
        return uncapSimpleName;
    }

    public Collection<FieldModel> getStringFields() {
        return stringFields;
    }

    public String getSupertypeName() {
        return superType;
    }

    public int hashCode() {
        return name.hashCode();
    }

    public void include(ClassModel decl) {
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

    private void addAll(Set<FieldModel> target, Set<FieldModel> source) {
        for (FieldModel field : source) {
            target.add(validateField(field));
        }
    }

}