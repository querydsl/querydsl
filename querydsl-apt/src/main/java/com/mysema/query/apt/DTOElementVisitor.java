/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleElementVisitor6;

import net.jcip.annotations.Immutable;

import com.mysema.query.codegen.ClassModel;
import com.mysema.query.codegen.ConstructorModel;
import com.mysema.query.codegen.ParameterModel;
import com.mysema.query.codegen.TypeModel;

/**
 * @author tiwe
 *
 */
@Immutable
public final class DTOElementVisitor extends SimpleElementVisitor6<ClassModel, Void>{
    
    private final ProcessingEnvironment env;
    
    private final String namePrefix;
    
    private final APTModelFactory typeFactory;
    
    private final Configuration configuration;
    
    DTOElementVisitor(ProcessingEnvironment env, Configuration configuration, String namePrefix, APTModelFactory typeFactory){
        this.env = env;
        this.configuration = configuration;
        this.namePrefix = namePrefix;
        this.typeFactory = typeFactory;
    }
    
    @Override
    public ClassModel visitType(TypeElement e, Void p) {
        Elements elementUtils = env.getElementUtils();
        TypeModel c = typeFactory.create(e.asType(), elementUtils);
        ClassModel classModel = new ClassModel(namePrefix, null, c.getPackageName(), c.getName(), c.getSimpleName());
        List<? extends Element> elements = e.getEnclosedElements();
        
        // CONSTRUCTOR
        for (ExecutableElement constructor : ElementFilter.constructorsIn(elements)){
            if (configuration.isValidConstructor(constructor)){
                List<ParameterModel> parameters = new ArrayList<ParameterModel>(constructor.getParameters().size());
                for (VariableElement var : constructor.getParameters()){
                    TypeModel varType = typeFactory.create(var.asType(), elementUtils);
                    parameters.add(new ParameterModel(var.getSimpleName().toString(), varType.getName()));
                }
                classModel.addConstructor(new ConstructorModel(parameters));    
            }                
        }                                    
        return classModel;
    }
    
}
