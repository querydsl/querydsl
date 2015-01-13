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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.querydsl.core.annotations.QueryEntity;

/**
 * Department in a company. Has a Manager, and a set of Projects being worked
 * on.
 *
 * @version $Revision: 1.1 $
 */
@QueryEntity
public class Department {
    private String name;
    private Manager manager;
    private Set<Project> projects = new HashSet<Project>();
    private List<Employee> employees;

    public Department() {

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
