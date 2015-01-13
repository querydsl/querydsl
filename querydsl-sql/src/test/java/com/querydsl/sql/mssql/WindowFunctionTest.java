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
package com.querydsl.sql.mssql;

import static com.querydsl.sql.Constants.employee;
import static com.querydsl.sql.SQLExpressions.rowNumber;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLSerializer;
import com.querydsl.sql.WindowFunction;
import com.querydsl.core.types.Expression;

public class WindowFunctionTest {

    private static String toString(Expression<?> e) {
        return new SQLSerializer(Configuration.DEFAULT).handle(e).toString();
    }

//    ROW_NUMBER() OVER (ORDER BY OrderDate) AS 'RowNumber'

//    ROW_NUMBER() OVER (PARTITION BY PostalCode ORDER BY SalesYTD DESC)

    @Test
    public void Mutable() {
        WindowFunction<Long> rn = rowNumber().over().orderBy(employee.firstname);
        assertEquals("row_number() over (order by e.FIRSTNAME)", toString(rn));
        assertEquals("row_number() over (order by e.FIRSTNAME, e.LASTNAME)", toString(rn.orderBy(employee.lastname)));
    }

    @Test
    public void OrderBy() {
        assertEquals("row_number() over (order by e.FIRSTNAME)",
            toString(rowNumber().over().orderBy(employee.firstname.asc())));

        assertEquals("row_number() over (order by e.FIRSTNAME)",
            toString(rowNumber().over().orderBy(employee.firstname)));

        assertEquals("row_number() over (order by e.FIRSTNAME) as rn",
            toString(rowNumber().over().orderBy(employee.firstname.asc()).as("rn")));

        assertEquals("row_number() over (order by e.FIRSTNAME desc)",
            toString(rowNumber().over().orderBy(employee.firstname.desc())));
    }

    @Test
    public void PartitionBy() {
        assertEquals("row_number() over (partition by e.LASTNAME order by e.FIRSTNAME)",
            toString(rowNumber().over().partitionBy(employee.lastname).orderBy(employee.firstname.asc())));

        assertEquals("row_number() over (partition by e.LASTNAME, e.FIRSTNAME order by e.FIRSTNAME)",
            toString(rowNumber().over().partitionBy(employee.lastname, employee.firstname).orderBy(employee.firstname.asc())));
    }


}
