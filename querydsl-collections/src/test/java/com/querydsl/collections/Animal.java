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
package com.querydsl.collections;

import java.util.Date;

import com.querydsl.core.annotations.PropertyType;
import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QueryType;

@QueryEntity
public class Animal {

    protected boolean alive;

    protected java.util.Date birthdate = new java.util.Date();

    @QueryType(PropertyType.SIMPLE)
    private Date dateAsSimple;

    protected int bodyWeight, weight, toes;

    protected Color color;

    protected int id;

    protected String name;

    public java.util.Date getBirthdate() {
        return birthdate;
    }

    public int getBodyWeight() {
        return bodyWeight;
    }

    public Color getColor() {
        return color;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getToes() {
        return toes;
    }

    public int getWeight() {
        return weight;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setBirthdate(java.util.Date birthdate) {
        this.birthdate = new Date(birthdate.getTime());
    }

    public void setBodyWeight(int bodyWeight) {
        this.bodyWeight = bodyWeight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setToes(int toes) {
        this.toes = toes;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateAsSimple() {
        return dateAsSimple;
    }

    public void setDateAsSimple(Date dateAsSimple) {
        this.dateAsSimple = new Date(dateAsSimple.getTime());
    }

}
