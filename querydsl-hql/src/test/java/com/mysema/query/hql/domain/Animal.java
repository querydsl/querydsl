/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * The Class Animal.
 */
@Entity
public class Animal {
    private boolean alive;

    private java.util.Date birthdate;

    private int bodyWeight, weight, toes;

    private Color color;

    private java.sql.Date dateField;

    @Id
    private int id;

    private String name;

    private java.sql.Time timeField;

    public java.util.Date getBirthdate() {
        return birthdate;
    }

    public int getBodyWeight() {
        return bodyWeight;
    }

    public Color getColor() {
        return color;
    }

    public java.sql.Date getDateField() {
        return dateField;
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
        this.birthdate = birthdate;
    }

    public void setBodyWeight(int bodyWeight) {
        this.bodyWeight = bodyWeight;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setDateField(java.sql.Date dateField) {
        this.dateField = dateField;
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