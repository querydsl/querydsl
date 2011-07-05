package com.mysema.query.domain;

import org.junit.Ignore;

import com.mysema.query.annotations.QueryEmbedded;
import com.mysema.query.annotations.QueryEntity;

@Ignore
public class ExternalEmbeddableTest {

    @QueryEntity
    public class EntityWithExternalEmbeddable {
        
        @QueryEmbedded
        EmbeddableWithoutQType embeddable;
        
    }
    
}
