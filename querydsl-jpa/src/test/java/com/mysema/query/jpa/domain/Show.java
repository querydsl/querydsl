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

/**
 * The Class Show.
 */
@Entity
public class Show {
    @ElementCollection
    public Map<String, String> acts;

    @Id
    public int id;
    
    public Show() {}
        
    public Show(int id) {
        this.id = id;
    }
    
}
