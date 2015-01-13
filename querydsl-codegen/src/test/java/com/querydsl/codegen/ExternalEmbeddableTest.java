package com.querydsl.codegen;

import org.junit.Ignore;

import com.querydsl.core.annotations.QueryEmbedded;
import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.domain.EmbeddableWithoutQType;

@Ignore
public class ExternalEmbeddableTest {

    @QueryEntity
    public static class EntityWithExternalEmbeddable {
        
        @QueryEmbedded
        EmbeddableWithoutQType embeddable;
        
    }
    
}
