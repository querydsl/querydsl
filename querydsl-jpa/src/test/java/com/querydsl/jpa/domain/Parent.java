package com.querydsl.jpa.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name="Parent2")
public class Parent {

    @Id
    int id;
    
    @OneToMany(mappedBy = "parent")  
    Set<Child> children;
    
}
