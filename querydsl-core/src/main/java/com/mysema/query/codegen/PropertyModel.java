/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

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
    
    private final BeanModel classModel;
    
    private final String name, escapedName, typeName;
    
    @Nullable
    private final String queryTypeName;
    
    private final TypeModel type;
    
    public PropertyModel(BeanModel classModel, String name, TypeModel type){
        this.classModel = classModel;
        this.name = Assert.notNull(name);
        this.escapedName = JavaSyntaxUtils.isReserved(name) ? (name + "_") : name;
        this.type = Assert.notNull(type);
        this.typeName = getLocalName(type);    
        if (type.getTypeCategory().isSubCategoryOf(TypeCategory.SIMPLE)){
            this.queryTypeName = null;
        }else if (isVisible(type)){
            this.queryTypeName = classModel.getPrefix() + type.getSimpleName();
        }else{
            this.queryTypeName = type.getPackageName() + "." + classModel.getPrefix() + type.getSimpleName();
        }        
    }
    
    private boolean isVisible(TypeModel type){
        return classModel.getPackageName().equals(type.getPackageName()) || type.getPackageName().equals("java.lang");
    }
    
    private String getLocalName(TypeModel type){        
        return isVisible(type) ? type.getLocalName() : type.getName();
    }
    
    public int compareTo(PropertyModel o) {
        return name.compareToIgnoreCase(o.name);
    }

    public PropertyModel createCopy(BeanModel model){
        return new PropertyModel(model, name, type);
    }

    public boolean equals(Object o) {
        return o instanceof PropertyModel && name.equals(((PropertyModel) o).name);
    }

    public TypeCategory getTypeCategory() {
        return type.getTypeCategory();
    }

    public String getGenericParameterName(int i){
        if (i < type.getParameterCount()){
            TypeModel typeModel = type.getParameter(i);
            if (typeModel.getParameterCount() > 0){
                return getGenericName(typeModel);    
            }else{
                return getLocalName(typeModel);
            }
            
        }else{
            return null;
        }
    }
    
    public String getParameterName(int i){
        if (i < type.getParameterCount()){
            return getLocalName(type.getParameter(i));
        }else{
            return null;
        }
    }
    
    public String getName() {
        return name;
    }
    
    public String getEscapedName(){
        return escapedName;
    }
    
    public String getQueryTypeName() {
        return queryTypeName;
    }

    public String getSimpleTypeName() {
        return type.getSimpleName();
    }

    public String getTypeName() {
        return typeName;
    }
    
    public String getGenericTypeName(){
        TypeModel base = type;
        if (type.getTypeCategory().isSubCategoryOf(TypeCategory.COLLECTION)){
            base = type.getParameter(0);
        }else if (type.getTypeCategory().isSubCategoryOf(TypeCategory.MAP)){
            base = type.getParameter(1);
        }        
        if (base.getParameterCount() > 0){
            return getGenericName(base);
        }else{
            return typeName;
        }        
    }
    
    private String getGenericName(TypeModel typeModel){
        StringBuilder builder = new StringBuilder(getLocalName(typeModel)).append("<");
        for (int i = 0; i < typeModel.getParameterCount(); i++){
            if (i > 0) builder.append(",");
            builder.append("?");
        }
        return builder.append(">").toString();    
    }
    
    public String getTypePackage() {
        return type.getPackageName();
    }

    public int hashCode() {
        return name.hashCode();
    }

    public String toString() {
        return type.getName() + " " + name;
    }

}