package com.mysema.query.domain;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.mysema.query.annotations.QueryEmbeddable;
import com.mysema.query.types.PathMetadataFactory;

public class Superclass5Test {
    
    public static class SuperClass {
        
        String superClassProperty;
        
    }
    
    @QueryEmbeddable
    public static class Embeddable extends SuperClass {
        
        String embeddableProperty;
        
    }

    @Test
    public void SuperClass_Properties() {
        QSuperclass5Test_SuperClass qtype = new QSuperclass5Test_SuperClass(PathMetadataFactory.forVariable("var"));
        assertNotNull(qtype.superClassProperty);
    }
    
    @Test
    public void Entity_Properties() {
        QSuperclass5Test_Embeddable qtype = new QSuperclass5Test_Embeddable(PathMetadataFactory.forVariable("var"));
        assertNotNull(qtype.superClassProperty);
        assertNotNull(qtype.embeddableProperty);
    }

}
