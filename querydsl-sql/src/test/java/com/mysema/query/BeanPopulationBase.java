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
package com.mysema.query;

import static com.mysema.query.Target.CUBRID;
import static com.mysema.query.Target.DERBY;
import static com.mysema.query.Target.NUODB;
import static com.mysema.query.Target.ORACLE;
import static com.mysema.query.Target.POSTGRES;
import static com.mysema.query.Target.SQLITE;
import static com.mysema.query.Target.SQLSERVER;
import static com.mysema.query.Target.TERADATA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Test;

import com.mysema.query.sql.dml.BeanMapper;
import com.mysema.query.sql.domain.Employee;
import com.mysema.query.sql.domain.QEmployee;
import com.mysema.testutil.ExcludeIn;

@ExcludeIn({CUBRID, DERBY, ORACLE, SQLSERVER, POSTGRES, SQLITE, TERADATA, NUODB})
public class BeanPopulationBase extends AbstractBaseTest {

    private final QEmployee e = new QEmployee("e");

    @After
    public void tearDown() {
        delete(e).where(e.firstname.eq("John")).execute();
    }

    @Test
    public void CustomProjection() {
        // Insert
        Employee employee = new Employee();
        employee.setFirstname("John");
        Integer id = insert(e).populate(employee).executeWithKey(e.id);
        employee.setId(id);

        // Update
        employee.setLastname("S");
        assertEquals(1l, update(e).populate(employee).where(e.id.eq(employee.getId())).execute());

        // Query
        Employee smith = extQuery().from(e).where(e.lastname.eq("S"))
            .limit(1)
            .uniqueResult(Employee.class, e.lastname, e.firstname);
        assertEquals("John", smith.getFirstname());
        assertEquals("S", smith.getLastname());

        // Query with alias
        smith = extQuery().from(e).where(e.lastname.eq("S"))
            .limit(1)
            .uniqueResult(Employee.class, e.lastname.as("lastname"), e.firstname.as("firstname"));
        assertEquals("John", smith.getFirstname());
        assertEquals("S", smith.getLastname());

        // Query into custom type
        OtherEmployee other = extQuery().from(e).where(e.lastname.eq("S"))
            .limit(1)
            .uniqueResult(OtherEmployee.class, e.lastname, e.firstname);
        assertEquals("John", other.getFirstname());
        assertEquals("S", other.getLastname());

        // Delete (no changes needed)
        assertEquals(1l, delete(e).where(e.id.eq(employee.getId())).execute());
    }

    @Test
    public void Insert_Update_Query_and_Delete() {
        // Insert
        Employee employee = new Employee();
        employee.setFirstname("John");
        Integer id = insert(e).populate(employee).executeWithKey(e.id);
        assertNotNull(id);
        employee.setId(id);

        // Update
        employee.setLastname("S");
        assertEquals(1l, update(e).populate(employee).where(e.id.eq(employee.getId())).execute());

        // Query
        Employee smith = query().from(e).where(e.lastname.eq("S")).limit(1).uniqueResult(e);
        assertEquals("John", smith.getFirstname());

        // Delete (no changes needed)
        assertEquals(1l, delete(e).where(e.id.eq(employee.getId())).execute());
    }

    @Test
    public void Populate_With_BeanMapper() {
        Employee employee = new Employee();
        employee.setFirstname("John");
        insert(e).populate(employee, new BeanMapper()).execute();
    }

}
