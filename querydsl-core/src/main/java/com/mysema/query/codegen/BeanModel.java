/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.util.Collection;
import java.util.Collections;
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
    
    private boolean entityModel = true;
    
    // mutable
    private int escapeSuffix = 1;
    
    private final String prefix;
    
    @Nullable
    private BeanModel superModel;
    
    private final Collection<String> superTypes;
    
    private final TypeModel typeModel;
    
    private final Map<TypeCategory,Collection<PropertyModel>> typeToProperties = MapUtils.lazyMap(
            new HashMap<TypeCategory,Collection<PropertyModel>>(),
            new Factory<Collection<PropertyModel>>(){
                @Override
                public Collection<PropertyModel> create() {
                    return new HashSet<PropertyModel>();
                }                
            });

    private String uncapSimpleName;
    
    public BeanModel(String prefix, TypeModel typeModel) {
        this(prefix, typeModel, Collections.<String>emptyList());
    }
    
    public BeanModel(String prefix, TypeModel typeModel, Collection<String> superTypes) {
        this.prefix = Assert.notNull(prefix);        
        this.typeModel = typeModel;
        this.uncapSimpleName = StringUtils.uncapitalize(typeModel.getSimpleName());
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
        return typeModel.getSimpleName().compareTo(o.typeModel.getSimpleName());
    }

    public boolean equals(Object o) {
        return o instanceof BeanModel && typeModel.getName().equals(((BeanModel) o).typeModel.getName());
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
    
    public Collection<PropertyModel> getEntityLists() {
        return typeToProperties.get(TypeCategory.ENTITYLIST);
    }

    public Collection<PropertyModel> getEntityMaps() {
        return typeToProperties.get(TypeCategory.ENTITYMAP);
    }

    public Collection<PropertyModel> getEntityProperties() {
        return typeToProperties.get(TypeCategory.ENTITY);
    }

    public String getLocalName() {
        return typeModel.getLocalName();
    }

    public String getName() {
        return typeModel.getName();
    }
    
    public String getGenericName(){
        if (typeModel.getParameterCount() == 0){
            return typeModel.getLocalName();
        }else{
            StringBuilder builder = new StringBuilder(typeModel.getLocalName()).append("<");
            for (int i = 0; i < typeModel.getParameterCount(); i++){
                if (i > 0) builder.append(",");
                builder.append("?");
            }
            return builder.append(">").toString();    
        }            
    }

    public Collection<PropertyModel> getNumericProperties() {
        return typeToProperties.get(TypeCategory.NUMERIC);
    }

    public String getPackageName() {
        return typeModel.getPackageName();
    }

    public String getPrefix(){
        return prefix;
    }

    public Collection<PropertyModel> getSimpleCollections() {
        return typeToProperties.get(TypeCategory.SIMPLECOLLECTION);
    }

    public Collection<PropertyModel> getSimpleLists() {
        return typeToProperties.get(TypeCategory.SIMPLELIST);
    }

    public Collection<PropertyModel> getSimpleMaps() {
        return typeToProperties.get(TypeCategory.SIMPLEMAP);
    }

    public String getSimpleName() {
        return typeModel.getSimpleName();
    }

    public Collection<PropertyModel> getSimpleProperties() {
        return typeToProperties.get(TypeCategory.SIMPLE);
    }

    public Collection<PropertyModel> getStringProperties() {
        return typeToProperties.get(TypeCategory.STRING);
    }
        
    @Nullable
    public BeanModel getSuperModel() {
        return superModel;
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
        return typeModel.getName().hashCode();
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


    public boolean isEntityModel() {
        return entityModel;
    }


    public void setEntityModel(boolean entityModel) {
        this.entityModel = entityModel;
    }


    public void setSuperModel(BeanModel superModel) {
        this.superModel = superModel;
    }


    private PropertyModel validateField(PropertyModel field) {
        if (field.getName().equals(this.uncapSimpleName)) {
            uncapSimpleName = StringUtils.uncapitalize(typeModel.getSimpleName())+ (escapeSuffix++);
        }
        return field;
    }
    
}