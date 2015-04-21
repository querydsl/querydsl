package com.querydsl.sql;

import static com.querydsl.sql.Constants.employee;
import static com.querydsl.sql.RelationalPathExtractor.extract;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.querydsl.sql.domain.QEmployee;
import com.querydsl.core.types.dsl.Expressions;

public class RelationalPathExtractorTest {

    private SQLQuery<?> query() {
        return new SQLQuery<Void>(SQLTemplates.DEFAULT);
    }

    @Test
    public void SimpleQuery() {
        QEmployee employee2 = new QEmployee("employee2");
        SQLQuery<?> query = query().from(employee, employee2);

        assertEquals(ImmutableSet.of(employee, employee2), extract(query.getMetadata()));
    }

    @Test
    public void Joins() {
        QEmployee employee2 = new QEmployee("employee2");
        SQLQuery<?> query = query().from(employee)
                .innerJoin(employee2).on(employee.superiorId.eq(employee2.id));

        assertEquals(ImmutableSet.of(employee, employee2), extract(query.getMetadata()));
    }

    @Test
    public void SubQuery() {
        SQLQuery<?> query = query().from(employee)
                .where(employee.id.eq(query().from(employee).select(employee.id.max())));
        assertEquals(ImmutableSet.of(employee), extract(query.getMetadata()));
    }

    @Test
    public void SubQuery2() {
        QEmployee employee2 = new QEmployee("employee2");
        SQLQuery<?> query = query().from(employee)
            .where(Expressions.list(employee.id, employee.lastname)
                .in(query().from(employee2).select(employee2.id, employee2.lastname)));

        assertEquals(ImmutableSet.of(employee, employee2), extract(query.getMetadata()));
    }

}
