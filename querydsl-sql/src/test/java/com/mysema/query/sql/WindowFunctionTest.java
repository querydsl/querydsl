package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.support.Expressions;
import com.mysema.query.types.Expression;
import com.mysema.query.types.path.NumberPath;

public class WindowFunctionTest {

    private static String toString(Expression<?> e) {
        return new SQLSerializer(Configuration.DEFAULT).handle(e).toString();
    }

    @Test
    public void Complex() {
        NumberPath<Long> path = Expressions.numberPath(Long.class, "path");
        NumberPath<Long> path2 = Expressions.numberPath(Long.class, "path2");
        Expression<?> wf = SQLExpressions.sum(path).over().partitionBy(path2).orderBy(path);
        assertEquals("sum(path) over (partition by path2 order by path)", toString(wf));
    }

    @Test
    public void All() {
        NumberPath<Long> path = Expressions.numberPath(Long.class, "path");
        NumberPath<Long> path2 = Expressions.numberPath(Long.class, "path2");
        assertEquals("avg(path)", toString(SQLExpressions.avg(path)));
        assertEquals("count(path)", toString(SQLExpressions.count(path)));
        assertEquals("corr(path,path2)", toString(SQLExpressions.corr(path, path2)));
        assertEquals("covar_pop(path,path2)", toString(SQLExpressions.covarPop(path, path2)));
        assertEquals("covar_samp(path,path2)", toString(SQLExpressions.covarSamp(path, path2)));
        assertEquals("cume_dist()", toString(SQLExpressions.cumeDist()));
        assertEquals("dense_rank()", toString(SQLExpressions.denseRank()));
        assertEquals("first_value(path)", toString(SQLExpressions.firstValue(path)));
        assertEquals("lag(path)", toString(SQLExpressions.lag(path)));
        assertEquals("last_value(path)", toString(SQLExpressions.lastValue(path)));
        assertEquals("lead(path)", toString(SQLExpressions.lead(path)));
        // TODO LISTAGG
        assertEquals("max(path)", toString(SQLExpressions.max(path)));
        assertEquals("min(path)", toString(SQLExpressions.min(path)));
        // TODO NTH_VALUE *
        assertEquals("ntile(?)", toString(SQLExpressions.ntile(4)));
        assertEquals("percent_rank()", toString(SQLExpressions.percentRank()));
        // TODO PERCENTILE_CONT
        // TODO PERCENTILE_DISC
        assertEquals("rank()", toString(SQLExpressions.rank()));
        assertEquals("ratio_to_report(path)", toString(SQLExpressions.ratioToReport(path)));
        // TODO REGR_ (Linear Regression) Functions *
        assertEquals("row_number()", toString(SQLExpressions.rowNumber()));
        assertEquals("stddev(path)", toString(SQLExpressions.stddev(path)));
        assertEquals("stddev(distinct path)", toString(SQLExpressions.stddevDistinct(path)));
        assertEquals("stddev_pop(path)", toString(SQLExpressions.stddevPop(path)));
        assertEquals("stddev_samp(path)", toString(SQLExpressions.stddevSamp(path)));
        assertEquals("sum(path)", toString(SQLExpressions.sum(path)));
        assertEquals("variance(path)", toString(SQLExpressions.variance(path)));
        assertEquals("var_pop(path)", toString(SQLExpressions.varPop(path)));
        assertEquals("var_samp(path)", toString(SQLExpressions.varSamp(path)));
    }

    @Test
    public void Rows_Between() {
        NumberPath<Long> path = Expressions.numberPath(Long.class, "path");
        NumberPath<Integer> intPath = Expressions.numberPath(Integer.class, "intPath");
        WindowFunction<Long> wf = SQLExpressions.sum(path).over().orderBy(path);

        assertEquals("sum(path) over (order by path rows between current row and unbounded following)",
                toString(wf.rows().between().currentRow().unboundedFollowing()));
        assertEquals("sum(path) over (order by path rows between preceding intPath and following intPath)",
                toString(wf.rows().between().preceding(intPath).following(intPath)));
        assertEquals("sum(path) over (order by path rows between preceding ? and following ?)",
                toString(wf.rows().between().preceding(1).following(3)));
    }

    @Test
    public void Rows_UnboundedPreceding() {
        NumberPath<Long> path = Expressions.numberPath(Long.class, "path");
        WindowFunction<Long> wf = SQLExpressions.sum(path).over().orderBy(path);

        assertEquals("sum(path) over (order by path rows unbounded preceding)",
                toString(wf.rows().unboundedPreceding()));
    }

    @Test
    public void Rows_CurrentRow() {
        NumberPath<Long> path = Expressions.numberPath(Long.class, "path");
        WindowFunction<Long> wf = SQLExpressions.sum(path).over().orderBy(path);

        assertEquals("sum(path) over (order by path rows current row)",
                toString(wf.rows().currentRow()));
    }

    @Test
    public void Rows_PrecedingRow() {
        NumberPath<Long> path = Expressions.numberPath(Long.class, "path");
        NumberPath<Integer> intPath = Expressions.numberPath(Integer.class, "intPath");
        WindowFunction<Long> wf = SQLExpressions.sum(path).over().orderBy(path);

        assertEquals("sum(path) over (order by path rows preceding intPath)",
                toString(wf.rows().preceding(intPath)));
        assertEquals("sum(path) over (order by path rows preceding ?)",
                toString(wf.rows().preceding(3)));
    }

}
