/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql.testdomain;

import java.util.Map;
import java.util.Set;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

/**
 * The Class Department.
 */
@PersistenceCapable
// TODO : finish annotations
public class Department {
    private @PrimaryKey
    int id;
    // @ManyToOne
    private Company company;
    // @OneToMany(mappedBy="department")
    private Set<Employee> employees;
    private Map<String,Employee> employeesByUserName;
    private String name;
    // @ManyToOne
    private Department parent;

    public Department() {
    }

    public Department(int id, Company c) {
        setId(id);
        setCompany(c);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
        company.addDepartment(this);
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }
    
    public Map<String, Employee> getEmployeesByUserName() {
        return employeesByUserName;
    }

    public void setEmployeesByUserName(Map<String, Employee> employeesByUserName) {
        this.employeesByUserName = employeesByUserName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Department getParent() {
        return parent;
    }

    public void setParent(Department parent) {
        this.parent = parent;
    }

}