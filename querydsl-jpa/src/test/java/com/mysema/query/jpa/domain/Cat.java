/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.IndexColumn;

/**
 * The Class Cat.
 */
@Entity
@DiscriminatorValue("C")
public class Cat extends Animal {

    private int breed;

    private Color eyecolor;

    @OneToMany
    @JoinTable(name="kittens")
    @IndexColumn(name = "ind")
    private List<Cat> kittens = new ArrayList<Cat>();

    @OneToMany
    @JoinTable(name="kittens_set")
    private Set<Cat> kittensSet;

//    @OneToMany
//    @JoinTable(name="kittens_array")
//    @IndexColumn(name = "arrayIndex")
//    private Cat[] kittensArray = new Cat[0];

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

//    public Cat[] getKittensArray() {
//        return kittensArray;
//    }

    public void addKitten(Cat kitten) {
        kittens.add(kitten);
//        kittensArray = new Cat[]{kitten};
    }

    public Set<Cat> getKittensSet() {
        return kittensSet;
    }

    public void setKittensSet(Set<Cat> kittensSet) {
        this.kittensSet = kittensSet;
    }

}
