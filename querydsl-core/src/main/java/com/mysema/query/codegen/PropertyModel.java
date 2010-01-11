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

    private final String[] inits;

    private final String name, escapedName;

    private final TypeModel type;

    public PropertyModel(EntityModel context, String name, TypeModel type, String[] inits) {
        this(context, name, type, inits, false);
    }

    public PropertyModel(EntityModel context, String name, TypeModel type, String[] inits, boolean inherited) {
        this.context = context;
        this.name = Assert.notNull(name);
        this.escapedName = JavaSyntaxUtils.isReserved(name) ? (name + "_") : name;
        this.type = Assert.notNull(type);
        this.inits = inits;
        this.inherited = inherited;
    }

    public int compareTo(PropertyModel o) {
        return name.compareToIgnoreCase(o.getName());
    }

    public PropertyModel createCopy(EntityModel model) {
        boolean inherited = model.getSuperModel() != null;
        return new PropertyModel(model, name, type, inits, inherited);
    }

    public boolean equals(Object o) {
        return o instanceof PropertyModel && name.equals(((PropertyModel) o).name);
    }

    public EntityModel getContext() {
        return context;
    }

    public String getEscapedName() {
        return escapedName;
    }

    public String[] getInits() {
        return inits;
    }

    public String getName() {
        return name;
    }

    public TypeModel getParameter(int i) {
        return type.getParameter(i);
    }

    public TypeModel getType() {
        return type;
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

}