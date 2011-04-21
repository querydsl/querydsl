/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.domain;

import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The Class Show.
 */
@Entity
@Table(name="show_")
public class Show {

    @Id
    long id;
    
    @ElementCollection
    public Map<String, String> acts;
    
    public Show() {}
        
    public Show(int id) {
        this.id = id;
    }
    
}
