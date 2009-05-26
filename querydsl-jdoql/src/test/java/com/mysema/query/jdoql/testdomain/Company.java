package com.mysema.query.jdoql.testdomain;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

/**
 * The Class Company.
 */
@PersistenceCapable
// TODO : finish annotations
public class Company {
    private @PrimaryKey
    int id;
    // @ManyToOne
    private Employee ceo;
    // @OneToMany(mappedBy="company")
    private List<Department> departments;
    private String name;

    public Company() {
    }

    public Company(int id) {
        setId(id);
    }

    public void addDepartment(Department department) {
        if (departments == null) {
            departments = new ArrayList<Department>();
        }
        departments.add(department);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Employee getCeo() {
        return ceo;
    }

    public void setCeo(Employee ceo) {
        this.ceo = ceo;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}