/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.mysema.query.sql.domain.Employee;
import com.mysema.query.sql.domain.QEmployee;
import com.mysema.query.types.Path;

public class ForeignKeyTest {

    @Test
    public void On() {
        QEmployee employee = new QEmployee("employee");
        QEmployee employee2 = new QEmployee("employee2");

        ForeignKey<Employee> foreignKey = new ForeignKey<Employee>(employee, employee.superiorId, "ID");
        assertEquals("employee.SUPERIOR_ID = employee2.ID", foreignKey.on(employee2).toString());

        foreignKey = new ForeignKey<Employee>(employee, Arrays.<Path<?>>asList(employee.superiorId, employee.firstname), Arrays.asList("ID", "FN"));
        assertEquals("employee.SUPERIOR_ID = employee2.ID && employee.FIRSTNAME = employee2.FN", foreignKey.on(employee2).toString());
    }

}
