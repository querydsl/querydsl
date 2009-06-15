/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.domain;

import javax.persistence.Entity;
import javax.persistence.Id;


/**
 * The Class Animal.
 */
@Entity
public class Animal {
    boolean alive;
    
    java.util.Date birthdate;
    
    int bodyWeight, weight, toes;
    
    Color color;
    
    @Id
    int id;
    
    String name;
}