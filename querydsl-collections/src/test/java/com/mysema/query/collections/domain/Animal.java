/**
 * 
 */
package com.mysema.query.collections.domain;

import com.mysema.query.annotations.Entity;

@Entity
public class Animal {
    protected boolean alive;
    protected java.util.Date birthdate = new java.util.Date();
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
        this.birthdate = birthdate;
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

}