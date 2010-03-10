/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.oracle;

import static com.mysema.query.Constants.employee;
import static com.mysema.query.sql.oracle.OracleGrammar.level;
import static com.mysema.query.sql.oracle.OracleGrammar.sumOver;

import java.sql.SQLException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mysema.query.Connections;
import com.mysema.query.SelectBaseTest;
import com.mysema.query.Target;
import com.mysema.query.sql.OracleTemplates;
import com.mysema.query.sql.domain.QEMPLOYEE;
import com.mysema.query.sql.oracle.OracleQuery;
import com.mysema.testutil.FilteringTestRunner;
import com.mysema.testutil.Label;
import com.mysema.testutil.ResourceCheck;

@RunWith(FilteringTestRunner.class)
@ResourceCheck("/oracle.run")
@Label(Target.ORACLE)
public class SelectOracleTest extends SelectBaseTest {

    @BeforeClass
    public static void setUp() throws Exception {
        Connections.initOracle();
    }

    @Before
    public void setUpForTest() {
        dialect = new OracleTemplates().newLineToSingleSpace();
    }
    

    @Test
    public void testConnectByPrior() throws SQLException{
        expectedQuery = 
                "select employee.id, employee.lastname, employee.superior_id " +
                        "from employee employee " +
                        "connect by prior employee.id = employee.superior_id";
        qo().from(employee)
            .connectByPrior(employee.id.eq(employee.superiorId))
            .list(employee.id, employee.lastname, employee.superiorId);
    }
    
    @Test
    public void testConnectByPrior2() throws SQLException{
        expectedQuery = 
                "select employee.id, employee.lastname, employee.superior_id " +
                "from employee employee " +
                "start with employee.id = ? " +
                "connect by prior employee.id = employee.superior_id";
        qo().from(employee)
            .startWith(employee.id.eq(1))
            .connectByPrior(employee.id.eq(employee.superiorId))
            .list(employee.id, employee.lastname, employee.superiorId);
    }
    
    @Test
    public void testConnectByPrior3() throws SQLException{
        expectedQuery = 
                "select employee.id, employee.lastname, employee.superior_id " +
                "from employee employee " +
                "start with employee.id = ? " +                
                "connect by prior employee.id = employee.superior_id " +
                "order siblings by employee.lastname";
        qo().from(employee)
            .startWith(employee.id.eq(1))
            .connectByPrior(employee.id.eq(employee.superiorId))
            .orderSiblingsBy(employee.lastname)
            .list(employee.id, employee.lastname, employee.superiorId);
    }
    
    @Test
    public void testConnectByPrior4() throws SQLException{
        expectedQuery = 
                "select employee.id, employee.lastname, employee.superior_id " +
                "from employee employee " +
                "connect by nocycle prior employee.id = employee.superior_id";
        qo().from(employee)
            .connectByNocyclePrior(employee.id.eq(employee.superiorId))
            .list(employee.id, employee.lastname, employee.superiorId);
    }
    
    @Test
    @Ignore
    public void testConnectBy() throws SQLException{
        // TODO : come up with a legal case
        qo().from(employee)
            .where(level.eq(-1))
            .connectBy(level.lt(1000))            
            .list(employee.id);
    }
    
    @Test
    public void testSumOver() throws SQLException{
//        SQL> select deptno,
//        2  ename,
//        3  sal,
//        4  sum(sal) over (partition by deptno
//        5  order by sal,ename) CumDeptTot,
//        6  sum(sal) over (partition by deptno) SalByDept,
//        7  sum(sal) over (order by deptno, sal) CumTot,
//        8  sum(sal) over () TotSal
//        9  from emp
//       10  order by deptno, sal;
        expectedQuery = "select employee.lastname, employee.salary, " +
                        "sum(employee.salary) over (partition by employee.superior_id order by employee.lastname, employee.salary), " +
                        "sum(employee.salary) over ( order by employee.superior_id, employee.salary), " +
                        "sum(employee.salary) over () from employee employee order by employee.salary asc, employee.superior_id asc";
        qo().from(employee)
            .orderBy(employee.salary.asc(), employee.superiorId.asc())
            .list(
               employee.lastname,
               employee.salary,
               sumOver(employee.salary).partition(employee.superiorId).order(employee.lastname, employee.salary),
               sumOver(employee.salary).order(employee.superiorId, employee.salary),
               sumOver(employee.salary));
        
        // shorter version
        QEMPLOYEE e = employee;
        qo().from(e)
            .orderBy(e.salary.asc(), e.superiorId.asc())
            .list(e.lastname, e.salary,
               sumOver(e.salary).partition(e.superiorId).order(e.lastname, e.salary),
               sumOver(e.salary).order(e.superiorId, e.salary),
               sumOver(e.salary));
    }
    
    protected OracleQuery qo(){
        return new OracleQuery(Connections.getConnection(), dialect){
            @Override
            protected String buildQueryString(boolean forCountRow) {
                String rv = super.buildQueryString(forCountRow);
                if (expectedQuery != null){
                   Assert.assertEquals(expectedQuery, rv);
                   expectedQuery = null;
                }
                System.out.println(rv);
                return rv;
            }
        };
    }


}
