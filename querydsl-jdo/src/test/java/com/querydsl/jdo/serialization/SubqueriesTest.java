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
package com.querydsl.jdo.serialization;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.querydsl.jdo.models.company.QDepartment;
import com.querydsl.jdo.models.company.QEmployee;

public class SubqueriesTest extends AbstractTest{

    private QDepartment department = QDepartment.department;

    private QDepartment d = new QDepartment("d");

    private QEmployee e = new QEmployee("e");

    private QEmployee employee = QEmployee.employee;

/*  "SELECT FROM " + Department.class.getName() + " WHERE this.employees.size() == " +
 *  "(SELECT MAX(d.employees.size()) FROM " + Department.class.getName() + " d)"; */
    @Test
    public void test1() {
        assertEquals(
          "SELECT FROM com.querydsl.jdo.models.company.Department " +
          "WHERE this.employees.size() == " +
          "(SELECT max(d.employees.size()) FROM com.querydsl.jdo.models.company.Department d)",

          serialize(query().from(department).where(department.employees.size().eq(
               query().from(d).unique(d.employees.size().max())
            )).list(department))
        );
    }

/*  "SELECT FROM " + Employee.class.getName() + " WHERE this.weeklyhours > " +
 *  "(SELECT AVG(e.weeklyhours) FROM this.department.employees e)"; */
    @Test
    public void test2() {
        assertEquals(
          "SELECT FROM com.querydsl.jdo.models.company.Employee " +
          "WHERE this.weeklyhours > " +
          "(SELECT avg(e.weeklyhours) FROM this.department.employees e)",

          serialize(query().from(employee).where(employee.weeklyhours.gt(
               query().from(employee.department.employees, e).unique(e.weeklyhours.avg())
            )).list(employee))
        );
    }

/*  "SELECT FROM " + Employee.class.getName() +
 *   " WHERE this.weeklyhours > " +
 *   "(SELECT AVG(e.weeklyhours) FROM this.department.employees e " +
 *   " WHERE e.manager == this.manager)"; */
    @Test
    public void test3() {
        assertEquals(
          "SELECT FROM com.querydsl.jdo.models.company.Employee " +
          "WHERE this.weeklyhours > " +
          "(SELECT avg(e.weeklyhours) FROM this.department.employees e WHERE e.manager == this.manager)",

          serialize(query().from(employee).where(employee.weeklyhours.gt(
               query().from(employee.department.employees, e).where(e.manager.eq(employee.manager)).unique(e.weeklyhours.avg())
            )).list(employee))
        );
    }
/*  "SELECT FROM " + Employee.class.getName() + " WHERE this.weeklyhours > " +
 *   "(SELECT AVG(e.weeklyhours) FROM " + Employee.class.getName() + " e)"; */
    @Test
    public void test4() {
        assertEquals(
          "SELECT FROM com.querydsl.jdo.models.company.Employee " +
          "WHERE this.weeklyhours > " +
          "(SELECT avg(e.weeklyhours) FROM com.querydsl.jdo.models.company.Employee e)",

          serialize(query().from(employee).where(employee.weeklyhours.gt(
               query().from(e).unique(e.weeklyhours.avg())
            )).list(employee))
        );
    }

/*  "SELECT FROM " + Employee.class.getName() +
 *   " WHERE this.weeklyhours == emp.weeklyhours && " +
 *   "emp.firstname == 'emp1First' VARIABLES Employee emp"; */
    @Test
    public void test5() {
        assertEquals(
          "SELECT FROM com.querydsl.jdo.models.company.Employee " +
          "WHERE this.weeklyhours == e.weeklyhours && this.firstName == a1 " +
          "VARIABLES com.querydsl.jdo.models.company.Employee e " +
          "PARAMETERS java.lang.String a1",

          serialize(query().from(employee, e)
              .where(
                  employee.weeklyhours.eq(e.weeklyhours),
                  employee.firstName.eq("emp1First")
              ).list(employee))
        );
    }

}
