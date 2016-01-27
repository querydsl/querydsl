package com.querydsl.sql;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.testutil.ExcludeIn;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.Param;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.sql.domain.Employee;
import com.querydsl.sql.domain.QEmployee;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static com.querydsl.core.Target.CUBRID;
import static com.querydsl.core.Target.DB2;
import static com.querydsl.core.Target.DERBY;
import static com.querydsl.core.Target.FIREBIRD;
import static com.querydsl.core.Target.H2;
import static com.querydsl.core.Target.HSQLDB;
import static com.querydsl.core.Target.MYSQL;
import static com.querydsl.core.Target.POSTGRESQL;
import static com.querydsl.core.Target.SQLITE;
import static com.querydsl.core.Target.SQLSERVER;
import static com.querydsl.core.Target.TERADATA;
import static com.querydsl.sql.Constants.employee;
import static com.querydsl.sql.Constants.employee2;
import static com.querydsl.sql.Constants.survey;
import static com.querydsl.sql.Constants.survey2;
import static com.querydsl.sql.SQLExpressions.select;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class SubqueriesBase extends AbstractBaseTest {

    @Test
    @ExcludeIn({CUBRID, DERBY, FIREBIRD, H2, HSQLDB, SQLITE, SQLSERVER})
    public void keys() {
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
    public void list_in_query() {
        QEmployee employee2 = new QEmployee("employee2");
        query().from(employee)
            .where(Expressions.list(employee.id, employee.lastname)
                .in(query().from(employee2).select(employee2.id, employee2.lastname)))
            .select(employee.id).fetch();
    }

    @Test
    @SkipForQuoted
    @ExcludeIn(DB2) // ID is reserved IN DB2
    public void subQueries() throws SQLException {
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
    public void subQuery_alias() {
        query().from(query().from(employee).select(employee.all()).as(employee2)).select(employee2.all()).fetch();
    }

    @Test
    @ExcludeIn(SQLITE)
    public void subQuery_all() {
        query().from(employee).where(employee.id.gtAll(
                query().from(employee2).select(employee2.id))).fetchCount();
    }

    @Test
    @ExcludeIn(SQLITE)
    public void subQuery_any() {
        query().from(employee).where(employee.id.gtAny(
                query().from(employee2).select(employee2.id))).fetchCount();
    }

    @Test
    public void subQuery_innerJoin() {
        SubQueryExpression<Integer> sq = query().from(employee2).select(employee2.id);
        QEmployee sqEmp = new QEmployee("sq");
        query().from(employee).innerJoin(sq, sqEmp).on(sqEmp.id.eq(employee.id)).select(employee.id).fetch();

    }

    @Test
    public void subQuery_leftJoin() {
        SubQueryExpression<Integer> sq = query().from(employee2).select(employee2.id);
        QEmployee sqEmp = new QEmployee("sq");
        query().from(employee).leftJoin(sq, sqEmp).on(sqEmp.id.eq(employee.id)).select(employee.id).fetch();

    }

    @Test
    @ExcludeIn({MYSQL, POSTGRESQL, DERBY, SQLSERVER, TERADATA})
    public void subQuery_params() {
        Param<String> aParam = new Param<String>(String.class, "param");
        SQLQuery<?> subQuery = select(Wildcard.all).from(employee).where(employee.firstname.eq(aParam));
        subQuery.set(aParam, "Mike");

        assertEquals(1, query().from(subQuery).fetchCount());
    }

    @Test
    @ExcludeIn(SQLITE)
    public void subQuery_rightJoin() {
        SubQueryExpression<Integer> sq = query().from(employee2).select(employee2.id);
        QEmployee sqEmp = new QEmployee("sq");
        query().from(employee).rightJoin(sq, sqEmp).on(sqEmp.id.eq(employee.id)).select(employee.id).fetch();
    }

    @Test
    public void subQuery_with_alias() {
        List<Integer> ids1 = query().from(employee).select(employee.id).fetch();
        List<Integer> ids2 = query().from(query().from(employee).select(employee.id), employee).select(employee.id).fetch();
        assertEquals(ids1, ids2);
    }

    @Test
    public void subQuery_with_alias2() {
        List<Integer> ids1 = query().from(employee).select(employee.id).fetch();
        List<Integer> ids2 = query().from(query().from(employee).select(employee.id).as(employee)).select(employee.id).fetch();
        assertEquals(ids1, ids2);
    }

    @Test
    @SkipForQuoted
    public void subQuerySerialization() {
        SQLQuery<?> query = query();
        query.from(survey);
        assertEquals("from SURVEY s", query.toString());

        query.from(survey2);
        assertEquals("from SURVEY s, SURVEY s2", query.toString());
    }

    @Test
    public void subQuerySerialization2() {
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

    @Test
    public void scalarSubQueryInClause() {
        SQLSerializer serializer = new SQLSerializer(Configuration.DEFAULT);

        serializer.handle(
            this.query()
                .from(employee)
                .where(
                    SQLExpressions
                        .select(employee.firstname)
                        .from(employee)
                        .orderBy(employee.salary.asc())
                        .limit(1)
                        .in(Arrays.asList("Mike", "Mary"))
                ));

        expectedQuery = "(\nfrom EMPLOYEE e\n"
            + "where (select e.FIRSTNAME\n"
            + "from EMPLOYEE e\n"
            + "order by e.SALARY asc\n"
            + "limit ?) in (?, ?))";

        System.out.println(">>>> " + serializer.toString());
        assertEquals(expectedQuery, serializer.toString());
    }

    @Test
    public void scalarSubQueryInClause2() {
        SQLSerializer serializer = new SQLSerializer(Configuration.DEFAULT);

        serializer.handle(
            this.query()
                .from(employee)
                .where(
                    SQLExpressions
                        .select(employee.firstname)
                        .from(employee)
                        .orderBy(employee.salary.asc())
                        .limit(1)
                        .in("Mike", "Mary")
                ));

        expectedQuery = "(\nfrom EMPLOYEE e\n"
            + "where (select e.FIRSTNAME\n"
            + "from EMPLOYEE e\n"
            + "order by e.SALARY asc\n"
            + "limit ?) in (?, ?))";

        assertEquals(expectedQuery, serializer.toString());
    }
}
