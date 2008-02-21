package com.mysema.query.test.domain;

import java.util.Collection;

/**
 * Cat provides
 *
 * @author tiwe
 * @version $Id$
 */
public class Cat {
//    private Cat kittens, mate;
//    public final NumberProperty bodyWeight = num("bodyWeight");
//    public final StringProperty name = str("name");
    private Collection<Cat> kittens;
    private Cat mate;
    private int bodyWeight;
    private String name;
    private boolean alive;
    public Collection<Cat> getKittens() {
        return kittens;
    }
    public void setKittens(Collection<Cat> kittens) {
        this.kittens = kittens;
    }
    public Cat getMate() {
        return mate;
    }
    public void setMate(Cat mate) {
        this.mate = mate;
    }
    public int getBodyWeight() {
        return bodyWeight;
    }
    public void setBodyWeight(int bodyWeight) {
        this.bodyWeight = bodyWeight;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isAlive() {
        return alive;
    }
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    
}
