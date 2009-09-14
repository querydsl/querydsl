/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql.models.fitness;

import com.mysema.query.annotations.Entity;


/**
 * Piece of equipment in a Gym.
 * 
 * @version $Revision: 1.1 $
 */
@Entity
public class GymEquipment {
    String name;
    private Gym gym;
    private String stringKey;
    private String stringValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gym getGym() {
        return gym;
    }

    public String getStringKey() {
        return stringKey;
    }

    public String getStringValue() {
        return stringValue;
    }
}