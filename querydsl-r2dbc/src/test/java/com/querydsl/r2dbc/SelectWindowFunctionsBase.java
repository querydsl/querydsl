package com.querydsl.r2dbc;

import com.querydsl.core.Tuple;
import com.querydsl.core.testutil.ExcludeIn;
import com.querydsl.core.testutil.IncludeIn;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.SimplePath;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.sql.WindowOver;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.querydsl.core.Target.*;
import static com.querydsl.r2dbc.Constants.*;

public abstract class SelectWindowFunctionsBase extends AbstractBaseTest {

    @Test
    @ExcludeIn(SQLSERVER) // FIXME
    public void windowFunctions() {
        NumberPath<Integer> path = survey.id;
        NumberPath<?> path2 = survey.id;

        List<WindowOver<?>> exprs = new ArrayList<WindowOver<?>>();
        add(exprs, R2DBCExpressions.avg(path));
        add(exprs, R2DBCExpressions.count(path));
        add(exprs, R2DBCExpressions.corr(path, path2));
        add(exprs, R2DBCExpressions.covarPop(path, path2), DB2);
        add(exprs, R2DBCExpressions.covarSamp(path, path2), DB2);
        add(exprs, R2DBCExpressions.cumeDist(), DB2, TERADATA);
        add(exprs, R2DBCExpressions.denseRank(), TERADATA);
        add(exprs, R2DBCExpressions.firstValue(path), TERADATA);
        add(exprs, R2DBCExpressions.lag(path), TERADATA);
        add(exprs, R2DBCExpressions.lastValue(path), TERADATA);
        add(exprs, R2DBCExpressions.lead(path), TERADATA);
        add(exprs, R2DBCExpressions.max(path));
        add(exprs, R2DBCExpressions.min(path));
        add(exprs, R2DBCExpressions.nthValue(path, 2), DB2, TERADATA);
        add(exprs, R2DBCExpressions.ntile(3), DB2, TERADATA);
        add(exprs, R2DBCExpressions.percentRank(), DB2);
        add(exprs, R2DBCExpressions.rank());
        add(exprs, R2DBCExpressions.rowNumber());
        add(exprs, R2DBCExpressions.stddev(path), TERADATA);
        add(exprs, R2DBCExpressions.stddevPop(path), DB2, TERADATA);
        add(exprs, R2DBCExpressions.stddevSamp(path), DB2, TERADATA);
        add(exprs, R2DBCExpressions.sum(path));
        add(exprs, R2DBCExpressions.variance(path), TERADATA);
        add(exprs, R2DBCExpressions.varPop(path), DB2, TERADATA);
        add(exprs, R2DBCExpressions.varSamp(path), DB2, TERADATA);

        for (WindowOver<?> wo : exprs) {
            query().from(survey).select(wo.over().partitionBy(survey.name).orderBy(survey.id)).fetch().collectList().block();
        }
    }

    @Test
    public void windowFunctions_manual_paging() {
        Expression<Long> rowNumber = R2DBCExpressions.rowNumber().over().orderBy(employee.lastname.asc()).as("rn");
        Expression<Object[]> all = Wildcard.all;

        // simple
        System.out.println("#1");
        for (Tuple row : query().from(employee).select(employee.firstname, employee.lastname, rowNumber).fetch().collectList().block()) {
            System.out.println(row);
        }
        System.out.println();

        // with subquery, generic alias
        System.out.println("#2");
        R2DBCQuery<Tuple> sub = query().from(employee).select(employee.firstname, employee.lastname, rowNumber);
        SimplePath<Tuple> subAlias = Expressions.path(Tuple.class, "s");
        for (Object[] row : query().from(sub.as(subAlias)).select(all).fetch().collectList().block()) {
            System.out.println(Arrays.asList(row));
        }
        System.out.println();

        // with subquery, only row number
        System.out.println("#3");
        R2DBCQuery<Long> sub2 = query().from(employee).select(rowNumber);
        SimplePath<Long> subAlias2 = Expressions.path(Long.class, "s");
        for (Object[] row : query().from(sub2.as(subAlias2)).select(all).fetch().collectList().block()) {
            System.out.println(Arrays.asList(row));
        }
        System.out.println();

        // with subquery, specific alias
        System.out.println("#4");
        R2DBCQuery<Tuple> sub3 = query().from(employee).select(employee.firstname, employee.lastname, rowNumber);
        for (Tuple row : query().from(sub3.as(employee2)).select(employee2.firstname, employee2.lastname).fetch().collectList().block()) {
            System.out.println(Arrays.asList(row));
        }
    }

    @Test
    @IncludeIn(ORACLE)
    public void windowFunctions_keep() {
        List<WindowOver<?>> exprs = new ArrayList<WindowOver<?>>();
        NumberPath<Integer> path = survey.id;

        add(exprs, R2DBCExpressions.avg(path));
        add(exprs, R2DBCExpressions.count(path));
        add(exprs, R2DBCExpressions.max(path));
        add(exprs, R2DBCExpressions.min(path));
        add(exprs, R2DBCExpressions.stddev(path));
        add(exprs, R2DBCExpressions.variance(path));

        for (WindowOver<?> wo : exprs) {
            query().from(survey).select(wo.keepFirst().orderBy(survey.id)).fetch().collectList().block();
        }
    }

    @Test
    @ExcludeIn({DB2, SQLSERVER})
    public void windowFunctions_regr() {
        List<WindowOver<?>> exprs = new ArrayList<WindowOver<?>>();
        NumberPath<Integer> path = survey.id;
        NumberPath<?> path2 = survey.id;

        add(exprs, R2DBCExpressions.regrSlope(path, path2), SQLSERVER);
        add(exprs, R2DBCExpressions.regrIntercept(path, path2));
        add(exprs, R2DBCExpressions.regrCount(path, path2));
        add(exprs, R2DBCExpressions.regrR2(path, path2));
        add(exprs, R2DBCExpressions.regrAvgx(path, path2));
        add(exprs, R2DBCExpressions.regrSxx(path, path2));
        add(exprs, R2DBCExpressions.regrSyy(path, path2));
        add(exprs, R2DBCExpressions.regrSxy(path, path2));

        for (WindowOver<?> wo : exprs) {
            query().from(survey).select(wo.over().partitionBy(survey.name).orderBy(survey.id)).fetch().collectList().block();
        }
    }

    @Test
    @IncludeIn(ORACLE)
    public void windowFunctions_oracle() {
        List<WindowOver<?>> exprs = new ArrayList<WindowOver<?>>();
        NumberPath<Integer> path = survey.id;
        add(exprs, R2DBCExpressions.countDistinct(path));
        add(exprs, R2DBCExpressions.ratioToReport(path));
        add(exprs, R2DBCExpressions.stddevDistinct(path));

        for (WindowOver<?> wo : exprs) {
            query().from(survey).select(wo.over().partitionBy(survey.name)).fetch().collectList().block();
        }
    }

    @Test
    public void windowFunctions_over() {
        //SELECT Shipment_id,Ship_date, SUM(Qty) OVER () AS Total_Qty
        //FROM TestDB.Shipment

        query().from(employee).select(
                employee.id,
                R2DBCExpressions.sum(employee.salary).over()).fetch().collectList().block();
    }

    @Test
    public void windowFunctions_partitionBy() {
        //SELECT Shipment_id,Ship_date,Ship_Type,
        //SUM(Qty) OVER (PARTITION BY Ship_Type ) AS Total_Qty
        //FROM TestDB.Shipment

        query().from(employee).select(
                employee.id,
                employee.superiorId,
                R2DBCExpressions.sum(employee.salary).over()
                        .partitionBy(employee.superiorId)).fetch().collectList().block();
    }

    @Test
    @ExcludeIn(SQLSERVER)
    public void windowFunctions_orderBy() {
        //SELECT Shipment_id,Ship_date,Ship_Type,
        //SUM(Qty) OVER (PARTITION BY Ship_Type ORDER BY Ship_Dt ) AS Total_Qty
        //FROM TestDB.Shipment

        query().from(employee).select(
                employee.id,
                R2DBCExpressions.sum(employee.salary).over()
                        .partitionBy(employee.superiorId)
                        .orderBy(employee.datefield)).fetch().collectList().block();
    }

    @Test
    @ExcludeIn(SQLSERVER)
    public void windowFunctions_unboundedRows() {
        //SELECT Shipment_id,Ship_date,Ship_Type,
        //SUM(Qty) OVER (PARTITION BY Ship_Type ORDER BY Ship_Dt
        //ROWS BETWEEN UNBOUNDED PRECEDING
        //AND CURRENT ROW) AS Total_Qty
        //FROM TestDB.Shipment

        query().from(employee).select(
                employee.id,
                R2DBCExpressions.sum(employee.salary).over()
                        .partitionBy(employee.superiorId)
                        .orderBy(employee.datefield)
                        .rows().between().unboundedPreceding().currentRow()).fetch().collectList().block();
    }

}
