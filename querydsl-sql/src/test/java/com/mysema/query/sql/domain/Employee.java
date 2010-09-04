/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.domain;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

import com.mysema.query.sql.Column;
import com.mysema.query.sql.Table;

@Table("EMPLOYEE2")
public class Employee {

    @Column("ID")
    private int id;
    
    @Column("FIRSTNAME")
    private String firstname;
    
    @Column("LASTNAME")
    private String lastname;
    
    @Column("SALARY")
    private BigDecimal salary;
    
    @Column("DATEFIELD")
    private Date datefield;
    
    @Column("TIMEFIELD")
    private Time timefield;
    
    @Column("SUPERIOR_ID")
    private Integer superiorId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Date getDatefield() {
        return datefield;
    }

    public void setDatefield(Date datefield) {
        this.datefield = datefield;
    }

    public Time getTimefield() {
        return timefield;
    }

    public void setTimefield(Time timefield) {
        this.timefield = timefield;
    }

    public Integer getSuperiorId() {
        return superiorId;
    }

    public void setSuperiorId(Integer superiorId) {
        this.superiorId = superiorId;
    }
    
    
    
}
