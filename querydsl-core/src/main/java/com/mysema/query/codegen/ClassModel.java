/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.mysema.commons.lang.Assert;

/**
 * ClassModel represents the model of a query domain type with properties
 * 
 * @author tiwe
 * @version $Id$
 */
public final class ClassModel implements Comparable<ClassModel> {
        
    public static final String DEFAULT_PREFIX = "Q";
    
    private final Collection<ConstructorModel> constructors = new HashSet<ConstructorModel>();
    
    private ClassModel superModel;
    
    // mutable
    private int escapeSuffix = 1;
    
    private final String simpleName, name, packageName, localName;
    
    private final String prefix;
    
    @Nullable
    private final String superType;
    
    private final Map<TypeCategory,Collection<FieldModel>> typeToFields = MapUtils.lazyMap(
            new HashMap<TypeCategory,Collection<FieldModel>>(),
            new Factory<Collection<FieldModel>>(){
                @Override
                public Collection<FieldModel> create() {
                    return new HashSet<FieldModel>();
                }                
            });

    private String uncapSimpleName;
    
    public ClassModel(String prefix, @Nullable String superType, String packageName, String name, String simpleName) {
        this.prefix = Assert.notNull(prefix);
        this.superType = superType;
        this.packageName = Assert.notNull(packageName);
        this.name = Assert.notNull(name);
        this.simpleName = Assert.notNull(simpleName);
        this.uncapSimpleName = StringUtils.uncapitalize(simpleName);
        this.localName = name.substring(packageName.length()+1);
    }
    
    public void addConstructor(ConstructorModel co) {
        constructors.add(co);
    }

    public void addField(FieldModel field) {
        validateField(field);
        Collection<FieldModel> fields = typeToFields.get(field.getTypeCategory());
        fields.add(field);
    }

    public int compareTo(ClassModel o) {
        return simpleName.compareTo(o.simpleName);
    }

    public boolean equals(Object o) {
        return o instanceof ClassModel && simpleName.equals(((ClassModel) o).simpleName);
    }

    public Collection<FieldModel> getBooleanFields() {
        return typeToFields.get(TypeCategory.BOOLEAN);
    }

    public Collection<FieldModel> getComparableFields() {
        return typeToFields.get(TypeCategory.COMPARABLE);
    }

    public Collection<ConstructorModel> getConstructors() {
        return constructors;
    }

    public Collection<FieldModel> getDateFields() {
        return typeToFields.get(TypeCategory.DATE);
    }
    
    public Collection<FieldModel> getDateTimeFields() {
        return typeToFields.get(TypeCategory.DATETIME);
    }
    
    public Collection<FieldModel> getEntityCollections() {
        return typeToFields.get(TypeCategory.ENTITYCOLLECTION);
    }
    
    public Collection<FieldModel> getEntityFields() {
        return typeToFields.get(TypeCategory.ENTITY);
    }

    public Collection<FieldModel> getEntityLists() {
        return typeToFields.get(TypeCategory.ENTITYLIST);
    }

    public Collection<FieldModel> getEntityMaps() {
        return typeToFields.get(TypeCategory.ENTITYMAP);
    }

    public String getName() {
        return name;
    }

    public Collection<FieldModel> getNumericFields() {
        return typeToFields.get(TypeCategory.NUMERIC);
    }

    public String getPackageName() {
        return packageName;
    }
    
    public Collection<FieldModel> getSimpleCollections() {
        return typeToFields.get(TypeCategory.SIMPLECOLLECTION);
    }

    public Collection<FieldModel> getSimpleFields() {
        return typeToFields.get(TypeCategory.SIMPLE);
    }

    public Collection<FieldModel> getSimpleLists() {
        return typeToFields.get(TypeCategory.SIMPLELIST);
    }

    public Collection<FieldModel> getSimpleMaps() {
        return typeToFields.get(TypeCategory.SIMPLEMAP);
    }

    public String getLocalName(){
        return localName;
    }
    
    public String getSimpleName() {
        return simpleName;
    }

    public Collection<FieldModel> getStringFields() {
        return typeToFields.get(TypeCategory.STRING);
    }

    public String getSupertypeName() {
        return superType;
    }

    public Collection<FieldModel> getTimeFields() {
        return typeToFields.get(TypeCategory.TIME);
    }

    public String getUncapSimpleName() {
        return uncapSimpleName;
    }

    public int hashCode() {
        return name.hashCode();
    }

    public void include(ClassModel clazz) {
        for (TypeCategory category : TypeCategory.values()){
            Collection<FieldModel> source = clazz.typeToFields.get(category);
            if (!source.isEmpty()){
                Collection<FieldModel> target = typeToFields.get(category);
                for (FieldModel field : source) {   
                    target.add(validateField(field.createCopy(this)));
                }    
            }            
        }        
    }
        
    private FieldModel validateField(FieldModel field) {
        if (field.getName().equals(this.uncapSimpleName)) {
            uncapSimpleName = StringUtils.uncapitalize(simpleName)+ (escapeSuffix++);
        }
        return field;
    }
    
    public String getPrefix(){
        return prefix;
    }

    public ClassModel getSuperModel() {
        return superModel;
    }

    public void setSuperModel(ClassModel superModel) {
        this.superModel = superModel;
    }
    
}