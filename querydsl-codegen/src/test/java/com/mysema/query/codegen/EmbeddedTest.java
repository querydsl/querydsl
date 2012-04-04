package com.mysema.query.codegen;

import org.junit.Ignore;

import com.mysema.query.annotations.QueryEmbeddable;
import com.mysema.query.annotations.QueryEmbedded;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QuerySupertype;

@Ignore
public class EmbeddedTest {
    
    @QueryEntity
    public static class EntityClass extends AbstractEntity<SubEntityCode> {
        
    }
    
    @QuerySupertype
    public static abstract class AbstractEntity<C extends EntityCode> {

        @QueryEmbedded
        C code;
    }

    @QuerySupertype
    public static class EntityCode {

        String code;

    }
    
    @QueryEmbeddable
    public static class SubEntityCode extends EntityCode {
        
        String property;
        
    }
    
}    