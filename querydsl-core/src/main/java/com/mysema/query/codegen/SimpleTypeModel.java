/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import net.jcip.annotations.Immutable;

import com.mysema.commons.lang.Assert;


/**
 * SimpleTypeModel represents a java type
 * 
 * @author tiwe
 *
 */
@Immutable
public final class SimpleTypeModel implements TypeModel {

    private final TypeModel[] parameters;

    private final String name, packageName, simpleName, localName;

    private final TypeCategory typeCategory;
    
    public SimpleTypeModel(
            TypeCategory typeCategory, 
            String name,
            String packageName, 
            String simpleName, 
            TypeModel... parameters) {
        this.typeCategory = Assert.notNull(typeCategory,"typeCategory is null");
        this.name = Assert.notNull(name,"name is null");
        this.packageName = Assert.notNull(packageName,"packageName is null");
        this.simpleName = Assert.notNull(simpleName,"simpleName is null");
        this.localName = name.substring(packageName.length()+1);
        this.parameters = Assert.notNull(parameters);
    }

    public SimpleTypeModel as(TypeCategory category) {
        if (typeCategory == category){
            return this;
        }else{
            return new SimpleTypeModel(category, name, packageName, simpleName, parameters);
        }
    }

    @Override
    public String getLocalName(){
        return localName;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public String getPrimitiveName(){
        return null;
    }

    @Override
    public String getSimpleName() {
        return simpleName;
    }

    @Override
    public TypeCategory getTypeCategory() {
        return typeCategory;
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public TypeModel getParameter(int i) {
        return parameters[i];
    }

    @Override
    public int getParameterCount() {
        return parameters.length;
    }

    @Override
    public TypeModel getSelfOrValueType() {
        if (parameters.length == 0){
            return this;
        }else{
            TypeModel rv = parameters[parameters.length -1];
            return rv != null ? rv : this;
        }
    }

    
}