package com.mysema.query.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;

import org.junit.Ignore;

@Ignore
public class ExternalEmbeddableTest {

    @Entity
    public class EntityWithExternalEmbeddable {
        
        @Embedded
        EmbeddableWithoutQType embeddable;
        
    }
    
}
