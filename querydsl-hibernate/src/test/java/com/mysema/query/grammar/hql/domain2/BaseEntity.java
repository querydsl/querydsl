package com.mysema.query.grammar.hql.domain2;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;


/**
 * BaseEntity is the top of entity hierarchy. (see thinglink-2.0 datamodel)
 * 
 */
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long _id;

    /**
     * @return The id of the entity.
     */
    public Long getId() {
        return _id;
    }
}
