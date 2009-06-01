/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql.testdomain;

import javax.jdo.annotations.PersistenceCapable;

/**
 * The Class Employee.
 */
@PersistenceCapable
// TODO : finish annotations
public class Employee extends Person {
    // @ManyToOne
    private Company company;
    // @ManyToOne
    private Department department;
    // @ManyToOne
    private Employee superior;

    public Employee() {
    }

    public Employee(int i) {
        setId(i);
    }

    public Employee(int i, Employee superior) {
        setId(i);
        setSuperior(superior);
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Employee getSuperior() {
        return superior;
    }

    public void setSuperior(Employee superior) {
        this.superior = superior;
    }

}