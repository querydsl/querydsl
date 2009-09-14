/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql.models.company;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mysema.query.annotations.Entity;

/**
 * Department in a company. Has a Manager, and a set of Projects being worked
 * on.
 * 
 * @version $Revision: 1.1 $
 */
@Entity
public class Department {
    private String name;
    private Manager manager;
    private Set<Project> projects = new HashSet<Project>();
    private List<Employee> employees;
    
    public Department(){
        
    }

    public Department(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setManager(Manager mgr) {
        this.manager = mgr;
    }

    public Manager getManager() {
        return this.manager;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public void addProject(Project proj) {
        this.projects.add(proj);
    }

    public String toString() {
        return name;
    }

    public List<Employee> getEmployees() {
        return employees;
    }
    
    
}