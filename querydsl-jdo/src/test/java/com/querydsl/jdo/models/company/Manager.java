/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.jdo.models.company;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.querydsl.core.annotations.QueryEntity;

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
