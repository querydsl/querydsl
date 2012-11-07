package com.mysema.query.collections;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryProjection;

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
