package com.mysema.query;

import static com.mysema.query.Constants.employee;
import static com.mysema.query.Target.CUBRID;
import static com.mysema.query.Target.DERBY;
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
import com.mysema.testutil.ExcludeIn;

public class UnionBase extends AbstractBaseTest {

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
        SubQueryExpression<Object[]> sq1 = sq().from(employee).unique(employee.firstname, employee.lastname);
        SubQueryExpression<Object[]> sq2 = sq().from(employee).unique(employee.lastname, employee.firstname);
        List<Object[]> list = query().union(sq1, sq2).list();
        assertFalse(list.isEmpty());
        for (Object[] row : list){
            assertNotNull(row[0]);
            assertNotNull(row[1]);
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
        SimpleSubQuery<Object[]> sq3 = sq().from(employee).unique(new Expression[]{employee.id.max()});
        SimpleSubQuery<Object[]> sq4 = sq().from(employee).unique(new Expression[]{employee.id.min()});
        List<Object[]> list2 = query().union(sq3, sq4).list();
        assertFalse(list2.isEmpty());
    }
    
    @Test
    @ExcludeIn({DERBY})
    public void Union4() {
        SimpleSubQuery<Object[]> sq1 = sq().from(employee).unique(employee.id, employee.firstname);
        SimpleSubQuery<Object[]> sq2 = sq().from(employee).unique(employee.id, employee.firstname);
        query().union(employee, sq1, sq2).list(employee.id.count());
    }
    
    // FIXME for CUBRID
    @Test
    @ExcludeIn({DERBY, CUBRID})
    public void Union5() {
        /* (select e.ID, e.FIRSTNAME, superior.ID as sup_id, superior.FIRSTNAME as sup_name 
         * from EMPLOYEE e join EMPLOYEE superior on e.SUPERIOR_ID = superior.ID) 
         * union 
         * (select e.ID, e.FIRSTNAME, null, null from EMPLOYEE e) 
         * order by ID asc
         */
        QEmployee superior = new QEmployee("superior");
        ListSubQuery<Object[]> sq1 = sq().from(employee)
                .join(employee.superiorIdKey, superior)
                .list(employee.id, employee.firstname, superior.id.as("sup_id"), superior.firstname.as("sup_name"));
        ListSubQuery<Object[]> sq2 = sq().from(employee)
                .list(employee.id, employee.firstname, null, null);
        List<Object[]> results = query().union(sq1, sq2).orderBy(employee.id.asc()).list();
        for (Object[] result : results) {
            System.err.println(Arrays.asList(result));
        }
 
    }
    
    @Test
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
        SubQueryExpression<Object[]> sq1 = sq().from(employee).unique(employee.id.max(), employee.id.max().subtract(1));
        SubQueryExpression<Object[]> sq2 = sq().from(employee).unique(employee.id.min(), employee.id.min().subtract(1));

        List<Object[]> list = query().union(sq1, sq2).list();
        assertEquals(2, list.size());
        assertTrue(list.get(0) != null);
        assertTrue(list.get(1) != null);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void Union_Multi_Column_Projection_Iterate() throws IOException{
        SubQueryExpression<Object[]> sq1 = sq().from(employee).unique(employee.id.max(), employee.id.max().subtract(1));
        SubQueryExpression<Object[]> sq2 = sq().from(employee).unique(employee.id.min(), employee.id.min().subtract(1));

        CloseableIterator<Object[]> iterator = query().union(sq1,sq2).iterate();
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
        for (Employee employee : employees){
            assertNotNull(employee);
        }
    }
    
    
}
