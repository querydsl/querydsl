package com.querydsl.jpa.domain;

import java.util.Collection;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

@Entity
public class Human extends Mammal {

    @ElementCollection
    Collection<Integer> hairs;
    
}
