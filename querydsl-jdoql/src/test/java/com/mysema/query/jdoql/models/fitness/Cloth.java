/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql.models.fitness;

import com.mysema.query.annotations.Entity;


/**
 * Item of clothing in a Gym.
 * 
 * @version $Revision: 1.1 $
 */
@Entity
public class Cloth {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}