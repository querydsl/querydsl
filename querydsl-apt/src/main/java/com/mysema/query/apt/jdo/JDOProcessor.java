package com.mysema.query.apt.jdo;

import static com.mysema.query.apt.Constants.*;

import com.mysema.query.apt.general.DefaultEntityVisitor;
import com.mysema.query.apt.general.GeneralProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.MethodDeclaration;

public class JDOProcessor extends GeneralProcessor{

    public JDOProcessor(AnnotationProcessorEnvironment env) {
    	super(env, JDO_ENTITY, JDO_ENTITY, QD_DTO);
    }
    
    @Override
    protected DefaultEntityVisitor createEntityVisitor(){
        return new DefaultEntityVisitor(){
            @Override
            public void visitMethodDeclaration(MethodDeclaration d) {
                // skip property handling
            }
        };
    }
}
