package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.sql.domain.QEmployee;

public class SQLSubQueryTest {
    
    @Test
    public void Multiple_Projections(){
        SQLSubQuery query = new SQLSubQuery();
        query.from(QEmployee.employee);
        assertEquals(1, query.list(QEmployee.employee).getMetadata().getProjection().size());
        assertEquals(1, query.list(QEmployee.employee).getMetadata().getProjection().size());
    }

}
