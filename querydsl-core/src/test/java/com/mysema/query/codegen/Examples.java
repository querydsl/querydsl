package com.mysema.query.codegen;

import com.mysema.query.annotations.QueryEntity;

public class Examples {
    
    public static class Supertype {
        
        String supertypeProperty;
    }
    
    @QueryEntity
    public static class SimpleEntity extends Supertype{

    }

    @QueryEntity
    public static abstract class AbstractEntity<Id extends java.io.Serializable> {

        Id id;

        String first;

    }

    @QueryEntity
    public static class SubEntity extends AbstractEntity<java.lang.Long> {
     
        String second;
     
    } 

}
