package com.mysema.query.domain;

import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.annotations.QueryEmbedded;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QuerySupertype;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.path.PathInits;

public class Embeddable2Test {
    
    @QuerySupertype
    public abstract class SomeMappedSuperClassHavingMyEmbeddable {

        @QueryEmbedded
        MyEmbeddable embeddable;
    }
    
    @QueryEntity
    public abstract class SomeEntityClassHavingMyEmbeddable {
        
        @QueryEmbedded
        MyEmbeddable embeddable;
        
    }
    
    @QueryEntity
    public class SomeEntity extends SomeMappedSuperClassHavingMyEmbeddable {
        
    }
    
    @Test
    @Ignore
    public void MappedSuperClassConstructors() throws SecurityException, NoSuchMethodException {
        assertNotNull(QEmbeddable2Test_SomeMappedSuperClassHavingMyEmbeddable.class.getConstructor(
                Class.class, PathMetadata.class, PathInits.class));
    }
    
    @Test
    @Ignore
    public void EntityConstructors() throws SecurityException, NoSuchMethodException {
        assertNotNull(QEmbeddable2Test_SomeEntityClassHavingMyEmbeddable.class.getConstructor(
                Class.class, PathMetadata.class, PathInits.class));
    }

}
