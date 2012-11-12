package com.mysema.query.codegen;

import com.mysema.query.annotations.QueryEmbeddable;
import com.mysema.query.annotations.QueryEmbedded;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QuerySupertype;

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