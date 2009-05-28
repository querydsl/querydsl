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
 * ClassModel represents a query domain type.
 * 
 * @author tiwe
 * @version $Id$
 */
public class ClassModel implements Comparable<ClassModel> {

    private final Set<FieldModel> booleanFields = new TreeSet<FieldModel>();

    private final Set<FieldModel> comparableFields = new TreeSet<FieldModel>();

    private final Set<ConstructorModel> constructors = new HashSet<ConstructorModel>();

    private final Set<FieldModel> entityCollections = new TreeSet<FieldModel>();

    private final Set<FieldModel> entityFields = new TreeSet<FieldModel>();

    private final Set<FieldModel> entityLists = new TreeSet<FieldModel>();

    private final Set<FieldModel> entityMaps = new TreeSet<FieldModel>();

    private final Set<FieldModel> numericFields = new TreeSet<FieldModel>();

    private final Set<FieldModel> simpleCollections = new TreeSet<FieldModel>();

    private final Set<FieldModel> simpleFields = new TreeSet<FieldModel>();

    private final Set<FieldModel> simpleLists = new TreeSet<FieldModel>();

    private final Set<FieldModel> simpleMaps = new TreeSet<FieldModel>();

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

    public void addField(FieldModel field) {
        validateField(field);
        switch (field.getFieldType()) {
        case BOOLEAN:
            booleanFields.add(field);
            break;
        case STRING:
            stringFields.add(field);
            break;
        case SIMPLE:
            simpleFields.add(field);
            break;
        case COMPARABLE:
            comparableFields.add(field);
            break;
        case NUMERIC:
            numericFields.add(field);
            break;
        case ENTITY:
            entityFields.add(field);
            break;
        case ENTITYCOLLECTION:
            entityCollections.add(field);
            break;
        case SIMPLECOLLECTION:
            simpleCollections.add(field);
            break;
        case ENTITYLIST:
            entityLists.add(field);
            break;
        case SIMPLELIST:
            simpleLists.add(field);
            break;
        case ENTITYMAP:
            entityMaps.add(field);
            break;
        case SIMPLEMAP:
            simpleMaps.add(field);
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

    public void include(ClassModel clazz) {
        addAll(booleanFields, clazz.booleanFields);
        addAll(entityCollections, clazz.entityCollections);
        addAll(entityFields, clazz.entityFields);
        addAll(entityLists, clazz.entityLists);
        addAll(entityMaps, clazz.entityMaps);
        addAll(comparableFields, clazz.comparableFields);
        addAll(numericFields, clazz.numericFields);
        addAll(simpleCollections, clazz.simpleCollections);
        addAll(simpleFields, clazz.simpleFields);
        addAll(simpleLists, clazz.simpleLists);
        addAll(simpleMaps, clazz.simpleMaps);
        addAll(stringFields, clazz.stringFields);
    }

    private void addAll(Set<FieldModel> target, Set<FieldModel> source) {
        for (FieldModel field : source) {
            target.add(validateField(field));
        }
    }

}