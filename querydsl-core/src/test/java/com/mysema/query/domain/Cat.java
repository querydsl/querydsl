/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The Class Cat.
 */
public class Cat extends Animal {

    private int breed;

    private List<Cat> kittens = new ArrayList<Cat>();

    private Set<Cat> kittensSet;

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

    public List<Cat> getKittens() {
        return kittens;
    }

    public Cat getMate() {
        return mate;
    }

    public void addKitten(Cat kitten) {
        kittens.add(kitten);
    }

    public Set<Cat> getKittensSet() {
        return kittensSet;
    }

    public void setKittensSet(Set<Cat> kittensSet) {
        this.kittensSet = kittensSet;
    }

}
