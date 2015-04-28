package com.querydsl.sql;

import static com.querydsl.core.Target.*;
import static com.querydsl.sql.Constants.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.querydsl.core.Tuple;
import com.querydsl.core.testutil.ExcludeIn;
import com.querydsl.core.testutil.IncludeIn;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.SimplePath;
import com.querydsl.core.types.dsl.Wildcard;

public class SelectWindowFunctionsBase extends AbstractBaseTest {

    @Test
    @ExcludeIn(SQLSERVER) // FIXME
    public void WindowFunctions() {
        NumberPath<Integer> path = survey.id;
        NumberPath<?> path2 = survey.id;

        List<WindowOver<?>> exprs = new ArrayList<WindowOver<?>>();
        add(exprs, SQLExpressions.avg(path));
        add(exprs, SQLExpressions.count(path));
        add(exprs, SQLExpressions.corr(path, path2));
        add(exprs, SQLExpressions.covarPop(path, path2), DB2);
        add(exprs, SQLExpressions.covarSamp(path, path2), DB2);
        add(exprs, SQLExpressions.cumeDist(), DB2, TERADATA);
        add(exprs, SQLExpressions.denseRank(), TERADATA);
        add(exprs, SQLExpressions.firstValue(path), TERADATA);
        add(exprs, SQLExpressions.lag(path), TERADATA);
        add(exprs, SQLExpressions.lastValue(path), TERADATA);
        add(exprs, SQLExpressions.lead(path), TERADATA);
        add(exprs, SQLExpressions.max(path));
        add(exprs, SQLExpressions.min(path));
        add(exprs, SQLExpressions.nthValue(path, 2), DB2, TERADATA);
        add(exprs, SQLExpressions.ntile(3), DB2, TERADATA);
        add(exprs, SQLExpressions.percentRank(), DB2);
        add(exprs, SQLExpressions.rank());
        add(exprs, SQLExpressions.rowNumber());
        add(exprs, SQLExpressions.stddev(path), TERADATA);
        add(exprs, SQLExpressions.stddevPop(path), DB2, TERADATA);
        add(exprs, SQLExpressions.stddevSamp(path), DB2, TERADATA);
        add(exprs, SQLExpressions.sum(path));
        add(exprs, SQLExpressions.variance(path), TERADATA);
        add(exprs, SQLExpressions.varPop(path), DB2, TERADATA);
        add(exprs, SQLExpressions.varSamp(path), DB2, TERADATA);

        for (WindowOver<?> wo : exprs) {
            query().from(survey).select(wo.over().partitionBy(survey.name).orderBy(survey.id)).fetch();
        }
    }

    @Test
    public void WindowFunctions_Manual_Paging() {
        Expression<Long> rowNumber = SQLExpressions.rowNumber().over().orderBy(employee.lastname.asc()).as("rn");
        Expression<Object[]> all = Wildcard.all;

        // simple
        System.out.println("#1");
        for (Tuple row : query().from(employee).select(employee.firstname, employee.lastname, rowNumber).fetch()) {
            System.out.println(row);
        }
        System.out.println();

        // with subquery, generic alias
        System.out.println("#2");
        SQLQuery<Tuple> sub = query().from(employee).select(employee.firstname, employee.lastname, rowNumber);
        SimplePath<Tuple> subAlias = Expressions.path(Tuple.class, "s");
        for (Object[] row : query().from(sub.as(subAlias)).select(all).fetch()) {
            System.out.println(Arrays.asList(row));
        }
        System.out.println();

        // with subquery, only row number
        System.out.println("#3");
        SQLQuery<Long> sub2 = query().from(employee).select(rowNumber);
        SimplePath<Long> subAlias2 = Expressions.path(Long.class, "s");
        for (Object[] row : query().from(sub2.as(subAlias2)).select(all).fetch()) {
            System.out.println(Arrays.asList(row));
        }
        System.out.println();

        // with subquery, specific alias
        System.out.println("#4");
        SQLQuery<Tuple> sub3 = query().from(employee).select(employee.firstname, employee.lastname, rowNumber);
        for (Tuple row : query().from(sub3.as(employee2)).select(employee2.firstname, employee2.lastname).fetch()) {
            System.out.println(Arrays.asList(row));
        }
    }

    @Test
    @IncludeIn(ORACLE)
    public void WindowFunctions_Keep() {
        List<WindowOver<?>> exprs = new ArrayList<WindowOver<?>>();
        NumberPath<Integer> path = survey.id;

        add(exprs, SQLExpressions.avg(path));
        add(exprs, SQLExpressions.count(path));
        add(exprs, SQLExpressions.max(path));
        add(exprs, SQLExpressions.min(path));
        add(exprs, SQLExpressions.stddev(path));
        add(exprs, SQLExpressions.variance(path));

        for (WindowOver<?> wo : exprs) {
            query().from(survey).select(wo.keepFirst().orderBy(survey.id)).fetch();
        }
    }

    @Test
    @ExcludeIn({DB2, SQLSERVER})
    public void WindowFunctions_Regr() {
        List<WindowOver<?>> exprs = new ArrayList<WindowOver<?>>();
        NumberPath<Integer> path = survey.id;
        NumberPath<?> path2 = survey.id;

        add(exprs, SQLExpressions.regrSlope(path,  path2), SQLSERVER);
        add(exprs, SQLExpressions.regrIntercept(path,  path2));
        add(exprs, SQLExpressions.regrCount(path,  path2));
        add(exprs, SQLExpressions.regrR2(path,  path2));
        add(exprs, SQLExpressions.regrAvgx(path, path2));
        add(exprs, SQLExpressions.regrSxx(path, path2));
        add(exprs, SQLExpressions.regrSyy(path, path2));
        add(exprs, SQLExpressions.regrSxy(path,  path2));

        for (WindowOver<?> wo : exprs) {
            query().from(survey).select(wo.over().partitionBy(survey.name).orderBy(survey.id)).fetch();
        }
    }

    @Test
    @IncludeIn(ORACLE)
    public void WindowFunctions_Oracle() {
        List<WindowOver<?>> exprs = new ArrayList<WindowOver<?>>();
        NumberPath<Integer> path = survey.id;
        add(exprs, SQLExpressions.countDistinct(path));
        add(exprs, SQLExpressions.ratioToReport(path));
        add(exprs, SQLExpressions.stddevDistinct(path));

        for (WindowOver<?> wo : exprs) {
            query().from(survey).select(wo.over().partitionBy(survey.name)).fetch();
        }
    }

    @Test
    public void WindowFunctions_Over() {
        //SELECT Shipment_id,Ship_date, SUM(Qty) OVER () AS Total_Qty
        //FROM TestDB.Shipment

        query().from(employee).select(
                employee.id,
                SQLExpressions.sum(employee.salary).over()).fetch();
    }

    @Test
    public void WindowFunctions_PartitionBy() {
        //SELECT Shipment_id,Ship_date,Ship_Type,
        //SUM(Qty) OVER (PARTITION BY Ship_Type ) AS Total_Qty
        //FROM TestDB.Shipment

        query().from(employee).select(
                employee.id,
                employee.superiorId,
                SQLExpressions.sum(employee.salary).over()
                    .partitionBy(employee.superiorId)).fetch();
    }

    @Test
    @ExcludeIn(SQLSERVER)
    public void WindowFunctions_OrderBy() {
        //SELECT Shipment_id,Ship_date,Ship_Type,
        //SUM(Qty) OVER (PARTITION BY Ship_Type ORDER BY Ship_Dt ) AS Total_Qty
        //FROM TestDB.Shipment

        query().from(employee).select(
                employee.id,
                SQLExpressions.sum(employee.salary).over()
                    .partitionBy(employee.superiorId)
                    .orderBy(employee.datefield)).fetch();
    }

    @Test
    @ExcludeIn(SQLSERVER)
    public void WindowFunctions_UnboundedRows() {
        //SELECT Shipment_id,Ship_date,Ship_Type,
        //SUM(Qty) OVER (PARTITION BY Ship_Type ORDER BY Ship_Dt
        //ROWS BETWEEN UNBOUNDED PRECEDING
        //AND CURRENT ROW) AS Total_Qty
        //FROM TestDB.Shipment

        query().from(employee).select(
                employee.id,
                SQLExpressions.sum(employee.salary).over()
                    .partitionBy(employee.superiorId)
                    .orderBy(employee.datefield)
                    .rows().between().unboundedPreceding().currentRow()).fetch();
    }

    @Test
    @IncludeIn({TERADATA})
    public void WindowFunctions_Qualify() {
        //SELECT Shipment_id,Ship_date,Ship_Type,
        //Rank() OVER (PARTITION BY Ship_Type ORDER BY Ship_Dt ) AS rnk
        //FROM TestDB.Shipment
        //QUALIFY  (Rank() OVER (PARTITION BY Ship_Type ORDER BY Ship_Dt ))  =1

        teradataQuery().from(employee)
               .qualify(SQLExpressions.rank().over()
                       .partitionBy(employee.superiorId)
                       .orderBy(employee.datefield).eq(1l))
               .select(employee.id,SQLExpressions.sum(employee.salary).over()).fetch();

    }

}
