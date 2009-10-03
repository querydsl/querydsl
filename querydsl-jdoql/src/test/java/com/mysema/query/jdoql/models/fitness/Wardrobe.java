/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql.models.fitness;

import java.util.ArrayList;
import java.util.List;

import com.mysema.query.annotations.QueryEntity;

/**
 * Container for clothes in a Gym.
 * 
 * @version $Revision: 1.1 $
 */
@QueryEntity
public class Wardrobe {
    // this must be initialized in the constructor. dont change it
    private List<Cloth> clothes;
    private String model;
    private Gym gym;
    private String stringKey;
    private String stringValue;

    public Wardrobe() {
        // this must be initialized in the constructor. dont change it
        clothes = new ArrayList<Cloth>();
    }

    public List<Cloth> getClothes() {
        return clothes;
    }

    public void setClothes(ArrayList<Cloth> clothes) {
        this.clothes = clothes;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
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