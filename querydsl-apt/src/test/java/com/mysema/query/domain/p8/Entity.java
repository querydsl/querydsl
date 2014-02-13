package com.mysema.query.domain.p8;

import com.mysema.query.annotations.QueryEntity;

@QueryEntity
public class Entity {

    private Long id;

    private Custom custom;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Custom getCustom() {
        return custom;
    }

    public void setCustom(Custom custom) {
        this.custom = custom;
    }

}
