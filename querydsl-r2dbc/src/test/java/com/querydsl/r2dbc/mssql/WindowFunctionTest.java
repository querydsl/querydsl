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
package com.querydsl.r2dbc.mssql;

import com.querydsl.core.types.Expression;
import com.querydsl.r2dbc.Configuration;
import com.querydsl.r2dbc.R2DBCExpressions;
import com.querydsl.r2dbc.SQLSerializer;
import com.querydsl.r2dbc.SQLTemplates;
import com.querydsl.sql.WindowFunction;
import org.junit.Test;

import static com.querydsl.r2dbc.Constants.employee;
import static org.junit.Assert.assertEquals;

public class WindowFunctionTest {

    private static final Configuration configuration = new Configuration(SQLTemplates.DEFAULT);

    private static String toString(Expression<?> e) {
        return new SQLSerializer(configuration).handle(e).toString();
    }

//    ROW_NUMBER() OVER (ORDER BY OrderDate) AS 'RowNumber'

//    ROW_NUMBER() OVER (PARTITION BY PostalCode ORDER BY SalesYTD DESC)

    @Test
    public void mutable() {
        WindowFunction<Long> rn = R2DBCExpressions.rowNumber().over().orderBy(employee.firstname);
        assertEquals("row_number() over (order by e.FIRSTNAME asc)", toString(rn));
        assertEquals("row_number() over (order by e.FIRSTNAME asc, e.LASTNAME asc)", toString(rn.orderBy(employee.lastname)));
    }

    @Test
    public void orderBy() {
        assertEquals("row_number() over (order by e.FIRSTNAME asc)",
                toString(R2DBCExpressions.rowNumber().over().orderBy(employee.firstname.asc())));

        assertEquals("row_number() over (order by e.FIRSTNAME asc)",
                toString(R2DBCExpressions.rowNumber().over().orderBy(employee.firstname)));

        assertEquals("row_number() over (order by e.FIRSTNAME asc) as rn",
                toString(R2DBCExpressions.rowNumber().over().orderBy(employee.firstname.asc()).as("rn")));

        assertEquals("row_number() over (order by e.FIRSTNAME desc)",
                toString(R2DBCExpressions.rowNumber().over().orderBy(employee.firstname.desc())));
    }

    @Test
    public void partitionBy() {
        assertEquals("row_number() over (partition by e.LASTNAME order by e.FIRSTNAME asc)",
                toString(R2DBCExpressions.rowNumber().over().partitionBy(employee.lastname).orderBy(employee.firstname.asc())));

        assertEquals("row_number() over (partition by e.LASTNAME, e.FIRSTNAME order by e.FIRSTNAME asc)",
                toString(R2DBCExpressions.rowNumber().over().partitionBy(employee.lastname, employee.firstname).orderBy(employee.firstname.asc())));
    }


}
