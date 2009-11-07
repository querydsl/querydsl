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

    private final String fullName, packageName, simpleName, localName;

    private final TypeModel[] parameters;

    private final TypeCategory typeCategory;
    
    private final boolean visible;
    
    public SimpleTypeModel(
            TypeCategory typeCategory, 
            String name,
            String packageName, 
            String simpleName, 
            TypeModel... parameters) {
        this.typeCategory = Assert.notNull(typeCategory,"typeCategory is null");
        this.fullName = Assert.notNull(name,"name is null");
        this.packageName = Assert.notNull(packageName,"packageName is null");
        this.simpleName = Assert.notNull(simpleName,"simpleName is null");
        this.localName = name.substring(packageName.length()+1);
        this.parameters = Assert.notNull(parameters);
        this.visible = packageName.equals("java.lang");
    }

    public SimpleTypeModel as(TypeCategory category) {
        if (typeCategory == category){
            return this;
        }else{
            return new SimpleTypeModel(category, fullName, packageName, simpleName, parameters);
        }
    }

    @Override
    public String getFullName() {
        return fullName;
    }
    
    @Override
    public String getLocalGenericName(BeanModel context) {
        if (parameters.length > 0){
            StringBuilder builder = new StringBuilder();
            if (!visible && !context.getPackageName().equals(packageName)){
                builder.append(packageName).append(".");
            }
            builder.append(localName).append("<");
            for (int i = 0; i < parameters.length; i++){
                if (i > 0) builder.append(",");
                if (parameters[i] != null && !parameters[i].equals(this)){
                    builder.append(parameters[i].getLocalGenericName(context));    
                }else{
                    builder.append("?");
                }                
            }            
            builder.append(">");
            return builder.toString();
            
        }else{
            return getLocalRawName(context);
        }
    }

    @Override
    public String getLocalRawName(BeanModel context){
        if (visible || context.getPackageName().equals(packageName)){
            return localName;    
        }else{
            return fullName;
        }        
    }

    @Override
    public String getPackageName() {
        return packageName;
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
    public String getPrimitiveName(){
        return null;
    }

    @Override
    public TypeModel getSelfOrValueType() {
        if (typeCategory.isSubCategoryOf(TypeCategory.COLLECTION) 
         || typeCategory.isSubCategoryOf(TypeCategory.MAP)){
            return parameters[parameters.length - 1];
        }else{
            return this;
        }
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
        return fullName;
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof TypeModel){
            TypeModel t = (TypeModel)o;
            return fullName.equals(t.getFullName());
        }else{
            return false;
        }
    }
    
    @Override
    public int hashCode(){
        return fullName.hashCode();
    }
    
}