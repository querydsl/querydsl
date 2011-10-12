package com.mysema.query.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.junit.Test;

public class EmbeddedTest {
    
    @Entity
    public class EntityClass extends AbstractEntity<SubEntityCode> {
        
    }
    
    @MappedSuperclass
    public abstract class AbstractEntity<C extends EntityCode> {

        @Embedded
        @Column(name = "code", nullable = false, unique = true)
        C code;
    }

    @MappedSuperclass
    public class EntityCode {

        @Column(name = "code", unique = true)
        String code;

    }
    
    @Embeddable
    public class SubEntityCode extends EntityCode {
        
        String property;
        
    }
    
    @Test
    public void EntityClass() {
        assertNotNull(QEmbeddedTest_EntityClass.entityClass.code.property);
        assertEquals(SubEntityCode.class, QEmbeddedTest_EntityClass.entityClass.code.getType());
    }
    

}
