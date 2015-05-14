package com.querydsl.jdo.serialization;

import static com.querydsl.jdo.JDOExpressions.selectFrom;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.querydsl.jdo.models.company.QEmployee;

public class GroupByTest extends AbstractTest {

    @Test
    public void GroupBy() {
        QEmployee employee = QEmployee.employee;
        assertEquals(
                "SELECT FROM com.querydsl.jdo.models.company.Employee " +
                "PARAMETERS java.lang.String a1 " +
                "GROUP BY this.emailAddress " +
                "HAVING this.emailAddress != a1",

                serialize(selectFrom(employee)
                    .groupBy(employee.emailAddress).having(employee.emailAddress.ne("XXX"))));
    }

}
