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
package com.mysema.query.sql.mssql;

import static com.mysema.query.Constants.employee;
import static com.mysema.query.sql.mssql.SQLServerGrammar.rn;
import static com.mysema.query.sql.mssql.SQLServerGrammar.rowNumber;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RowNumberTest {

//    ROW_NUMBER() OVER (ORDER BY OrderDate) AS 'RowNumber'

//    ROW_NUMBER() OVER( PARTITION BY PostalCode ORDER BY SalesYTD DESC)

    @Test
    public void Mutable() {
        RowNumber rn = rowNumber().orderBy(employee.firstname);
        assertEquals("row_number() over (order by e.firstname)", rn.toString());
        assertEquals("row_number() over (order by e.firstname, e.lastname)", rn.orderBy(employee.lastname).toString());
    }

    @Test
    public void OrderBy() {
        assertEquals("row_number() over (order by e.firstname)",
            rowNumber().orderBy(employee.firstname.asc()).toString());

        assertEquals("row_number() over (order by e.firstname)",
            rowNumber().orderBy(employee.firstname).toString());

        assertEquals("row_number() over (order by e.firstname) as rn",
            rowNumber().orderBy(employee.firstname.asc()).as(rn).toString());

        assertEquals("row_number() over (order by e.firstname desc)",
            rowNumber().orderBy(employee.firstname.desc()).toString());
    }

    @Test
    public void PartitionBy() {
        assertEquals("row_number() over (partition by e.lastname order by e.firstname)",
            rowNumber().partitionBy(employee.lastname).orderBy(employee.firstname.asc()).toString());

        assertEquals("row_number() over (partition by e.lastname, e.firstname order by e.firstname)",
            rowNumber().partitionBy(employee.lastname, employee.firstname).orderBy(employee.firstname.asc()).toString());
    }

}
