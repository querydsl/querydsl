/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.codegen;

import java.util.List;

import net.jcip.annotations.Immutable;

import com.mysema.commons.lang.Assert;

@Immutable
public class Delegate {

    private final Type declaringType;

    private final Type delegateType;

    private final String name;

    private final List<Parameter> parameters;

    private final Type returnType;

    public Delegate(Type declaringType, Type delegateType, String name, List<Parameter> params, Type returnType) {
        this.declaringType = Assert.notNull(declaringType,"declaringType");
        this.delegateType = Assert.notNull(delegateType,"delegateType");
        this.name = Assert.notNull(name,"name");
        this.parameters = Assert.notNull(params,"params");
        this.returnType = Assert.notNull(returnType,"returnType");
    }

    @Override
    public boolean equals(Object o) {
        if (o == this){
            return true;
        }else if (o instanceof Delegate){
            Delegate m = (Delegate)o;
            return m.name.equals(name) && m.parameters.equals(parameters);
        }else{
            return false;
        }
    }

    public Type getDeclaringType() {
        return declaringType;
    }

    public Type getDelegateType() {
        return delegateType;
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

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString(){
        return delegateType.getFullName() + "." + name + " " + parameters;
    }

}
