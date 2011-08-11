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
     
        String getName();
    }
    
    @Embeddable
    public class EmbeddableClass implements EmbeddableInterface {

        @Override
        public String getName() {
            return null;
        }
        
    }
    
    @Test
    public void Type() {
        assertEquals(
            QEmbeddableInterfaceTest_EmbeddableInterface.class, 
            QEmbeddableInterfaceTest_EntityClass.entityClass.children.any().getClass());
    }
    
    @Test
    public void Properties() {
        assertNotNull(QEmbeddableInterfaceTest_EmbeddableInterface.embeddableInterface.name);
        assertNotNull(QEmbeddableInterfaceTest_EmbeddableClass.embeddableClass.name);
    }
    
}
