package com.querydsl.sql;

import static com.querydsl.core.Target.*;
import static com.querydsl.sql.Constants.*;
import static com.querydsl.sql.SQLExpressions.select;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.testutil.ExcludeIn;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.*;
import com.querydsl.sql.domain.Employee;
import com.querydsl.sql.domain.QEmployee;

public class SubqueriesBase extends AbstractBaseTest {

    @Test
    @ExcludeIn({CUBRID, DERBY, FIREBIRD, H2, HSQLDB, SQLITE, SQLSERVER})
    public void Keys() {
        QEmployee employee2 = new QEmployee("employee2");
        ForeignKey<Employee> nameKey1 = new ForeignKey<Employee>(employee,
                ImmutableList.of(employee.firstname, employee.lastname),
                ImmutableList.of("a", "b"));
        ForeignKey<Employee> nameKey2 = new ForeignKey<Employee>(employee,
                ImmutableList.of(employee.firstname, employee.lastname),
                ImmutableList.of("a", "b"));

        query().from(employee)
        .where(nameKey1.in(query().from(employee2).select(nameKey2.getProjection())))
            .select(employee.id).fetch();
    }

    @Test
    @ExcludeIn({CUBRID, DERBY, FIREBIRD, H2, HSQLDB, SQLITE, SQLSERVER})
    public void List_In_Query() {
        QEmployee employee2 = new QEmployee("employee2");
        query().from(employee)
            .where(Expressions.list(employee.id, employee.lastname)
                .in(query().from(employee2).select(employee2.id, employee2.lastname)))
            .select(employee.id).fetch();
    }

    @Test
    @SkipForQuoted
    @ExcludeIn(DB2) // ID is reserved IN DB2
    public void SubQueries() throws SQLException {
        // subquery in where block
        expectedQuery = "select e.ID from EMPLOYEE e "
            + "where e.ID = (select max(e.ID) "
            + "from EMPLOYEE e)";
        List<Integer> list = query().from(employee)
        .where(employee.id.eq(query().from(employee).select(employee.id.max())))
        .select(employee.id).fetch();
        assertFalse(list.isEmpty());
    }

    @Test
    public void SubQuery_Alias() {
        query().from(query().from(employee).select(employee.all()).as(employee2)).select(employee2.all()).fetch();
    }

    @Test
    @ExcludeIn(SQLITE)
    public void SubQuery_All() {
        query().from(employee).where(employee.id.gtAll(
                query().from(employee2).select(employee2.id))).fetchCount();
    }

    @Test
    @ExcludeIn(SQLITE)
    public void SubQuery_Any() {
        query().from(employee).where(employee.id.gtAny(
                query().from(employee2).select(employee2.id))).fetchCount();
    }

    @Test
    public void SubQuery_InnerJoin() {
        SubQueryExpression<Integer> sq = query().from(employee2).select(employee2.id);
        QEmployee sqEmp = new QEmployee("sq");
        query().from(employee).innerJoin(sq, sqEmp).on(sqEmp.id.eq(employee.id)).select(employee.id).fetch();

    }

    @Test
    public void SubQuery_LeftJoin() {
        SubQueryExpression<Integer> sq = query().from(employee2).select(employee2.id);
        QEmployee sqEmp = new QEmployee("sq");
        query().from(employee).leftJoin(sq, sqEmp).on(sqEmp.id.eq(employee.id)).select(employee.id).fetch();

    }

    @Test
    @ExcludeIn({MYSQL, POSTGRESQL, DERBY, SQLSERVER, TERADATA})
    public void SubQuery_Params() {
        Param<String> aParam = new Param<String>(String.class, "param");
        SQLQuery<?> subQuery = select(Wildcard.all).from(employee).where(employee.firstname.eq(aParam));
        subQuery.set(aParam, "Mike");

        assertEquals(1, query().from(subQuery).fetchCount());
    }

    @Test
    @ExcludeIn(SQLITE)
    public void SubQuery_RightJoin() {
        SubQueryExpression<Integer> sq = query().from(employee2).select(employee2.id);
        QEmployee sqEmp = new QEmployee("sq");
        query().from(employee).rightJoin(sq, sqEmp).on(sqEmp.id.eq(employee.id)).select(employee.id).fetch();
    }

    @Test
    public void SubQuery_with_Alias() {
        List<Integer> ids1 = query().from(employee).select(employee.id).fetch();
        List<Integer> ids2 = query().from(query().from(employee).select(employee.id), employee).select(employee.id).fetch();
        assertEquals(ids1, ids2);
    }

    @Test
    public void SubQuery_with_Alias2() {
        List<Integer> ids1 = query().from(employee).select(employee.id).fetch();
        List<Integer> ids2 = query().from(query().from(employee).select(employee.id).as(employee)).select(employee.id).fetch();
        assertEquals(ids1, ids2);
    }

    @Test
    @SkipForQuoted
    public void SubQuerySerialization() {
        SQLQuery<?> query = query();
        query.from(survey);
        assertEquals("from SURVEY s", query.toString());

        query.from(survey2);
        assertEquals("from SURVEY s, SURVEY s2", query.toString());
    }

    @Test
    public void SubQuerySerialization2() {
        NumberPath<BigDecimal> sal = Expressions.numberPath(BigDecimal.class, "sal");
        PathBuilder<Object[]> sq = new PathBuilder<Object[]>(Object[].class, "sq");
        SQLSerializer serializer = new SQLSerializer(Configuration.DEFAULT);

        serializer.handle(
                query()
                .from(employee)
                .select(employee.salary.add(employee.salary).add(employee.salary).as(sal))
                .as(sq));
        assertEquals(
                "(select (e.SALARY + e.SALARY + e.SALARY) as sal\nfrom EMPLOYEE e) as sq",
                serializer.toString());
    }


}
