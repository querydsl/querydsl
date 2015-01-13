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

    public Cat() {}

    public Cat(String name, int id) {
        this.setId(id);
        this.setName( name);
    }

    public Cat(String name, int id, double bodyWeight) {
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
