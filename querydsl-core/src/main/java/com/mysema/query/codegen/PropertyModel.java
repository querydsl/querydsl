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
    
    private final BeanModel context;
    
    private final boolean inherited;
    
    private final String name, escapedName, typeName;
    
    @Nullable
    private final String queryTypeName;
    
    private final TypeModel type;
    
    private final String[] inits;
    
    public PropertyModel(BeanModel classModel, String name, TypeModel type, String[] inits){
        this(classModel, name, type, inits, false);
    }
    
    public PropertyModel(BeanModel classModel, String name, TypeModel type, String[] inits, boolean inherited){
        this.context = classModel;
        this.name = Assert.notNull(name);
        this.escapedName = JavaSyntaxUtils.isReserved(name) ? (name + "_") : name;
        this.type = Assert.notNull(type);
        this.typeName = type.getLocalRawName(classModel);    
        if (type.getTypeCategory().isSubCategoryOf(TypeCategory.SIMPLE)){
            this.queryTypeName = null;
        }else{
            TypeModel valueType = type.getSelfOrValueType();
            if (valueType.getPackageName().equals(classModel.getPackageName())){
                this.queryTypeName = classModel.getPrefix() + valueType.getSimpleName();
            }else{
                this.queryTypeName = valueType.getPackageName() + "." + classModel.getPrefix() + valueType.getSimpleName();
            }        
        }   
        this.inits = inits;
        this.inherited = inherited;
    }
    
    public int compareTo(PropertyModel o) {
        return name.compareToIgnoreCase(o.getName());
    }
    
    /* (non-Javadoc)
     * @see com.mysema.query.codegen.PropertyModel#createCopy(com.mysema.query.codegen.BeanModel)
     */
    public PropertyModel createCopy(BeanModel model){
        boolean inherited = model.getSuperModel() != null; 
        return new PropertyModel(model, name, type, inits, inherited);
    }
    
    public boolean equals(Object o) {
        return o instanceof PropertyModel && name.equals(((PropertyModel) o).name);
    }

    /* (non-Javadoc)
     * @see com.mysema.query.codegen.PropertyModel#getEscapedName()
     */
    public String getEscapedName(){
        return escapedName;
    }


    /* (non-Javadoc)
     * @see com.mysema.query.codegen.PropertyModel#getGenericParameterName(int)
     */
    @Nullable
    public String getGenericParameterName(int i){
        if (i < type.getParameterCount()){
            return type.getParameter(i).getLocalGenericName(context);
            
        }else{
            return null;
        }
    }

    /* (non-Javadoc)
     * @see com.mysema.query.codegen.PropertyModel#getGenericTypeName()
     */
    public String getGenericTypeName(){
        return type.getLocalGenericName(context);   
    }
    
    /* (non-Javadoc)
     * @see com.mysema.query.codegen.PropertyModel#getInits()
     */
    public String[] getInits(){
        return inits;
    }
    
    /* (non-Javadoc)
     * @see com.mysema.query.codegen.PropertyModel#getBeanModel()
     */
    public BeanModel getBeanModel(){
        return context;
    }
    
    /* (non-Javadoc)
     * @see com.mysema.query.codegen.PropertyModel#getName()
     */
    public String getName() {
        return name;
    }
    
    /* (non-Javadoc)
     * @see com.mysema.query.codegen.PropertyModel#getRawParameterName(int)
     */
    @Nullable
    public String getRawParameterName(int i){
        if (i < type.getParameterCount()){            
            return type.getParameter(i).getLocalRawName(context);
        }else{
            return null;
        }
    }
    
    /* (non-Javadoc)
     * @see com.mysema.query.codegen.PropertyModel#getQueryTypeName()
     */
    public String getQueryTypeName() {
        return queryTypeName;
    }

    /* (non-Javadoc)
     * @see com.mysema.query.codegen.PropertyModel#getSimpleTypeName()
     */
    public String getSimpleTypeName() {
        return type.getSimpleName();
    }

    /* (non-Javadoc)
     * @see com.mysema.query.codegen.PropertyModel#getTypeCategory()
     */
    public TypeCategory getTypeCategory() {
        return type.getTypeCategory();
    }
    
    /* (non-Javadoc)
     * @see com.mysema.query.codegen.PropertyModel#getTypeName()
     */
    public String getTypeName() {
        return typeName;
    }
    
    /* (non-Javadoc)
     * @see com.mysema.query.codegen.PropertyModel#getTypePackage()
     */
    public String getTypePackage() {
        return type.getPackageName();
    }
    
    public int hashCode() {
        return Arrays.asList(name, type).hashCode();
    }

    /* (non-Javadoc)
     * @see com.mysema.query.codegen.PropertyModel#isInherited()
     */
    public boolean isInherited() {
        return inherited;
    }

    public String toString() {
        return type.getFullName() + " " + name;
    }

}