package com.mysema.query.maven;

import javax.jdo.annotations.PersistenceCapable;

import com.mysema.query.annotations.QueryEntity;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@PersistenceCapable
@javax.persistence.Entity
@QueryEntity
public class Entity {

    String property;

    @Temporal(TemporalType.TIMESTAMP)
    Date annotatedProperty;
}
