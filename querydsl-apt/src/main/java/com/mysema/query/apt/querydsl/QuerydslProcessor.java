package com.mysema.query.apt.querydsl;

import com.mysema.query.apt.general.GeneralProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;

/**
 * QureydslProcessor provides
 *
 * @author tiwe
 * @version $Id$
 */
public class QuerydslProcessor extends GeneralProcessor{

    public QuerydslProcessor(AnnotationProcessorEnvironment env) {
        super(env, null, qdEntity, qdDto);
    }

}
