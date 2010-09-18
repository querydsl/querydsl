/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jdo.models.company;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.mysema.query.annotations.QueryEntity;

/**
 * Manager of a set of Employees, and departments.
 *
 * @version $Revision: 1.1 $
 */
@QueryEntity
public class Manager extends Employee {
    protected Set<Employee> subordinates;
    protected Set<Department> departments;

    /**
     * Default constructor required since this is a PersistenceCapable class.
     */
    protected Manager() {
    }

    public Manager(long id, String firstname, String lastname, String email,
            float salary, String serial) {
        super(id, firstname, lastname, email, salary, serial);
        this.departments = new HashSet<Department>();
        this.subordinates = new HashSet<Employee>();
    }

    public Set<Employee> getSubordinates() {
        return this.subordinates;
    }

    public void addSubordinate(Employee e) {
        this.subordinates.add(e);
    }

    public void removeSubordinate(Employee e) {
        this.subordinates.remove(e);
    }

    public void addSubordinates(Collection<Employee> c) {
        this.subordinates.addAll(c);
    }

    public void clearSubordinates() {
        this.subordinates.clear();
    }

    public Set<Department> getDepartments() {
        return this.departments;
    }

    public void addDepartment(Department d) {
        this.departments.add(d);
    }

    public void removeDepartment(Department d) {
        this.departments.remove(d);
    }

    public void clearDepartments() {
        this.departments.clear();
    }

}
