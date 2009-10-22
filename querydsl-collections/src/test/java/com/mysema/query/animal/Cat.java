/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.animal;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mysema.query.annotations.PropertyType;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryProjection;
import com.mysema.query.annotations.QueryType;

@QueryEntity
public class Cat extends Animal {
    
    private int breed;
    
    private java.sql.Date dateField;
    
    private Color eyecolor;

    private List<Cat> kittens;

    private Map<String, Cat> kittensByName;

    private Cat mate;

    @QueryType(PropertyType.NONE)    
    private String skippedField;

    @QueryType(PropertyType.SIMPLE)    
    private String stringAsSimple;
    
    private java.sql.Time timeField;
    
    public Cat() {
        this.kittensByName = Collections.emptyMap();
    }
    
    public Cat(String name) {
        Cat kitten = new Cat();
        this.kittens = Arrays.asList(kitten);
        this.kittensByName = Collections.singletonMap("Kitty", kitten);
        this.name = name;
    }
    
    @QueryProjection
    public Cat(String name, int id) {
        this(name);
        this.id = id;
    }
    
    public Cat(String name, int id, Date birthdate) {
        this(name, id);
        this.birthdate = birthdate;
        this.dateField = new java.sql.Date(birthdate.getTime());
        this.timeField = new java.sql.Time(birthdate.getTime());
    }
    
    public int getBreed() {
        return breed;
    }

    public java.sql.Date getDateField() {
        return dateField;
    }
    
    public Color getEyecolor() {
        return eyecolor;
    }
    
    public List<Cat> getKittens() {
        return kittens;
    }

    public Map<String, Cat> getKittensByName() {
        return kittensByName;
    }

    public Cat getMate() {
        return mate;
    }
    
    public String getSkippedField() {
        return skippedField;
    }

    public String getStringAsSimple() {
        return stringAsSimple;
    }

    public java.sql.Time getTimeField() {
        return timeField;
    }

    public void setBreed(int breed) {
        this.breed = breed;
    }

    public void setDateField(java.sql.Date dateField) {
        this.dateField = dateField;
    }

    public void setEyecolor(Color eyecolor) {
        this.eyecolor = eyecolor;
    }

    public void setKittens(List<Cat> kittens) {
        this.kittens = kittens;
    }

    public void setKittensByName(Map<String, Cat> kittensByName) {
        this.kittensByName = kittensByName;
    }

    public void setMate(Cat mate) {
        this.mate = mate;
    }

    public void setSkippedField(String skippedField) {
        this.skippedField = skippedField;
    }

    public void setStringAsSimple(String stringAsSimple) {
        this.stringAsSimple = stringAsSimple;
    }

    public void setTimeField(java.sql.Time timeField) {
        this.timeField = timeField;
    }
    
    public String toString() {
        return name;
    }
    
    

}