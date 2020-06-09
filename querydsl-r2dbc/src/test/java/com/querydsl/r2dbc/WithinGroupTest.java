package com.querydsl.r2dbc;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WithinGroupTest {

    private static String toString(Expression<?> e) {
        return new SQLSerializer(Configuration.DEFAULT).handle(e).toString();
    }

    @Test
    public void all() {
        NumberPath<Long> path = Expressions.numberPath(Long.class, "path");
        NumberPath<Long> path2 = Expressions.numberPath(Long.class, "path2");

        assertEquals("cume_dist(path)", toString(R2DBCExpressions.cumeDist(path)));
        assertEquals("cume_dist(path, path2)", toString(R2DBCExpressions.cumeDist(path, path2)));
        assertEquals("dense_rank(path, path2)", toString(R2DBCExpressions.denseRank(path, path2)));
        assertEquals("listagg(path,',')", toString(R2DBCExpressions.listagg(path, ",")));
        assertEquals("percent_rank(path, path2)", toString(R2DBCExpressions.percentRank(path, path2)));
        assertEquals("percentile_cont(path)", toString(R2DBCExpressions.percentileCont(path)));
        assertEquals("percentile_disc(path)", toString(R2DBCExpressions.percentileDisc(path)));
        assertEquals("rank(path, path2)", toString(R2DBCExpressions.rank(path, path2)));
    }

}
