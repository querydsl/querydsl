package com.querydsl.sql;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;

public class WithinGroupTest {
    NumberPath<Long> path = null;
    NumberPath<Long> path2 = null;

    private static String toString(Expression<?> e) {
        return new SQLSerializer(Configuration.DEFAULT).handle(e).toString();
    }
    @Before
    public void setPaths(){
        this.path = Expressions.numberPath(Long.class, "path");
        this.path2 = Expressions.numberPath(Long.class, "path2");
    }

    @Test
    public void cume_Dist(){
        assertEquals("cume_dist(path)", toString(SQLExpressions.cumeDist(path)));
        assertEquals("cume_dist(path, path2)", toString(SQLExpressions.cumeDist(path, path2)));
    }

    @Test
    public void dense_Rank(){
        assertEquals("dense_rank(path, path2)", toString(SQLExpressions.denseRank(path, path2)));
    }

    @Test
    public void perfect_Rank(){
        assertEquals("percent_rank(path, path2)", toString(SQLExpressions.percentRank(path, path2)));
    }

    @Test
    public void percentile(){
        assertEquals("percentile_cont(path)", toString(SQLExpressions.percentileCont(path)));
        assertEquals("percentile_disc(path)", toString(SQLExpressions.percentileDisc(path)));
    }

    @Test
    public void rank(){
        assertEquals("rank(path, path2)", toString(SQLExpressions.rank(path, path2)));
    }

    @Test
    public void listaggComma() {
        assertEquals("listagg(path,',')", toString(SQLExpressions.listagg(path, ",")));
    }

    @Test
    public void listaggEmpty() {
        assertEquals("listagg(path,'')", toString(SQLExpressions.listagg(path, "")));
    }

    @Test
    public void listaggSpace() {
        assertEquals("listagg(path,' ')", toString(SQLExpressions.listagg(path, " ")));
    }

    @Test
    public void listaggDelimiter() {
        assertEquals("listagg(path,'|')", toString(SQLExpressions.listagg(path, "|")));
    }

    @Test
    public void listaggCommaWithSpace() {
        assertEquals("listagg(path,', ')", toString(SQLExpressions.listagg(path, ", ")));
    }

    @Test
    public void listaggIntString() {
        assertEquals("listagg(path,'1')", toString(SQLExpressions.listagg(path, "1")));
    }

}