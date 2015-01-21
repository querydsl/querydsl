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

import java.util.HashMap;
import java.util.Map;

import com.querydsl.core.annotations.QueryEntity;

/**
 * Gymnasium.
 *
 * @version $Revision: 1.1 $
 */
@QueryEntity
public class Gym {
    private Map<String, String> codes;
    private String location;
    private String name;

    // this must be initialized in the constructor. dont change it
    private Map<String, Wardrobe> wardrobes; // store Wardrobe in values
    private Map<Wardrobe, String> wardrobes2; // store Wardrobe in keys
    private Map<String, Wardrobe> wardrobesInverse; // store Wardrobe in values
    private Map<Wardrobe, String> wardrobesInverse2; // store Wardrobe in keys

    private Map<String, GymEquipment> equipments; // store Equipments in values
    private Map<GymEquipment, String> equipments2; // store Equipments in keys
    private Map<String, GymEquipment> equipmentsInverse; // store Equipments in
                                                         // values
    private Map<GymEquipment, String> equipmentsInverse2; // store Equipments in
                                                          // keys

    private Map<String, Gym> partners; // store Gym in values
    private Map<Gym, String> partners2; // store Gym in keys
    private Map<String, Gym> partnersInverse; // store Gym in values
    private Map<Gym, String> partnersInverse2; // store Gym in keys

    private Gym gym;
    private Gym gym2;
    private String stringKey;
    private String stringValue;

    public Gym() {
        // this must be initialized in the constructor. dont change it
        wardrobes = new HashMap<String, Wardrobe>();
        equipments = new HashMap<String, GymEquipment>();
        partners = new HashMap<String, Gym>();
        wardrobes2 = new HashMap<Wardrobe, String>();
        equipments2 = new HashMap<GymEquipment, String>();
        partners2 = new HashMap<Gym, String>();
        wardrobesInverse = new HashMap<String, Wardrobe>();
        equipmentsInverse = new HashMap<String, GymEquipment>();
        partnersInverse = new HashMap<String, Gym>();
        wardrobesInverse2 = new HashMap<Wardrobe, String>();
        equipmentsInverse2 = new HashMap<GymEquipment, String>();
        partnersInverse2 = new HashMap<Gym, String>();
        codes = new HashMap<String, String>();
    }

    /**
     * @return Returns the location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location
     *            The location to set.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return Returns the wardrobes.
     */
    public Map<String, Wardrobe> getWardrobes() {
        return wardrobes;
    }

    /**
     * @param wardrobes
     *            The wardrobes to set.
     */
    public void setWardrobes(Map<String, Wardrobe> wardrobes) {
        this.wardrobes = wardrobes;
    }

    public Map<String, GymEquipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(Map<String, GymEquipment> equipments) {
        this.equipments = equipments;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPartners(Map<String, Gym> partners) {
        this.partners = partners;
    }

    public Map<String, Gym> getPartners() {
        return partners;
    }

    public Map<Gym, String> getPartners2() {
        return partners2;
    }

    public Map<GymEquipment, String> getEquipments2() {
        return equipments2;
    }

    public Map<Wardrobe, String> getWardrobes2() {
        return wardrobes2;
    }

    public Map<String, Wardrobe> getWardrobesInverse() {
        return wardrobesInverse;
    }

    public Map<Wardrobe, String> getWardrobesInverse2() {
        return wardrobesInverse2;
    }

    public Map<String, GymEquipment> getEquipmentsInverse() {
        return equipmentsInverse;
    }

    public Map<GymEquipment, String> getEquipmentsInverse2() {
        return equipmentsInverse2;
    }

    public Map<String, Gym> getPartnersInverse() {
        return partnersInverse;
    }

    public Map<Gym, String> getPartnersInverse2() {
        return partnersInverse2;
    }

    public Gym getGym() {
        return gym;
    }

    public Gym getGym2() {
        return gym2;
    }

    public String getStringKey() {
        return stringKey;
    }

    public String getStringValue() {
        return stringValue;
    }

    public Map<String, String> getCodes() {
        return codes;
    }

    public void setCodes(Map<String, String> codes) {
        this.codes = codes;
    }
}
