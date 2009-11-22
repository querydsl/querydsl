/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.util.Arrays;

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
    
    private final String name, escapedName;
    
    private final TypeModel type;
    
    private final String[] inits;
    
    public PropertyModel(EntityModel classModel, String name, TypeModel type, String[] inits){
        this(classModel, name, type, inits, false);
    }
    
    public PropertyModel(EntityModel classModel, String name, TypeModel type, String[] inits, boolean inherited){
        this.context = classModel;
        this.name = Assert.notNull(name);
        this.escapedName = JavaSyntaxUtils.isReserved(name) ? (name + "_") : name;
        this.type = Assert.notNull(type);
        this.inits = inits;
        this.inherited = inherited;
    }
    
    public int compareTo(PropertyModel o) {
        return name.compareToIgnoreCase(o.getName());
    }
    
    public PropertyModel createCopy(EntityModel model){
        boolean inherited = model.getSuperModel() != null; 
        return new PropertyModel(model, name, type, inits, inherited);
    }
    
    public boolean equals(Object o) {
        return o instanceof PropertyModel && name.equals(((PropertyModel) o).name);
    }

    public String getEscapedName(){
        return escapedName;
    }

    public String[] getInits(){
        return inits;
    }

    public EntityModel getEntityModel(){
        return context;
    }

    public String getName() {
        return name;
    }
    
    
    public int hashCode() {
        return Arrays.asList(name, type).hashCode();
    }

    public boolean isInherited() {
        return inherited;
    }

    public String toString() {
        return context.getFullName() + "." + name;
    }
    
    public TypeModel getParameter(int i){
        return type.getParameter(i);
    }

    public TypeModel getType() {
        return type;
    }
      

}