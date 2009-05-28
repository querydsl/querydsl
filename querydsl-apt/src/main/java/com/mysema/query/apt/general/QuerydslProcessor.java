/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt.general;

import static com.mysema.query.apt.Constants.QD_DTO;
import static com.mysema.query.apt.Constants.QD_ENTITY;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.MethodDeclaration;

/**
 * QureydslProcessor provides Querydsl annotation handling support
 * 
 * @author tiwe
 * @version $Id$
 */
public class QuerydslProcessor extends GeneralProcessor {

    public QuerydslProcessor(AnnotationProcessorEnvironment env) {
        super(env, null, QD_ENTITY, QD_DTO);
    }

    @Override
    protected DefaultEntityVisitor createEntityVisitor() {
        return new DefaultEntityVisitor() {
            @Override
            public void visitMethodDeclaration(MethodDeclaration d) {
                // skip property handling
            }
        };
    }

}
