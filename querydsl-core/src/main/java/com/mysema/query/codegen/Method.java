/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.util.Collections;
import java.util.List;

import net.jcip.annotations.Immutable;

import com.mysema.commons.lang.Assert;

/**
 * @author tiwe
 * 
 */
@Immutable
public final class Method {

    private final Type declaringType;

    private final String name;

    private final List<Parameter> parameters;

    private final Type returnType;
    
    private final String template;

    public Method(EntityType context, String name, String template, Type returnType) {
        this(context, name, template, Collections.<Parameter>emptyList(), returnType);        
    }
    
    public Method(Type declaringType, String name, String template, List<Parameter> params, Type returnType) {
        this.declaringType = Assert.notNull(declaringType);
        this.name = Assert.notNull(name);
        this.template = Assert.notNull(template);
        this.parameters = Assert.notNull(params);
        this.returnType = Assert.notNull(returnType);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this){
            return true;
        }else if (o instanceof Method){
            Method m = (Method)o;
            return m.name.equals(name) && m.parameters.equals(parameters);    
        }else{
            return false;
        }        
    }
    
    public Method createCopy(EntityType model) {
        // TODO
        return this;
    }

    public Type getDeclaringType() {
        return declaringType;
    }

    public String getName() {
        return name;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public Type getReturnType() {
        return returnType;
    }
    
    public String getTemplate() {
        return template;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
    
    
}
