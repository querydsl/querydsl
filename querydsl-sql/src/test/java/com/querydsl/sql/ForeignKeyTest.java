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
package com.querydsl.sql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.querydsl.sql.domain.Employee;
import com.querydsl.sql.domain.QEmployee;

import java.util.Arrays;

public class ForeignKeyTest {

    @Test
    public void on() {
        QEmployee employee = new QEmployee("employee");
        QEmployee employee2 = new QEmployee("employee2");

        ForeignKey<Employee> foreignKey = new ForeignKey<Employee>(employee, employee.superiorId, "ID");
        assertEquals("employee.superiorId = employee2.ID", foreignKey.on(employee2).toString());

        foreignKey = new ForeignKey<Employee>(employee,
                Arrays.asList(employee.superiorId, employee.firstname),
                Arrays.asList("ID", "FN"));
        assertEquals("employee.superiorId = employee2.ID && employee.firstname = employee2.FN", foreignKey.on(employee2).toString());
    }

}
