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
 * BeanModel represents the model of a query domain type with properties
 * 
 * @author tiwe
 * @version $Id$
 */
public final class BeanModel implements Comparable<BeanModel> {
        
    public static final String DEFAULT_PREFIX = "Q";
    
    private final Collection<ConstructorModel> constructors = new HashSet<ConstructorModel>();
    
    private BeanModel superModel;
    
    // mutable
    private int escapeSuffix = 1;
    
    private boolean entityModel = true;
    
    private final String simpleName, name, packageName, localName;
    
    private final String prefix;
    
    private final Collection<String> superTypes;
    
    private final Map<TypeCategory,Collection<PropertyModel>> typeToProperties = MapUtils.lazyMap(
            new HashMap<TypeCategory,Collection<PropertyModel>>(),
            new Factory<Collection<PropertyModel>>(){
                @Override
                public Collection<PropertyModel> create() {
                    return new HashSet<PropertyModel>();
                }                
            });

    private String uncapSimpleName;
    
    public BeanModel(String prefix, String packageName, String name, String simpleName, Collection<String> superTypes) {
        this.prefix = Assert.notNull(prefix);        
        this.packageName = Assert.notNull(packageName);
        this.name = Assert.notNull(name);
        this.simpleName = Assert.notNull(simpleName);
        this.uncapSimpleName = StringUtils.uncapitalize(simpleName);
        this.localName = name.substring(packageName.length()+1);
        this.superTypes = superTypes;
    }
    

    public void addConstructor(ConstructorModel co) {
        constructors.add(co);
    }

    public void addProperty(PropertyModel field) {
        validateField(field);
        Collection<PropertyModel> fields = typeToProperties.get(field.getTypeCategory());
        fields.add(field);
    }

    public int compareTo(BeanModel o) {
        return simpleName.compareTo(o.simpleName);
    }

    public boolean equals(Object o) {
        return o instanceof BeanModel && simpleName.equals(((BeanModel) o).simpleName);
    }

    public Collection<PropertyModel> getBooleanProperties() {
        return typeToProperties.get(TypeCategory.BOOLEAN);
    }

    public Collection<PropertyModel> getComparableProperties() {
        return typeToProperties.get(TypeCategory.COMPARABLE);
    }

    public Collection<ConstructorModel> getConstructors() {
        return constructors;
    }

    public Collection<PropertyModel> getDateProperties() {
        return typeToProperties.get(TypeCategory.DATE);
    }
    
    public Collection<PropertyModel> getDateTimeProperties() {
        return typeToProperties.get(TypeCategory.DATETIME);
    }
    
    public Collection<PropertyModel> getEntityCollections() {
        return typeToProperties.get(TypeCategory.ENTITYCOLLECTION);
    }
    
    public Collection<PropertyModel> getEntityProperties() {
        return typeToProperties.get(TypeCategory.ENTITY);
    }

    public Collection<PropertyModel> getEntityLists() {
        return typeToProperties.get(TypeCategory.ENTITYLIST);
    }

    public Collection<PropertyModel> getEntityMaps() {
        return typeToProperties.get(TypeCategory.ENTITYMAP);
    }

    public String getName() {
        return name;
    }

    public Collection<PropertyModel> getNumericProperties() {
        return typeToProperties.get(TypeCategory.NUMERIC);
    }

    public String getPackageName() {
        return packageName;
    }
    
    public Collection<PropertyModel> getSimpleCollections() {
        return typeToProperties.get(TypeCategory.SIMPLECOLLECTION);
    }

    public Collection<PropertyModel> getSimpleProperties() {
        return typeToProperties.get(TypeCategory.SIMPLE);
    }

    public Collection<PropertyModel> getSimpleLists() {
        return typeToProperties.get(TypeCategory.SIMPLELIST);
    }

    public Collection<PropertyModel> getSimpleMaps() {
        return typeToProperties.get(TypeCategory.SIMPLEMAP);
    }

    public String getLocalName(){
        return localName;
    }
    
    public String getSimpleName() {
        return simpleName;
    }

    public Collection<PropertyModel> getStringProperties() {
        return typeToProperties.get(TypeCategory.STRING);
    }

    public Collection<String> getSuperTypes() {
        return superTypes;
    }

    public Collection<PropertyModel> getTimeProperties() {
        return typeToProperties.get(TypeCategory.TIME);
    }

    public String getUncapSimpleName() {
        return uncapSimpleName;
    }

    public int hashCode() {
        return name.hashCode();
    }

    public void include(BeanModel clazz) {
        for (TypeCategory category : TypeCategory.values()){
            Collection<PropertyModel> source = clazz.typeToProperties.get(category);
            if (!source.isEmpty()){
                Collection<PropertyModel> target = typeToProperties.get(category);
                for (PropertyModel field : source) {   
                    target.add(validateField(field.createCopy(this)));
                }    
            }            
        }        
    }
        
    private PropertyModel validateField(PropertyModel field) {
        if (field.getName().equals(this.uncapSimpleName)) {
            uncapSimpleName = StringUtils.uncapitalize(simpleName)+ (escapeSuffix++);
        }
        return field;
    }
    
    public String getPrefix(){
        return prefix;
    }

    public BeanModel getSuperModel() {
        return superModel;
    }

    public void setSuperModel(BeanModel superModel) {
        this.superModel = superModel;
    }

    public boolean isEntityModel() {
        return entityModel;
    }

    public void setEntityModel(boolean entityModel) {
        this.entityModel = entityModel;
    }
    
}