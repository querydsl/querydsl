/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.codegen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.jcip.annotations.Immutable;

import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
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

    public Method(Type declaringType, String name, String template, Type returnType) {
        this(declaringType, name, template, Collections.<Parameter>emptyList(), returnType);
    }

    public Method(Type declaringType, String name, String template, List<Parameter> params, Type returnType) {
        this.declaringType = Assert.notNull(declaringType,"declaringType");
        this.name = Assert.notNull(name,"name");
        this.template = Assert.notNull(template,"template");
        this.parameters = Assert.notNull(params,"params");
        this.returnType = Assert.notNull(returnType,"returnType");
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
        Type newReturnType = TypeResolver.resolve(returnType, declaringType, model);
        if (newReturnType.getCategory() == TypeCategory.ENTITY){
            newReturnType = newReturnType.as(TypeCategory.SIMPLE);
        }

        List<Parameter> newParameters = new ArrayList<Parameter>();
        for (Parameter param : parameters){
            Type newType = TypeResolver.resolve(param.getType(), declaringType, model);
            newParameters.add(new Parameter(param.getName(), newType));
        }
        if (!newReturnType.equals(returnType) || !newParameters.equals(parameters)){
            return new Method(declaringType, name, template, newParameters, newReturnType);
        }else{
            return this;
        }
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

    @Override
    public String toString(){
        return declaringType.getFullName() + "." + name + " " + parameters;
    }

}
