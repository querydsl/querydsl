package com.querydsl.r2dbc;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.sql.WindowFunction;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WindowFunctionTest {

    private static String toString(Expression<?> e) {
        return new SQLSerializer(Configuration.DEFAULT).handle(e).toString();
    }

    @Test
    public void complex() {
        NumberPath<Long> path = Expressions.numberPath(Long.class, "path");
        NumberPath<Long> path2 = Expressions.numberPath(Long.class, "path2");
        Expression<?> wf = R2DBCExpressions.sum(path).over().partitionBy(path2).orderBy(path);
        assertEquals("sum(path) over (partition by path2 order by path asc)", toString(wf));
    }

    @Test
    public void complex_nullsFirst() {
        NumberPath<Long> path = Expressions.numberPath(Long.class, "path");
        NumberPath<Long> path2 = Expressions.numberPath(Long.class, "path2");
        Expression<?> wf = R2DBCExpressions.sum(path).over().partitionBy(path2).orderBy(path.desc().nullsFirst());
        assertEquals("sum(path) over (partition by path2 order by path desc nulls first)", toString(wf));
    }

    @Test
    public void all() {
        NumberPath<Long> path = Expressions.numberPath(Long.class, "path");
        NumberPath<Long> path2 = Expressions.numberPath(Long.class, "path2");
        assertEquals("avg(path)", toString(R2DBCExpressions.avg(path)));
        assertEquals("count(path)", toString(R2DBCExpressions.count(path)));
        assertEquals("corr(path,path2)", toString(R2DBCExpressions.corr(path, path2)));
        assertEquals("covar_pop(path,path2)", toString(R2DBCExpressions.covarPop(path, path2)));
        assertEquals("covar_samp(path,path2)", toString(R2DBCExpressions.covarSamp(path, path2)));
        assertEquals("cume_dist()", toString(R2DBCExpressions.cumeDist()));
        assertEquals("dense_rank()", toString(R2DBCExpressions.denseRank()));
        assertEquals("first_value(path)", toString(R2DBCExpressions.firstValue(path)));
        assertEquals("lag(path)", toString(R2DBCExpressions.lag(path)));
        assertEquals("last_value(path)", toString(R2DBCExpressions.lastValue(path)));
        assertEquals("lead(path)", toString(R2DBCExpressions.lead(path)));
        assertEquals("max(path)", toString(R2DBCExpressions.max(path)));
        assertEquals("min(path)", toString(R2DBCExpressions.min(path)));
        assertEquals("nth_value(path, ?)", toString(R2DBCExpressions.nthValue(path, 3)));
        assertEquals("ntile(?)", toString(R2DBCExpressions.ntile(4)));
        assertEquals("percent_rank()", toString(R2DBCExpressions.percentRank()));
        assertEquals("rank()", toString(R2DBCExpressions.rank()));
        assertEquals("ratio_to_report(path)", toString(R2DBCExpressions.ratioToReport(path)));
        assertEquals("row_number()", toString(R2DBCExpressions.rowNumber()));
        assertEquals("stddev(path)", toString(R2DBCExpressions.stddev(path)));
        assertEquals("stddev(distinct path)", toString(R2DBCExpressions.stddevDistinct(path)));
        assertEquals("stddev_pop(path)", toString(R2DBCExpressions.stddevPop(path)));
        assertEquals("stddev_samp(path)", toString(R2DBCExpressions.stddevSamp(path)));
        assertEquals("sum(path)", toString(R2DBCExpressions.sum(path)));
        assertEquals("variance(path)", toString(R2DBCExpressions.variance(path)));
        assertEquals("var_pop(path)", toString(R2DBCExpressions.varPop(path)));
        assertEquals("var_samp(path)", toString(R2DBCExpressions.varSamp(path)));

        // TODO FIRST
        // TODO LAST
        // TODO NTH_VALUE ... FROM (FIRST|LAST) (RESPECT|IGNORE) NULLS
    }

    @Test
    public void regr() {
        NumberPath<Long> path = Expressions.numberPath(Long.class, "path");
        NumberPath<Long> path2 = Expressions.numberPath(Long.class, "path2");
        assertEquals("regr_slope(path, path2)", toString(R2DBCExpressions.regrSlope(path, path2)));
        assertEquals("regr_intercept(path, path2)", toString(R2DBCExpressions.regrIntercept(path, path2)));
        assertEquals("regr_count(path, path2)", toString(R2DBCExpressions.regrCount(path, path2)));
        assertEquals("regr_r2(path, path2)", toString(R2DBCExpressions.regrR2(path, path2)));
        assertEquals("regr_avgx(path, path2)", toString(R2DBCExpressions.regrAvgx(path, path2)));
        assertEquals("regr_avgy(path, path2)", toString(R2DBCExpressions.regrAvgy(path, path2)));
        assertEquals("regr_sxx(path, path2)", toString(R2DBCExpressions.regrSxx(path, path2)));
        assertEquals("regr_syy(path, path2)", toString(R2DBCExpressions.regrSyy(path, path2)));
        assertEquals("regr_sxy(path, path2)", toString(R2DBCExpressions.regrSxy(path, path2)));
    }

    @Test
    public void rows_between() {
        NumberPath<Long> path = Expressions.numberPath(Long.class, "path");
        NumberPath<Integer> intPath = Expressions.numberPath(Integer.class, "intPath");
        WindowFunction<Long> wf = R2DBCExpressions.sum(path).over().orderBy(path);

        assertEquals("sum(path) over (order by path asc rows between current row and unbounded following)",
                toString(wf.rows().between().currentRow().unboundedFollowing()));
        assertEquals("sum(path) over (order by path asc rows between preceding intPath and following intPath)",
                toString(wf.rows().between().preceding(intPath).following(intPath)));
        assertEquals("sum(path) over (order by path asc rows between preceding ? and following ?)",
                toString(wf.rows().between().preceding(1).following(3)));
    }

    @Test
    public void rows_unboundedPreceding() {
        NumberPath<Long> path = Expressions.numberPath(Long.class, "path");
        WindowFunction<Long> wf = R2DBCExpressions.sum(path).over().orderBy(path);

        assertEquals("sum(path) over (order by path asc rows unbounded preceding)",
                toString(wf.rows().unboundedPreceding()));
    }

    @Test
    public void rows_currentRow() {
        NumberPath<Long> path = Expressions.numberPath(Long.class, "path");
        WindowFunction<Long> wf = R2DBCExpressions.sum(path).over().orderBy(path);

        assertEquals("sum(path) over (order by path asc rows current row)",
                toString(wf.rows().currentRow()));
    }

    @Test
    public void rows_precedingRow() {
        NumberPath<Long> path = Expressions.numberPath(Long.class, "path");
        NumberPath<Integer> intPath = Expressions.numberPath(Integer.class, "intPath");
        WindowFunction<Long> wf = R2DBCExpressions.sum(path).over().orderBy(path);

        assertEquals("sum(path) over (order by path asc rows preceding intPath)",
                toString(wf.rows().preceding(intPath)));
        assertEquals("sum(path) over (order by path asc rows preceding ?)",
                toString(wf.rows().preceding(3)));
    }

    @Test
    public void keep_first() {
        //MIN(salary) KEEP (DENSE_RANK FIRST ORDER BY commission_pct) OVER (PARTITION BY department_id)
        NumberPath<Long> path = Expressions.numberPath(Long.class, "path");
        NumberPath<Long> path2 = Expressions.numberPath(Long.class, "path2");
        NumberPath<Long> path3 = Expressions.numberPath(Long.class, "path3");
        assertEquals(
                "min(path) keep (dense_rank first order by path2 asc)",
                toString(R2DBCExpressions.min(path).keepFirst().orderBy(path2)));
        assertEquals(
                "min(path) keep (dense_rank first order by path2 asc) over (partition by path3)",
                toString(R2DBCExpressions.min(path).keepFirst().orderBy(path2).over().partitionBy(path3)));
    }

    @Test
    public void keep_last() {
        //MIN(salary) KEEP (DENSE_RANK FIRST ORDER BY commission_pct) OVER (PARTITION BY department_id)
        NumberPath<Long> path = Expressions.numberPath(Long.class, "path");
        NumberPath<Long> path2 = Expressions.numberPath(Long.class, "path2");
        NumberPath<Long> path3 = Expressions.numberPath(Long.class, "path3");
        assertEquals(
                "min(path) keep (dense_rank last order by path2 asc) over (partition by path3)",
                toString(R2DBCExpressions.min(path).keepLast().orderBy(path2).over().partitionBy(path3)));
    }


}
