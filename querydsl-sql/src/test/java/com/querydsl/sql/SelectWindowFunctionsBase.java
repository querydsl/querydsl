package com.querydsl.sql;

import static com.querydsl.sql.Constants.employee;
import static com.querydsl.sql.Constants.employee2;
import static com.querydsl.sql.Constants.survey;
import static com.querydsl.core.Target.DB2;
import static com.querydsl.core.Target.ORACLE;
import static com.querydsl.core.Target.SQLSERVER;
import static com.querydsl.core.Target.TERADATA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.expr.Wildcard;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.SimplePath;
import com.querydsl.core.types.query.ListSubQuery;
import com.querydsl.core.types.query.SimpleSubQuery;
import com.querydsl.core.testutil.ExcludeIn;
import com.querydsl.core.testutil.IncludeIn;

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
            query().from(survey).list(wo.over().partitionBy(survey.name).orderBy(survey.id));
        }
    }

    @Test
    public void WindowFunctions_Manual_Paging() {
        Expression<Long> rowNumber = SQLExpressions.rowNumber().over().orderBy(employee.lastname.asc()).as("rn");
        Expression<Object[]> all = Wildcard.all;

        // simple
        System.out.println("#1");
        for (Tuple row : query().from(employee).list(employee.firstname, employee.lastname, rowNumber)) {
            System.out.println(row);
        }
        System.out.println();

        // with subquery, generic alias
        System.out.println("#2");
        ListSubQuery<Tuple> sub = sq().from(employee).list(employee.firstname, employee.lastname, rowNumber);
        SimplePath<Tuple> subAlias = new SimplePath<Tuple>(Tuple.class, "s");
        for (Object[] row : query().from(sub.as(subAlias)).list(all)) {
            System.out.println(Arrays.asList(row));
        }
        System.out.println();

        // with subquery, only row number
        System.out.println("#3");
        SimpleSubQuery<Long> sub2 = sq().from(employee).unique(rowNumber);
        SimplePath<Long> subAlias2 = new SimplePath<Long>(Long.class, "s");
        for (Object[] row : query().from(sub2.as(subAlias2)).list(all)) {
            System.out.println(Arrays.asList(row));
        }
        System.out.println();

        // with subquery, specific alias
        System.out.println("#4");
        ListSubQuery<Tuple> sub3 = sq().from(employee).list(employee.firstname, employee.lastname, rowNumber);
        for (Tuple row : query().from(sub3.as(employee2)).list(employee2.firstname, employee2.lastname)) {
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
            query().from(survey).list(wo.keepFirst().orderBy(survey.id));
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
            query().from(survey).list(wo.over().partitionBy(survey.name).orderBy(survey.id));
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
            query().from(survey).list(wo.over().partitionBy(survey.name));
        }
    }

    @Test
    public void WindowFunctions_Over() {
        //SELECT Shipment_id,Ship_date, SUM(Qty) OVER () AS Total_Qty
        //FROM TestDB.Shipment

        query().from(employee).list(
                employee.id,
                SQLExpressions.sum(employee.salary).over());
    }

    @Test
    public void WindowFunctions_PartitionBy() {
        //SELECT Shipment_id,Ship_date,Ship_Type,
        //SUM(Qty) OVER (PARTITION BY Ship_Type ) AS Total_Qty
        //FROM TestDB.Shipment

        query().from(employee).list(
                employee.id,
                employee.superiorId,
                SQLExpressions.sum(employee.salary).over()
                    .partitionBy(employee.superiorId));
    }

    @Test
    @ExcludeIn(SQLSERVER)
    public void WindowFunctions_OrderBy() {
        //SELECT Shipment_id,Ship_date,Ship_Type,
        //SUM(Qty) OVER (PARTITION BY Ship_Type ORDER BY Ship_Dt ) AS Total_Qty
        //FROM TestDB.Shipment

        query().from(employee).list(
                employee.id,
                SQLExpressions.sum(employee.salary).over()
                    .partitionBy(employee.superiorId)
                    .orderBy(employee.datefield));
    }

    @Test
    @ExcludeIn(SQLSERVER)
    public void WindowFunctions_UnboundedRows() {
        //SELECT Shipment_id,Ship_date,Ship_Type,
        //SUM(Qty) OVER (PARTITION BY Ship_Type ORDER BY Ship_Dt
        //ROWS BETWEEN UNBOUNDED PRECEDING
        //AND CURRENT ROW) AS Total_Qty
        //FROM TestDB.Shipment

        query().from(employee).list(
                employee.id,
                SQLExpressions.sum(employee.salary).over()
                    .partitionBy(employee.superiorId)
                    .orderBy(employee.datefield)
                    .rows().between().unboundedPreceding().currentRow());
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
               .list(employee.id,SQLExpressions.sum(employee.salary).over());

    }

}
