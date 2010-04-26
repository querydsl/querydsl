/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.IOException;

import net.jcip.annotations.Immutable;

import com.mysema.commons.lang.Assert;


/**
 * SimpleType represents a java type
 * 
 * @author tiwe
 *
 */
@Immutable
public final class SimpleType extends AbstractType {

    private final String fullName, packageName, simpleName, localName;

    private final Type[] parameters;

    private final TypeCategory typeCategory;
    
    private final boolean visible, finalClass;
    
    public SimpleType(
            TypeCategory typeCategory, 
            String name,
            String packageName, 
            String simpleName, 
            boolean finalClass,
            Type... parameters) {
        this.typeCategory = Assert.notNull(typeCategory,"typeCategory is null");
        this.fullName = Assert.notNull(name,"name is null");
        this.packageName = Assert.notNull(packageName,"packageName is null");
        this.simpleName = Assert.notNull(simpleName,"simpleName is null");
        if (!packageName.isEmpty()){
            this.localName = name.substring(packageName.length()+1);            
        }else{
            this.localName = simpleName;    
        }        
        this.parameters = Assert.notNull(parameters,"parameters");
        this.visible = packageName.equals("java.lang");
        this.finalClass = finalClass;
    }

    public SimpleType as(TypeCategory category) {
        if (typeCategory == category){
            return this;
        }else{
            return new SimpleType(category, fullName, packageName, simpleName, finalClass, parameters);
        }
    }
    
    @Override
    public Type asArrayType() {
        return new SimpleType(TypeCategory.ARRAY, fullName+"[]", packageName, simpleName+"[]", finalClass, this);
    }

    @Override
    public boolean equals(Object o){
        if (o == this){
            return true;
        }else if (o instanceof Type){
            Type t = (Type)o;
            return fullName.equals(t.getFullName());
        }else{
            return false;
        }
    }
    
    @Override
    public TypeCategory getCategory() {
        return typeCategory;
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    // NOTE: Java serialization aspects mixed into model
    @Override
    public void appendLocalGenericName(Type context, Appendable builder, boolean asArgType) throws IOException {
        appendLocalRawName(context, builder);
        if (parameters.length > 0){                        
            builder.append("<");
            for (int i = 0; i < parameters.length; i++){
                if (i > 0){
                    builder.append(",");
                }
                if (parameters[i] != null && !parameters[i].getFullName().equals(fullName)){
                    parameters[i].appendLocalGenericName(context, builder, false);    
                }else{
                    builder.append("?");
                }                
            }            
            builder.append(">");            
        }
        
    }

    @Override
    public void  appendLocalRawName(Type context, Appendable builder) throws IOException{
        if (visible || context.getPackageName().equals(packageName)){
            builder.append(localName);    
        }else{
            builder.append(fullName);
        }        
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public Type getParameter(int i) {
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
    public Type getSelfOrValueType() {
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
    public boolean hasEntityFields() {
        return false;
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
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public String toString() {
        return fullName;
    }

    
}