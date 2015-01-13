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
package com.querydsl.core.domain;


/**
 * The Class Animal.
 */
public class Animal {
    private boolean alive;

    private java.util.Date birthdate;

    private int weight, toes;

    private double bodyWeight;

    private java.sql.Date dateField;

    private int id;

    private String name;

    private java.sql.Time timeField;

    public java.util.Date getBirthdate() {
        return new java.util.Date(birthdate.getTime());
    }

    public double getBodyWeight() {
        return bodyWeight;
    }


    public java.sql.Date getDateField() {
        return new java.sql.Date(dateField.getTime());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public java.sql.Time getTimeField() {
        return timeField;
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
        this.birthdate = new java.util.Date(birthdate.getTime());
    }

    public void setBodyWeight(double bodyWeight) {
        this.bodyWeight = bodyWeight;
    }

    public void setDateField(java.sql.Date dateField) {
        this.dateField = new java.sql.Date(dateField.getTime());
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimeField(java.sql.Time timeField) {
        this.timeField = timeField;
    }

    public void setToes(int toes) {
        this.toes = toes;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

}
