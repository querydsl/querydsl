package com.querydsl.sql;

import static com.querydsl.sql.Constants.employee;
import static com.querydsl.core.Target.ORACLE;
import static com.querydsl.sql.oracle.OracleGrammar.level;

import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.querydsl.sql.domain.QEmployee;
import com.querydsl.sql.oracle.OracleQuery;
import com.querydsl.core.testutil.IncludeIn;

public class SelectOracleBase extends AbstractBaseTest {

    private static final Logger logger = LoggerFactory.getLogger(AbstractSQLQuery.class);

    protected OracleQuery oracleQuery() {
        return new OracleQuery(connection, configuration) {
            @Override
            protected SQLSerializer serialize(boolean forCountRow) {
                SQLSerializer serializer = super.serialize(forCountRow);
                String rv = serializer.toString();
                if (expectedQuery != null) {
                   Assert.assertEquals(expectedQuery, rv.replace('\n', ' '));
                   expectedQuery = null;
                }
                logger.debug(rv);
                return serializer;
            }
        };
    }

    @Test
    @Ignore
    public void ConnectBy() throws SQLException {
        // TODO : come up with a legal case
        oracleQuery().from(employee)
            .where(level.eq(-1))
            .connectBy(level.lt(1000))
            .list(employee.id);
    }

    @Test
    @IncludeIn(ORACLE)
    @SkipForQuoted
    public void ConnectByPrior() throws SQLException {
        expectedQuery =  "select e.ID, e.LASTNAME, e.SUPERIOR_ID " +
                        "from EMPLOYEE e " +
                        "connect by prior e.ID = e.SUPERIOR_ID";
        oracleQuery().from(employee)
            .connectByPrior(employee.id.eq(employee.superiorId))
            .list(employee.id, employee.lastname, employee.superiorId);
    }

    @Test
    @IncludeIn(ORACLE)
    @SkipForQuoted
    public void ConnectByPrior2() throws SQLException {
        if (configuration.getUseLiterals()) return;

        expectedQuery =
                "select e.ID, e.LASTNAME, e.SUPERIOR_ID " +
                "from EMPLOYEE e " +
                "start with e.ID = ? " +
                "connect by prior e.ID = e.SUPERIOR_ID";
        oracleQuery().from(employee)
            .startWith(employee.id.eq(1))
            .connectByPrior(employee.id.eq(employee.superiorId))
            .list(employee.id, employee.lastname, employee.superiorId);
    }

    @Test
    @IncludeIn(ORACLE)
    @SkipForQuoted
    public void ConnectByPrior3() throws SQLException {
        if (configuration.getUseLiterals()) return;

        expectedQuery =
                "select e.ID, e.LASTNAME, e.SUPERIOR_ID " +
                "from EMPLOYEE e " +
                "start with e.ID = ? " +
                "connect by prior e.ID = e.SUPERIOR_ID " +
                "order siblings by e.LASTNAME";
        oracleQuery().from(employee)
            .startWith(employee.id.eq(1))
            .connectByPrior(employee.id.eq(employee.superiorId))
            .orderSiblingsBy(employee.lastname)
            .list(employee.id, employee.lastname, employee.superiorId);
    }

    @Test
    @IncludeIn(ORACLE)
    @SkipForQuoted
    public void ConnectByPrior4() throws SQLException {
        if (configuration.getUseLiterals()) return;

        expectedQuery =
                "select e.ID, e.LASTNAME, e.SUPERIOR_ID " +
                "from EMPLOYEE e " +
                "connect by nocycle prior e.ID = e.SUPERIOR_ID";
        oracleQuery().from(employee)
            .connectByNocyclePrior(employee.id.eq(employee.superiorId))
            .list(employee.id, employee.lastname, employee.superiorId);
    }

    @Test
    @IncludeIn(ORACLE)
    @SkipForQuoted
    public void SumOver() throws SQLException{
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
            "sum(e.SALARY) over () from EMPLOYEE e order by e.SALARY asc, e.SUPERIOR_ID asc";

        oracleQuery().from(employee)
            .orderBy(employee.salary.asc(), employee.superiorId.asc())
            .list(
               employee.lastname,
               employee.salary,
               SQLExpressions.sum(employee.salary).over().partitionBy(employee.superiorId).orderBy(employee.lastname, employee.salary),
               SQLExpressions.sum(employee.salary).over().orderBy(employee.superiorId, employee.salary),
               SQLExpressions.sum(employee.salary).over());

        // shorter version
        QEmployee e = employee;
        oracleQuery().from(e)
            .orderBy(e.salary.asc(), e.superiorId.asc())
            .list(e.lastname, e.salary,
               SQLExpressions.sum(e.salary).over().partitionBy(e.superiorId).orderBy(e.lastname, e.salary),
               SQLExpressions.sum(e.salary).over().orderBy(e.superiorId, e.salary),
               SQLExpressions.sum(e.salary).over());
    }

}
