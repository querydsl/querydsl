/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.r2dbc.dml;

import com.querydsl.r2dbc.domain.Employee;
import com.querydsl.sql.Column;
import org.junit.Before;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

public abstract class AbstractMapperTest {

    public static class EmployeeX {

        private String property;

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }
    }

    public static class EmployeeNames {

        @Column("ID")
        Integer _id;

        @Column("FIRSTNAME")
        String _firstname;

        @Column("LASTNAME")
        String _lastname;
    }

    protected Employee employee;

    @Before
    public void setUp() {
        employee = new Employee();
        employee.setDatefield(new Date(0));
        employee.setFirstname("A");
        employee.setLastname("B");
        employee.setSalary(new BigDecimal(1.0));
        employee.setSuperiorId(2);
        employee.setTimefield(new Time(0));
    }


}
