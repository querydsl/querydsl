package com.querydsl.r2dbc;

import com.google.common.collect.ImmutableSet;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.r2dbc.domain.QEmployee;
import com.querydsl.sql.RelationalPathExtractor;
import org.junit.Test;

import static com.querydsl.r2dbc.Constants.employee;
import static org.junit.Assert.assertEquals;

public class RelationalPathExtractorTest {

    private R2DBCQuery<?> query() {
        return new R2DBCQuery<Void>();
    }

    @Test
    public void simpleQuery() {
        QEmployee employee2 = new QEmployee("employee2");
        R2DBCQuery<?> query = query().from(employee, employee2);

        assertEquals(ImmutableSet.of(employee, employee2), RelationalPathExtractor.extract(query.getMetadata()));
    }

    @Test
    public void joins() {
        QEmployee employee2 = new QEmployee("employee2");
        R2DBCQuery<?> query = query().from(employee)
                .innerJoin(employee2).on(employee.superiorId.eq(employee2.id));

        assertEquals(ImmutableSet.of(employee, employee2), RelationalPathExtractor.extract(query.getMetadata()));
    }

    @Test
    public void subQuery() {
        R2DBCQuery<?> query = query().from(employee)
                .where(employee.id.eq(query().from(employee).select(employee.id.max())));
        assertEquals(ImmutableSet.of(employee), RelationalPathExtractor.extract(query.getMetadata()));
    }

    @Test
    public void subQuery2() {
        QEmployee employee2 = new QEmployee("employee2");
        R2DBCQuery<?> query = query().from(employee)
                .where(Expressions.list(employee.id, employee.lastname)
                        .in(query().from(employee2).select(employee2.id, employee2.lastname)));

        assertEquals(ImmutableSet.of(employee, employee2), RelationalPathExtractor.extract(query.getMetadata()));
    }

}
