package com.querydsl.codegen;

import com.querydsl.core.annotations.QueryEmbeddable;
import com.querydsl.core.annotations.QueryEmbedded;
import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QuerySupertype;

public class EmbeddedTest extends AbstractExporterTest {
    
    @QueryEntity
    public static class EntityClass extends AbstractEntity<SubEntityCode> {
        
    }
    
    @QuerySupertype
    public static abstract class AbstractEntity<C extends EntityCode> {

        @QueryEmbedded
        public C code;
    }

    @QuerySupertype
    public static class EntityCode {

        public String code;

    }
    
    @QueryEmbeddable
    public static class SubEntityCode extends EntityCode {
        
        public String property;
        
    }
    
}    