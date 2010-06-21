/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query._oracle;

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
import com.mysema.query.sql.domain.QEmployee;
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
    public void limitAndOffsetInOracle() throws SQLException {
        // limit
        expectedQuery = "select * from (   select e.ID from EMPLOYEE2 e ) where rownum <= ?";
        query().from(employee).limit(4).list(employee.id);

        // offset
        expectedQuery = "select * from (  select a.*, rownum rn from (   select e.ID from EMPLOYEE2 e  ) a) where rn > ?";
        query().from(employee).offset(3).list(employee.id);

        // limit offset
        expectedQuery =  "select * from (  select a.*, rownum rn from (   select e.ID from EMPLOYEE2 e  ) a) where rn > 3 and rn <= 7";
        query().from(employee).limit(4).offset(3).list(employee.id);
    }

    @Test
    public void connectByPrior() throws SQLException{
        expectedQuery =  "select e.ID, e.LASTNAME, e.SUPERIOR_ID " +
                        "from EMPLOYEE2 e " +
                        "connect by prior e.ID = e.SUPERIOR_ID";
        qo().from(employee)
            .connectByPrior(employee.id.eq(employee.superiorId))
            .list(employee.id, employee.lastname, employee.superiorId);
    }

    @Test
    public void connectByPrior2() throws SQLException{
        expectedQuery =
                "select e.ID, e.LASTNAME, e.SUPERIOR_ID " +
                "from EMPLOYEE2 e " +
                "start with e.ID = ? " +
                "connect by prior e.ID = e.SUPERIOR_ID";
        qo().from(employee)
            .startWith(employee.id.eq(1))
            .connectByPrior(employee.id.eq(employee.superiorId))
            .list(employee.id, employee.lastname, employee.superiorId);
    }

    @Test
    public void connectByPrior3() throws SQLException{
        expectedQuery =
                "select e.ID, e.LASTNAME, e.SUPERIOR_ID " +
                "from EMPLOYEE2 e " +
                "start with e.ID = ? " +
                "connect by prior e.ID = e.SUPERIOR_ID " +
                "order siblings by e.LASTNAME";
        qo().from(employee)
            .startWith(employee.id.eq(1))
            .connectByPrior(employee.id.eq(employee.superiorId))
            .orderSiblingsBy(employee.lastname)
            .list(employee.id, employee.lastname, employee.superiorId);
    }

    @Test
    public void connectByPrior4() throws SQLException{
        expectedQuery =
                "select e.ID, e.LASTNAME, e.SUPERIOR_ID " +
                "from EMPLOYEE2 e " +
                "connect by nocycle prior e.ID = e.SUPERIOR_ID";
        qo().from(employee)
            .connectByNocyclePrior(employee.id.eq(employee.superiorId))
            .list(employee.id, employee.lastname, employee.superiorId);
    }

    @Test
    @Ignore
    public void connectBy() throws SQLException{
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
        expectedQuery = "select e.LASTNAME, e.SALARY, " +
            "sum(e.SALARY) over (partition by e.SUPERIOR_ID order by e.LASTNAME, e.SALARY), " +
            "sum(e.SALARY) over (order by e.SUPERIOR_ID, e.SALARY), " +
            "sum(e.SALARY) over () from EMPLOYEE2 e order by e.SALARY asc, e.SUPERIOR_ID asc";

        qo().from(employee)
            .orderBy(employee.salary.asc(), employee.superiorId.asc())
            .list(
               employee.lastname,
               employee.salary,
               sumOver(employee.salary).partition(employee.superiorId).order(employee.lastname, employee.salary),
               sumOver(employee.salary).order(employee.superiorId, employee.salary),
               sumOver(employee.salary));

        // shorter version
        QEmployee e = employee;
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
                   Assert.assertEquals(expectedQuery, rv.replace('\n', ' '));
                   expectedQuery = null;
                }
                System.out.println(rv);
                return rv;
            }
        };
    }

}
