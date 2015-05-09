package com.querydsl.maven;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.querydsl.core.annotations.QueryEntity;

@PersistenceCapable
@javax.persistence.Entity
@QueryEntity
public class Entity {

    String property;

    @Temporal(TemporalType.TIMESTAMP)
    Date annotatedProperty;
}
