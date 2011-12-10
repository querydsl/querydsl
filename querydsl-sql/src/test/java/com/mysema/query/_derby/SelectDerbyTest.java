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
package com.mysema.query._derby;

import static com.mysema.query.Constants.employee;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mysema.query.Connections;
import com.mysema.query.SelectBaseTest;
import com.mysema.query.Target;
import com.mysema.query.sql.DerbyTemplates;
import com.mysema.testutil.Label;

@Label(Target.DERBY)
public class SelectDerbyTest extends SelectBaseTest {

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initDerby();
    }

    @Before
    public void setUpForTest() {
        templates = new DerbyTemplates(){{
            newLineToSingleSpace();
        }};
    }

    @Test
    public void limitAndOffsetInDerby() throws SQLException {
        expectedQuery = "select e.ID from EMPLOYEE e offset 3 rows fetch next 4 rows only";
        query().from(employee).limit(4).offset(3).list(employee.id);

        // limit
        expectedQuery = "select e.ID from EMPLOYEE e fetch first 4 rows only";
        query().from(employee).limit(4).list(employee.id);

        // offset
        expectedQuery = "select e.ID from EMPLOYEE e offset 3 rows";
        query().from(employee).offset(3).list(employee.id);

    }

}
