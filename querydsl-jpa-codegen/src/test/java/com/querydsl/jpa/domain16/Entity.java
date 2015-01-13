package com.querydsl.jpa.domain16;

import com.querydsl.core.annotations.PropertyType;
import com.querydsl.core.annotations.QueryType;

public class Entity {

    private Long id;

    @QueryType(PropertyType.SIMPLE)
    private Custom custom;

    @QueryType(PropertyType.ENTITY)
    private Custom2 custom2;

    private Custom3 custom3;

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

    public Custom2 getCustom2() {
        return custom2;
    }

    public void setCustom2(Custom2 custom2) {
        this.custom2 = custom2;
    }

    public Custom3 getCustom3() {
        return custom3;
    }

    public void setCustom3(Custom3 custom3) {
        this.custom3 = custom3;
    }

}
