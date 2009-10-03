/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql.models.company;

import com.mysema.query.annotations.QueryEntity;


/**
 * Organisation that hands out qualifications to employees after taking training
 * courses
 * 
 * @version $Revision: 1.1 $
 */
@QueryEntity
public class Organisation {
    String name;
    
    public Organisation(){}

    public Organisation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}