package com.querydsl.jpa.domain;

import javax.persistence.Entity;

@Entity
public class Entity2 extends Entity1 {
    
    public Entity2() {}
    
    public Entity2(int id) {
        this.id = id;
    }

    public String property2;
}
