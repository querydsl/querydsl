package com.mysema.query.domain;

import java.util.Collection;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Entity;

import org.junit.Ignore;

@Ignore
public class EmbeddableInterfaceTest {

    @Entity
    public class EntityClass {
        
        @ElementCollection(targetClass=EmbeddableClass.class)
        Collection<EmbeddableInterface> children;
        
    }
        
    public interface EmbeddableInterface {
        
    }
    
    @Embeddable
    public class EmbeddableClass implements EmbeddableInterface {
        
    }
    
}
