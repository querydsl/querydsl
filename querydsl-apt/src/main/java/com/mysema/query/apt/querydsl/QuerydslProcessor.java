package com.mysema.query.apt.querydsl;

import com.mysema.query.apt.Constants;
import com.mysema.query.apt.general.DefaultEntityVisitor;
import com.mysema.query.apt.general.GeneralProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.MethodDeclaration;

/**
 * QureydslProcessor provides
 *
 * @author tiwe
 * @version $Id$
 */
public class QuerydslProcessor extends GeneralProcessor implements Constants{

    public QuerydslProcessor(AnnotationProcessorEnvironment env) {
        super(env, null, qdEntity, qdDto);
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
