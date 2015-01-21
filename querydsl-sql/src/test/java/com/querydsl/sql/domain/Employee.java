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
package com.querydsl.sql.domain;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

//@Schema("PUBLIC")
//@Table("EMPLOYEE")
public class Employee {

    //@Column("ID")
    private Integer id;
    
    //@Column("FIRSTNAME")
    private String firstname;
    
    //@Column("LASTNAME")
    private String lastname;
    
    //@Column("SALARY")
    private BigDecimal salary;
    
    //@Column("DATEFIELD")
    private Date datefield;
    
    //@Column("TIMEFIELD")
    private Time timefield;
    
    //@Column("SUPERIOR_ID")
    private Integer superiorId;

    public Employee() {}
    
    public Employee(int id) {
        this.id = id;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
        return new Date(datefield.getTime());
    }

    public void setDatefield(Date datefield) {
        this.datefield = new Date(datefield.getTime());
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
