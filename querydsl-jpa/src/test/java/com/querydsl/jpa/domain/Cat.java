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
package com.querydsl.jpa.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

/**
 * The Class Cat.
 */
@Entity
@DiscriminatorValue("C")
public class Cat extends Animal {

    private int breed;

    private Color eyecolor;

    @OneToMany
    @JoinTable(name = "kittens", joinColumns = @JoinColumn(name = "cat_id"), inverseJoinColumns = @JoinColumn(name = "kitten_id"))
    @OrderColumn(name = "ind")
    private List<Cat> kittens = new ArrayList<Cat>();

    @OneToMany
    @JoinTable(name = "kittens_set", joinColumns = @JoinColumn(name = "cat_id"), inverseJoinColumns = @JoinColumn(name = "kitten_id"))
    private Set<Cat> kittensSet;

//    @OneToMany
//    @JoinTable(name="kittens_array")
//    @IndexColumn(name = "arrayIndex")
//    private Cat[] kittensArray = new Cat[0];

    @ManyToOne
    private Cat mate;

    public Cat() {}

    public Cat(int id) {
        setId(id);
    }

    public Cat(String name, int id) {
        setId(id);
        setName( name);
    }

    public Cat(String name, int id, Color color) {
        setId(id);
        setName( name);
        setColor(color);
    }

    public Cat(String name, int id, List<Cat> k) {
        setId(id);
        setName( name);
        kittens.addAll(k);
    }

    public Cat(String name, int id, double bodyWeight) {
        this(name, id);
        setBodyWeight(bodyWeight);
        setFloatProperty((float)bodyWeight);
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

