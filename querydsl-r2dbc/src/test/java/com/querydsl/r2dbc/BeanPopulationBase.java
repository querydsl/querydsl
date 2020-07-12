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
package com.querydsl.r2dbc;

import com.querydsl.core.testutil.ExcludeIn;
import com.querydsl.r2dbc.dml.BeanMapper;
import com.querydsl.r2dbc.domain.Employee;
import com.querydsl.r2dbc.domain.QEmployee;
import org.junit.After;
import org.junit.Test;

import static com.querydsl.core.Target.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ExcludeIn({CUBRID, DB2, DERBY, ORACLE, SQLSERVER, POSTGRESQL, SQLITE, TERADATA})
public abstract class BeanPopulationBase extends AbstractBaseTest {

    private final QEmployee e = new QEmployee("e");

    @After
    public void tearDown() {
        delete(e).where(e.firstname.eq("John")).execute().block();
    }

    @Test
    public void custom_projection() {
        // Insert
        Employee employee = new Employee();
        employee.setFirstname("John");
        Integer id = insert(e).populate(employee).executeWithKey(e.id).block();
        employee.setId(id);

        // Update
        employee.setLastname("S");
        assertEquals(1L, (long) update(e).populate(employee).where(e.id.eq(employee.getId())).execute().block());

        // Query
        Employee smith = extQuery().from(e).where(e.lastname.eq("S"))
                .limit(1)
                .uniqueResult(Employee.class, e.lastname, e.firstname).block();
        assertEquals("John", smith.getFirstname());
        assertEquals("S", smith.getLastname());

        // Query with alias
        smith = extQuery().from(e).where(e.lastname.eq("S"))
                .limit(1)
                .uniqueResult(Employee.class, e.lastname.as("lastname"), e.firstname.as("firstname")).block();
        assertEquals("John", smith.getFirstname());
        assertEquals("S", smith.getLastname());

        // Query into custom type
        OtherEmployee other = extQuery().from(e).where(e.lastname.eq("S"))
                .limit(1)
                .uniqueResult(OtherEmployee.class, e.lastname, e.firstname).block();
        assertEquals("John", other.getFirstname());
        assertEquals("S", other.getLastname());

        // Delete (no changes needed)
        assertEquals(1L, (long) delete(e).where(e.id.eq(employee.getId())).execute().block());
    }

    @Test
    public void insert_update_query_and_delete() {
        // Insert
        Employee employee = new Employee();
        employee.setFirstname("John");
        Integer id = insert(e).populate(employee).executeWithKey(e.id).block();
        assertNotNull(id);
        employee.setId(id);

        // Update
        employee.setLastname("S");
        assertEquals(1L, (long) update(e).populate(employee).where(e.id.eq(employee.getId())).execute().block());

        // Query
        Employee smith = query().from(e).where(e.lastname.eq("S")).limit(1).select(e).fetchFirst().block();
        assertEquals("John", smith.getFirstname());

        // Delete (no changes needed)
        assertEquals(1L, (long) delete(e).where(e.id.eq(employee.getId())).execute().block());
    }

    @Test
    public void populate_with_beanMapper() {
        Employee employee = new Employee();
        employee.setFirstname("John");
        insert(e).populate(employee, new BeanMapper()).execute();
    }

}
