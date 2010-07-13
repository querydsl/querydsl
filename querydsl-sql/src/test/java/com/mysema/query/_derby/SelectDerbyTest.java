/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
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
        templates = new DerbyTemplates().newLineToSingleSpace();
    }

    @Test
    public void limitAndOffsetInDerby() throws SQLException {
        expectedQuery = "select e.ID from EMPLOYEE2 e offset 3 rows fetch next 4 rows only";
        query().from(employee).limit(4).offset(3).list(employee.id);

        // limit
        expectedQuery = "select e.ID from EMPLOYEE2 e fetch first 4 rows only";
        query().from(employee).limit(4).list(employee.id);

        // offset
        expectedQuery = "select e.ID from EMPLOYEE2 e offset 3 rows";
        query().from(employee).offset(3).list(employee.id);

    }

}
