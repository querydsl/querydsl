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
public class SimpleTypeModel extends AbstractTypeModel {

    private final String fullName, packageName, simpleName, localName;

    private final TypeModel[] parameters;

    private final TypeCategory typeCategory;
    
    private final boolean visible, finalClass;
    
    public SimpleTypeModel(
            TypeCategory typeCategory, 
            String name,
            String packageName, 
            String simpleName, 
            boolean finalClass,
            TypeModel... parameters) {
        this.typeCategory = Assert.notNull(typeCategory,"typeCategory is null");
        this.fullName = Assert.notNull(name,"name is null");
        this.packageName = Assert.notNull(packageName,"packageName is null");
        this.simpleName = Assert.notNull(simpleName,"simpleName is null");
        this.localName = name.substring(packageName.length()+1);
        this.parameters = Assert.notNull(parameters);
        this.visible = packageName.equals("java.lang");
        this.finalClass = finalClass;
    }

    public SimpleTypeModel as(TypeCategory category) {
        if (typeCategory == category){
            return this;
        }else{
            return new SimpleTypeModel(category, fullName, packageName, simpleName, finalClass, parameters);
        }
    }

    @Override
    public String getFullName() {
        return fullName;
    }
    
    @Override
    public StringBuilder getLocalGenericName(TypeModel context, StringBuilder builder, boolean asArgType) {
        builder = getLocalRawName(context, builder);
        if (parameters.length > 0){                        
            builder.append("<");
            for (int i = 0; i < parameters.length; i++){
                if (i > 0) builder.append(",");
                if (parameters[i] != null && !parameters[i].getFullName().equals(fullName)){
                    builder = parameters[i].getLocalGenericName(context, builder, false);    
                }else{
                    builder.append("?");
                }                
            }            
            builder.append(">");            
        }
        return builder;
        
    }

    @Override
    public StringBuilder getLocalRawName(TypeModel context, StringBuilder builder){
        if (visible || context.getPackageName().equals(packageName)){
            builder.append(localName);    
        }else{
            builder.append(fullName);
        }        
        return builder;
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

    @Override
    public boolean isFinal() {
        return finalClass;
    }

    @Override
    public boolean hasEntityFields() {
        return false;
    }

    
}