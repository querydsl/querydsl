/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt.hibernate;

import java.io.IOException;

import com.mysema.query.apt.general.GeneralProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;

/**
 * HibernateProcessor provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class HibernateProcessor extends GeneralProcessor {

    public HibernateProcessor(AnnotationProcessorEnvironment env)
            throws IOException {
        super(env,"javax.persistence.Entity");
    }
    
}