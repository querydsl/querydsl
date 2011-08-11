package com.mysema.query.domain;

import static org.junit.Assert.*;

import java.util.Collection;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Entity;

import org.junit.Test;

public class EmbeddableInterfaceTest {

    @Entity
    public class EntityClass {
        
        @ElementCollection(targetClass=EmbeddableClass.class)
        Collection<EmbeddableInterface> children;
        
    }
        
    @Embeddable
    public interface EmbeddableInterface {
        
    }
    
    @Embeddable
    public class EmbeddableClass implements EmbeddableInterface {
        
    }
    
    @Test
    public void test() {
        assertEquals(
            QEmbeddableInterfaceTest_EmbeddableInterface.class, 
            QEmbeddableInterfaceTest_EntityClass.entityClass.children.any().getClass());
    }
    
}
