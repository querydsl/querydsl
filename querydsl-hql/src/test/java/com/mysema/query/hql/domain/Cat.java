/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.IndexColumn;

/**
 * The Class Cat.
 */
@Entity
public class Cat extends Animal {
    
    private int breed;
    
    private Color eyecolor;
    
    @OneToMany
    @IndexColumn(name = "ind")
    private List<Cat> kittens = new ArrayList<Cat>();
    
    @ManyToOne
    private Cat mate;

    public Cat(){}
    
    public Cat(String name, int id){
        this.setId(id);
        this.setName( name);
    }
    
    public Cat(String name, int id, double bodyWeight){
        this(name, id);
        this.setBodyWeight(bodyWeight);
    }
    
    public int getBreed() {
        return breed;
    }

    public Color getEyecolor() {
        return eyecolor;
    }

    public List<Cat> getKittens() {
        return kittens;
    }

    public Cat getMate() {
        return mate;
    }
}