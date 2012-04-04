package com.mysema.query.codegen;

import org.junit.Ignore;

import com.mysema.query.annotations.QueryEmbedded;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.domain.EmbeddableWithoutQType;

@Ignore
public class ExternalEmbeddableTest {

    @QueryEntity
    public static class EntityWithExternalEmbeddable {
        
        @QueryEmbedded
        EmbeddableWithoutQType embeddable;
        
    }
    
}
