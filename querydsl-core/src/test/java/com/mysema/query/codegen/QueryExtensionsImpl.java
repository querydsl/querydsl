/**
 * 
 */
package com.mysema.query.codegen;

import java.lang.annotation.Annotation;

import com.mysema.query.annotations.QueryExtensions;

@SuppressWarnings("all")
public class QueryExtensionsImpl implements QueryExtensions{

    private final Class<?> value;

    public QueryExtensionsImpl(Class<?> value){
        this.value = value;
    }

    @Override
    public Class<?> value() {
        return value;
    }
    @Override
    public Class<? extends Annotation> annotationType() {
        return QueryExtensions.class;
    }
}