package com.mysema.query.jdo.serialization;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.jdo.models.company.QEmployee;

public class GroupByTest extends AbstractTest {
    
    @Test
    public void GroupBy() {
        QEmployee employee = QEmployee.employee;
        assertEquals(
                "SELECT FROM com.mysema.query.jdo.models.company.Employee "+
                "PARAMETERS java.lang.String a1 "+
                "GROUP BY this.emailAddress "+
                "HAVING this.emailAddress != a1",

                serialize(query()
                    .from(employee)
                    .groupBy(employee.emailAddress).having(employee.emailAddress.ne("XXX"))
                    .list(employee)));
    }

}
