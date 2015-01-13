package com.querydsl.collections;

import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QueryProjection;

@QueryEntity
public class EntityWithLongId {

    private Long id;

    @QueryProjection
    public EntityWithLongId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
