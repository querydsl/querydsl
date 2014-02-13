package com.mysema.query.jpa.domain16;

import com.mysema.query.annotations.PropertyType;
import com.mysema.query.annotations.QueryType;

public class Entity {

    private Long id;

    @QueryType(PropertyType.SIMPLE)
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
