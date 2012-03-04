package com.mysema.query.domain;

import com.mysema.query.annotations.QueryEntity;

public class Examples {
    
    @QueryEntity
    public static class SimpleEntity {

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
