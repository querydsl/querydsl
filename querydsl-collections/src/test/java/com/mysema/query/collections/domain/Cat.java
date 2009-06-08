/**
 * 
 */
package com.mysema.query.collections.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.mysema.query.annotations.Entity;

@Entity
public class Cat extends Animal {
    private int breed;
    private Color eyecolor;
    private List<Cat> kittens;
    private Map<String, Cat> kittensByName;
    private Cat mate;

    public Cat() {
    }

    public Cat(String name) {
        Cat kitten = new Cat();        
        this.kittens = Arrays.asList(kitten);
        this.kittensByName = Collections.singletonMap("Kitty", kitten);
        this.name = name;
    }

    public Cat(String name, int id) {
        this(name);
        this.id = id;
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

    public Map<String, Cat> getKittensByName() {
        return kittensByName;
    }

    public Cat getMate() {
        return mate;
    }

    public String toString() {
        return name;
    }

    public void setBreed(int breed) {
        this.breed = breed;
    }

    public void setEyecolor(Color eyecolor) {
        this.eyecolor = eyecolor;
    }

    public void setKittens(List<Cat> kittens) {
        this.kittens = kittens;
    }

    public void setMate(Cat mate) {
        this.mate = mate;
    }

}