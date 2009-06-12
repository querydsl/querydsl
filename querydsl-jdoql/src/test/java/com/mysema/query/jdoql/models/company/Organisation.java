package com.mysema.query.jdoql.models.company;

import com.mysema.query.annotations.Entity;


/**
 * Organisation that hands out qualifications to employees after taking training
 * courses
 * 
 * @version $Revision: 1.1 $
 */
@Entity
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