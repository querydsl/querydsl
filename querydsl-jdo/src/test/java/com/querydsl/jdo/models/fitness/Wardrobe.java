/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.jdo.models.fitness;

import java.util.ArrayList;
import java.util.List;

import com.querydsl.core.annotations.QueryEntity;

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
