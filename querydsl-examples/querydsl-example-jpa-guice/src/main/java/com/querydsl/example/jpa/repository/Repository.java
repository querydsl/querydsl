package com.querydsl.example.jpa.repository;

import java.io.Serializable;

public interface Repository<Entity, Id extends Serializable> {
    /**
     * Get the persisted instance with the given id
     *
     * @param id
     * @return
     */
    Entity findById(Id id);

}