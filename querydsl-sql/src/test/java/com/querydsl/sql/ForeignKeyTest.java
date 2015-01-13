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
package com.querydsl.sql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.querydsl.sql.domain.Employee;
import com.querydsl.sql.domain.QEmployee;

public class ForeignKeyTest {

    @Test
    public void On() {
        QEmployee employee = new QEmployee("employee");
        QEmployee employee2 = new QEmployee("employee2");

        ForeignKey<Employee> foreignKey = new ForeignKey<Employee>(employee, employee.superiorId, "ID");
        assertEquals("employee.superiorId = employee2.ID", foreignKey.on(employee2).toString());

        foreignKey = new ForeignKey<Employee>(employee,
               ImmutableList.of(employee.superiorId, employee.firstname),
               ImmutableList.of("ID", "FN"));
        assertEquals("employee.superiorId = employee2.ID && employee.firstname = employee2.FN", foreignKey.on(employee2).toString());
    }

}
