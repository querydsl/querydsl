package com.mysema.query.jdoql.models.company;

import com.mysema.query.annotations.Entity;


/**
 * Project in a company.
 * 
 * @version $Revision: 1.2 $
 */
@Entity
public class Project {
    String name; // PK when app id
    long budget;
    
    public Project(){}

    public Project(String name, long budget) {
        super();
        this.name = name;
        this.budget = budget;
    }

    public long getBudget() {
        return budget;
    }

    public void setBudget(long budget) {
        this.budget = budget;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}