package com.mysema.query.domain;

import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.roo.addon.jpa.entity.RooJpaEntity;

public class RooEntities {
    
    @RooJpaEntity
    public static class MyEntity {
        
        @Id 
        Long id;
        
        String name;
        
        @ManyToOne
        MyEntity entity;
    }

}
