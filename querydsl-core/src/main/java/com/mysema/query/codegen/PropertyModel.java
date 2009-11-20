/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.util.Arrays;

import javax.annotation.Nullable;

import net.jcip.annotations.Immutable;

import com.mysema.commons.lang.Assert;
import com.mysema.util.JavaSyntaxUtils;

/**
 * PropertyModel represents a property in a query domain type.
 * 
 * @author tiwe
 * @version $Id$
 */
@Immutable
public final class PropertyModel implements Comparable<PropertyModel> {
    
    private final EntityModel context;
    
    private final boolean inherited;
    
    private final String name, escapedName, typeName;
    
    @Nullable
    private final String queryTypeName;
    
    private final TypeModel propertyType;
    
    private final String[] inits;
    
    public PropertyModel(EntityModel classModel, String name, TypeModel type, String[] inits){
        this(classModel, name, type, inits, false);
    }
    
    public PropertyModel(EntityModel classModel, String name, TypeModel type, String[] inits, boolean inherited){
        this.context = classModel;
        this.name = Assert.notNull(name);
        this.escapedName = JavaSyntaxUtils.isReserved(name) ? (name + "_") : name;
        this.propertyType = Assert.notNull(type);
        this.typeName = type.getLocalRawName(classModel, new StringBuilder()).toString();
        this.inits = inits;
        this.inherited = inherited;
        this.queryTypeName = type.getQueryTypeName(context);
    }
    
    public int compareTo(PropertyModel o) {
        return name.compareToIgnoreCase(o.getName());
    }
    
    public PropertyModel createCopy(EntityModel model){
        boolean inherited = model.getSuperModel() != null; 
        return new PropertyModel(model, name, propertyType, inits, inherited);
    }
    
    public boolean equals(Object o) {
        return o instanceof PropertyModel && name.equals(((PropertyModel) o).name);
    }

    public String getEscapedName(){
        return escapedName;
    }

    @Nullable
    public String getGenericParameterName(int i){
        return getGenericParameterName(i, false);
    }
    
    @Nullable
    public String getGenericParameterName(int i, boolean asArgType){
        if (i < propertyType.getParameterCount()){
            return propertyType.getParameter(i).getLocalGenericName(context, new StringBuilder(), asArgType).toString();
            
        }else{
            return null;
        }
    }

    public String getGenericTypeName(){
        return propertyType.getLocalGenericName(context, new StringBuilder(), false).toString();   
    }

    public String[] getInits(){
        return inits;
    }

    public EntityModel getBeanModel(){
        return context;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public String getRawParameterName(int i){
        if (i < propertyType.getParameterCount()){            
            return propertyType.getParameter(i).getLocalRawName(context, new StringBuilder()).toString();
        }else{
            return null;
        }
    }

    public String getQueryTypeName() {
        return queryTypeName;
    }

    public String getSimpleTypeName() {
        return propertyType.getSimpleName();
    }

    public TypeCategory getTypeCategory() {
        return propertyType.getTypeCategory();
    }

    public String getTypeName() {
        return typeName;
    }

    public String getTypePackage() {
        return propertyType.getPackageName();
    }
    
    public int hashCode() {
        return Arrays.asList(name, propertyType).hashCode();
    }

    public boolean isInherited() {
        return inherited;
    }

    public String toString() {
        return context.getFullName() + "." + name;
    }

    public boolean hasEntityFields() {
        return propertyType.hasEntityFields();
    }

}