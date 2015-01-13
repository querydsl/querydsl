package com.querydsl.sql;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.Tuple;
import com.querydsl.sql.domain.Employee;
import com.querydsl.sql.domain.QEmployee;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.query.ListSubQuery;
import com.querydsl.core.types.query.SimpleSubQuery;
import com.querydsl.core.types.template.NumberTemplate;
import com.querydsl.core.testutil.ExcludeIn;
import org.junit.Test;
import static com.querydsl.sql.Constants.employee;
import static com.querydsl.core.Target.*;
import static org.junit.Assert.*;

public class UnionBase extends AbstractBaseTest {

    @Test
    @ExcludeIn({MYSQL, TERADATA})
    public void In_Union() {
        assertTrue(query().from(employee)
            .where(employee.id.in(
                sq().union(sq().unique(NumberTemplate.ONE),
                           sq().unique(NumberTemplate.TWO))))
            .exists());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Union() throws SQLException {
        SubQueryExpression<Integer> sq1 = sq().from(employee).unique(employee.id.max());
        SubQueryExpression<Integer> sq2 = sq().from(employee).unique(employee.id.min());
        List<Integer> list = query().union(sq1, sq2).list();
        assertFalse(list.isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Union_All() {
        SubQueryExpression<Integer> sq1 = sq().from(employee).unique(employee.id.max());
        SubQueryExpression<Integer> sq2 = sq().from(employee).unique(employee.id.min());
        List<Integer> list = query().unionAll(sq1, sq2).list();
        assertFalse(list.isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void Union_Multiple_Columns() throws SQLException {
        SubQueryExpression<Tuple> sq1 = sq().from(employee).unique(employee.firstname, employee.lastname);
        SubQueryExpression<Tuple> sq2 = sq().from(employee).unique(employee.lastname, employee.firstname);
        List<Tuple> list = query().union(sq1, sq2).list();
        assertFalse(list.isEmpty());
        for (Tuple row : list) {
            assertNotNull(row.get(0, Object.class));
            assertNotNull(row.get(1, Object.class));
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    @ExcludeIn(DERBY)
    public void Union_Multiple_Columns2() throws SQLException {
        SubQueryExpression<Tuple> sq1 = sq().from(employee).unique(employee.firstname, employee.lastname);
        SubQueryExpression<Tuple> sq2 = sq().from(employee).unique(employee.firstname, employee.lastname);
        TestQuery query = query();
        query.union(sq1, sq2);
        List<String> list = query.list(employee.firstname);
        assertFalse(list.isEmpty());
        for (String row : list) {
            assertNotNull(row);
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    @ExcludeIn(DERBY)
    public void Union_Multiple_Columns3() throws SQLException {
        SubQueryExpression<Tuple> sq1 = sq().from(employee).unique(employee.firstname, employee.lastname);
        SubQueryExpression<Tuple> sq2 = sq().from(employee).unique(employee.firstname, employee.lastname);
        TestQuery query = query();
        query.union(sq1, sq2);
        List<Tuple> list = query.list(employee.lastname, employee.firstname);
        assertFalse(list.isEmpty());
        for (Tuple row : list) {
            System.out.println(row.get(0, String.class) + " " + row.get(1, String.class));
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Union_Empty_Result() throws SQLException {
        SubQueryExpression<Integer> sq1 = sq().from(employee).where(employee.firstname.eq("XXX")).unique(employee.id);
        SubQueryExpression<Integer> sq2 = sq().from(employee).where(employee.firstname.eq("YYY")).unique(employee.id);
        List<Integer> list = query().union(sq1, sq2).list();
        assertTrue(list.isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Union2() throws SQLException {
        List<Integer> list = query().union(
                sq().from(employee).unique(employee.id.max()),
                sq().from(employee).unique(employee.id.min())).list();
        assertFalse(list.isEmpty());

    }

    @Test
    @SuppressWarnings("unchecked")
    public void Union3() throws SQLException {
        SimpleSubQuery<Tuple> sq3 = sq().from(employee).unique(new Expression[]{employee.id.max()});
        SimpleSubQuery<Tuple> sq4 = sq().from(employee).unique(new Expression[]{employee.id.min()});
        List<Tuple> list2 = query().union(sq3, sq4).list();
        assertFalse(list2.isEmpty());
    }

    @Test
    @ExcludeIn({DERBY})
    public void Union4() {
        SimpleSubQuery<Tuple> sq1 = sq().from(employee).unique(employee.id, employee.firstname);
        SimpleSubQuery<Tuple> sq2 = sq().from(employee).unique(employee.id, employee.firstname);
        query().union(employee, sq1, sq2).list(employee.id.count());
    }

    // FIXME for CUBRID
    // Teradata: The ORDER BY clause must contain only integer constants.
    @Test
    @ExcludeIn({DERBY, CUBRID, FIREBIRD, TERADATA})
    public void Union5() {
        /* (select e.ID, e.FIRSTNAME, superior.ID as sup_id, superior.FIRSTNAME as sup_name
         * from EMPLOYEE e join EMPLOYEE superior on e.SUPERIOR_ID = superior.ID)
         * union
         * (select e.ID, e.FIRSTNAME, null, null from EMPLOYEE e)
         * order by ID asc
         */
        QEmployee superior = new QEmployee("superior");
        ListSubQuery<Tuple> sq1 = sq().from(employee)
                .join(employee.superiorIdKey, superior)
                .list(employee.id, employee.firstname, superior.id.as("sup_id"), superior.firstname.as("sup_name"));
        ListSubQuery<Tuple> sq2 = sq().from(employee)
                .list(employee.id, employee.firstname, null, null);
        List<Tuple> results = query().union(sq1, sq2).orderBy(employee.id.asc()).list();
        for (Tuple result : results) {
            System.err.println(Arrays.asList(result));
        }
    }

    @Test
    @ExcludeIn({FIREBIRD, TERADATA}) // The ORDER BY clause must contain only integer constants.
    @SuppressWarnings("unchecked")
    public void Union_With_Order() throws SQLException {
        SubQueryExpression<Integer> sq1 = sq().from(employee).unique(employee.id);
        SubQueryExpression<Integer> sq2 = sq().from(employee).unique(employee.id);
        List<Integer> list = query().union(sq1, sq2).orderBy(employee.id.asc()).list();
        assertFalse(list.isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    @ExcludeIn(FIREBIRD)
    public void Union_Multi_Column_Projection_List() throws IOException{
        SubQueryExpression<Tuple> sq1 = sq().from(employee).unique(employee.id.max(), employee.id.max().subtract(1));
        SubQueryExpression<Tuple> sq2 = sq().from(employee).unique(employee.id.min(), employee.id.min().subtract(1));

        List<Tuple> list = query().union(sq1, sq2).list();
        assertEquals(2, list.size());
        assertTrue(list.get(0) != null);
        assertTrue(list.get(1) != null);
    }

    @SuppressWarnings("unchecked")
    @Test
    @ExcludeIn(FIREBIRD)
    public void Union_Multi_Column_Projection_Iterate() throws IOException{
        SubQueryExpression<Tuple> sq1 = sq().from(employee).unique(employee.id.max(), employee.id.max().subtract(1));
        SubQueryExpression<Tuple> sq2 = sq().from(employee).unique(employee.id.min(), employee.id.min().subtract(1));

        CloseableIterator<Tuple> iterator = query().union(sq1,sq2).iterate();
        try{
            assertTrue(iterator.hasNext());
            assertTrue(iterator.next() != null);
            assertTrue(iterator.next() != null);
            assertFalse(iterator.hasNext());
        }finally{
            iterator.close();
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void Union_Single_Column_Projections_List() throws IOException{
        SubQueryExpression<Integer> sq1 = sq().from(employee).unique(employee.id.max());
        SubQueryExpression<Integer> sq2 = sq().from(employee).unique(employee.id.min());

        List<Integer> list = query().union(sq1, sq2).list();
        assertEquals(2, list.size());
        assertTrue(list.get(0) != null);
        assertTrue(list.get(1) != null);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void Union_Single_Column_Projections_Iterate() throws IOException{
        SubQueryExpression<Integer> sq1 = sq().from(employee).unique(employee.id.max());
        SubQueryExpression<Integer> sq2 = sq().from(employee).unique(employee.id.min());

        CloseableIterator<Integer> iterator = query().union(sq1,sq2).iterate();
        try{
            assertTrue(iterator.hasNext());
            assertTrue(iterator.next() != null);
            assertTrue(iterator.next() != null);
            assertFalse(iterator.hasNext());
        }finally{
            iterator.close();
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void Union_FactoryExpression() {
        ListSubQuery<Employee> sq1 = sq().from(employee)
                .list(Projections.constructor(Employee.class, employee.id));
        ListSubQuery<Employee> sq2 = sq().from(employee)
                .list(Projections.constructor(Employee.class, employee.id));
        List<Employee> employees = query().union(sq1, sq2).list();
        for (Employee employee : employees) {
            assertNotNull(employee);
        }
    }

    @Test
    @ExcludeIn({DERBY, CUBRID})
    public void Union_Clone() {
        NumberPath<Integer> idAlias = new NumberPath<Integer>(Integer.class, "id");
        ListSubQuery<Employee> sq1 = sq().from(employee)
                .list(Projections.constructor(Employee.class, employee.id.as(idAlias)));
        ListSubQuery<Employee> sq2 = sq().from(employee)
                .list(Projections.constructor(Employee.class, employee.id.as(idAlias)));

        TestQuery query = query();
        query.union(sq1, sq2);
        query.clone().list(idAlias);
    }

}
