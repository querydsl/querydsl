package com.querydsl.jpa.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class World {

    @Id
    Long id;
    
    @OneToMany
    Set<Mammal> mammals;
    
}
