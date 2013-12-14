package com.mysema.query;

import static com.mysema.query.Constants.employee;
import static com.mysema.query.Target.CUBRID;
import static com.mysema.query.Target.DERBY;
import static com.mysema.query.Target.MYSQL;
import static com.mysema.query.Target.TERADATA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.sql.domain.Employee;
import com.mysema.query.sql.domain.QEmployee;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Projections;
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.query.ListSubQuery;
import com.mysema.query.types.query.SimpleSubQuery;
import com.mysema.query.types.template.NumberTemplate;
import com.mysema.testutil.ExcludeIn;

public class UnionBase extends AbstractBaseTest {

    @Test
    @ExcludeIn(MYSQL)
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
    @ExcludeIn({DERBY, CUBRID, TERADATA})
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
    @ExcludeIn(TERADATA) // The ORDER BY clause must contain only integer constants.
    @SuppressWarnings("unchecked")
    public void Union_With_Order() throws SQLException {
        SubQueryExpression<Integer> sq1 = sq().from(employee).unique(employee.id);
        SubQueryExpression<Integer> sq2 = sq().from(employee).unique(employee.id);
        List<Integer> list = query().union(sq1, sq2).orderBy(employee.id.asc()).list();
        assertFalse(list.isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
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


}
