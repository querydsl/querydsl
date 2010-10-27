/**
 * 
 */
package com.mysema.query.codegen;

import java.lang.annotation.Annotation;

import com.mysema.query.annotations.QueryEntity;

@SuppressWarnings("all")
public class QueryEntityImpl implements QueryEntity{

    @Override
    public Class<? extends Annotation> annotationType() {
        return QueryEntity.class;
    }

}