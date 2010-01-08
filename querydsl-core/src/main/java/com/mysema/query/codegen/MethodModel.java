package com.mysema.query.codegen;

import java.util.List;

import net.jcip.annotations.Immutable;

import com.mysema.commons.lang.Assert;

/**
 * @author tiwe
 * 
 */
@Immutable
public class MethodModel {

    private final EntityModel context;

    private final String name;

    private final List<ParameterModel> parameters;

    private final TypeModel returnType;
    
    private final String template;

    public MethodModel(EntityModel context, String name, String template, List<ParameterModel> params, TypeModel returnType) {
        this.context = Assert.notNull(context);
        this.name = Assert.notNull(name);
        this.template = Assert.notNull(template);
        this.parameters = Assert.notNull(params);
        this.returnType = Assert.notNull(returnType);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof MethodModel && ((MethodModel) o).name.equals(name) && ((MethodModel) o).parameters.equals(parameters);
    }

    public EntityModel getContext() {
        return context;
    }

    public String getName() {
        return name;
    }

    public List<ParameterModel> getParameters() {
        return parameters;
    }

    public TypeModel getReturnType() {
        return returnType;
    }
    
    public String getTemplate() {
        return template;
    }

    @Override
    public int hashCode() {
        return parameters.hashCode();
    }
}
