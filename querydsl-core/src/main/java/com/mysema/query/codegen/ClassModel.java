/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import com.mysema.commons.lang.Assert;

/**
 * ClassModel represents a query domain type.
 * 
 * @author tiwe
 * @version $Id$
 */
public class ClassModel implements Comparable<ClassModel> {
    
    public static ClassModel createFor(Class<?> key){
        ClassModel value = new ClassModel(
                key.getSuperclass().getName(), 
                key.getPackage().getName(), 
                key.getName(), 
                key.getSimpleName());
        for (java.lang.reflect.Field f : key.getDeclaredFields()) {
            TypeModel typeHelper = ReflectionTypeModel.get(f.getType(), f.getGenericType());
            value.addField(new FieldModel(f.getName(), typeHelper));
        }
        return value;
    }

    private int escapeSuffix = 1;

    private final Collection<ConstructorModel> constructors = new HashSet<ConstructorModel>();
    
    private final Collection<FieldModel> booleanFields = new TreeSet<FieldModel>();

    private final Collection<FieldModel> comparableFields = new TreeSet<FieldModel>();

    private final Collection<FieldModel> dateFields = new TreeSet<FieldModel>();

    private final Collection<FieldModel> dateTimeFields = new TreeSet<FieldModel>();
    
    private final Collection<FieldModel> entityCollections = new TreeSet<FieldModel>();
    
    private final Collection<FieldModel> entityFields = new TreeSet<FieldModel>();
    
    private final Collection<FieldModel> entityLists = new TreeSet<FieldModel>();

    private final Collection<FieldModel> entityMaps = new TreeSet<FieldModel>();

    private final Collection<FieldModel> numericFields = new TreeSet<FieldModel>();

    private final Collection<FieldModel> simpleCollections = new TreeSet<FieldModel>();

    private final Collection<FieldModel> simpleFields = new TreeSet<FieldModel>();

    private final Collection<FieldModel> simpleLists = new TreeSet<FieldModel>();

    private final Collection<FieldModel> simpleMaps = new TreeSet<FieldModel>();

    private final Collection<FieldModel> stringFields = new TreeSet<FieldModel>();
    
    private final Collection<FieldModel> timeFields = new TreeSet<FieldModel>();
    
    private String simpleName, uncapSimpleName, name, packageName;
    
    private String superType;

    private boolean listsAsCollections;    

    public ClassModel(String superType, String packageName, String name, String simpleName) {
        this.superType = superType;
        this.packageName = Assert.notNull(packageName);
        this.name = Assert.notNull(name);
        this.simpleName = Assert.notNull(simpleName);
        this.uncapSimpleName = StringUtils.uncapitalize(simpleName);
    }

    private void addAll(Collection<FieldModel> target, Collection<FieldModel> source) {
        for (FieldModel field : source) {
            target.add(validateField(field));
        }
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
            if (listsAsCollections){
                entityCollections.add(field);
            }else{
                entityLists.add(field);    
            }            
            break;
        case SIMPLELIST:
            if (listsAsCollections){
                simpleCollections.add(field);
            }else{
                simpleLists.add(field);    
            }            
            break;
        case ENTITYMAP:
            entityMaps.add(field);
            break;
        case SIMPLEMAP:
            simpleMaps.add(field);
            break;
        case DATE:
            dateFields.add(field);
            break;
        case DATETIME:
            dateTimeFields.add(field);
            break;
        case TIME:
            timeFields.add(field);
            break;
        }
    }

    public void addSupertypeFields(Map<String, ClassModel> entityTypes, Map<String, ClassModel> supertypes) {
        String stype = getSupertypeName();
        Class<?> superClass = safeClassForName(stype);
        if (entityTypes.containsKey(stype) || supertypes.containsKey(stype)) {
            while (true) {
                ClassModel sdecl;
                if (entityTypes.containsKey(stype)) {
                    sdecl = entityTypes.get(stype);
                } else if (supertypes.containsKey(stype)) {
                    sdecl = supertypes.get(stype);
                } else {
                    return;
                }
                include(sdecl);
                stype = sdecl.getSupertypeName();
            }

        } else if (superClass != null && !superClass.equals(Object.class)) {
            // TODO : recursively up ?
            ClassModel type = ClassModel.createFor(superClass);
            // include fields of supertype
            include(type);
        }
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

    public Collection<FieldModel> getDateFields() {
        return dateFields;
    }
    
    public Collection<FieldModel> getDateTimeFields() {
        return dateTimeFields;
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

    public Collection<FieldModel> getStringFields() {
        return stringFields;
    }

    public String getSupertypeName() {
        return superType;
    }

    public Collection<FieldModel> getTimeFields() {
        return timeFields;
    }

    public String getUncapSimpleName() {
        return uncapSimpleName;
    }

    public int hashCode() {
        return name.hashCode();
    }

    public void include(ClassModel clazz) {
        addAll(booleanFields, clazz.booleanFields);
        addAll(dateFields, clazz.dateFields);
        addAll(dateTimeFields, clazz.dateTimeFields);
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
        addAll(timeFields, clazz.timeFields);
    }
    
    private Class<?> safeClassForName(String stype) {
        try {
            return stype != null ? Class.forName(stype) : null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }    
    
    private FieldModel validateField(FieldModel field) {
        if (field.getName().equals(this.uncapSimpleName)) {
            uncapSimpleName = StringUtils.uncapitalize(simpleName)+ (escapeSuffix++);
        }
        return field;
    }

    public void setListsAsCollections(boolean b) {
        listsAsCollections = b;        
    }

}