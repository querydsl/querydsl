package com.querydsl.sql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.querydsl.core.support.Expressions;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.path.NumberPath;

public class WithinGroupTest {

    private static String toString(Expression<?> e) {
        return new SQLSerializer(Configuration.DEFAULT).handle(e).toString();
    }

    @Test
    public void All() {
        NumberPath<Long> path = Expressions.numberPath(Long.class, "path");
        NumberPath<Long> path2 = Expressions.numberPath(Long.class, "path2");

        assertEquals("cume_dist(path)", toString(SQLExpressions.cumeDist(path)));
        assertEquals("cume_dist(path, path2)", toString(SQLExpressions.cumeDist(path, path2)));
        assertEquals("dense_rank(path, path2)", toString(SQLExpressions.denseRank(path, path2)));
        assertEquals("listagg(path,',')", toString(SQLExpressions.listagg(path, ",")));
        assertEquals("percent_rank(path, path2)", toString(SQLExpressions.percentRank(path, path2)));
        assertEquals("percentile_cont(path)", toString(SQLExpressions.percentileCont(path)));
        assertEquals("percentile_disc(path)", toString(SQLExpressions.percentileDisc(path)));
        assertEquals("rank(path, path2)", toString(SQLExpressions.rank(path, path2)));
    }

}
