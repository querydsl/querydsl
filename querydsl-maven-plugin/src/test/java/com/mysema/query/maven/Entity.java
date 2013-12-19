package com.mysema.query.maven;

import javax.jdo.annotations.PersistenceCapable;

import com.mysema.query.annotations.QueryEntity;

@PersistenceCapable
@javax.persistence.Entity
@QueryEntity
public class Entity {

    String property;

}
