package com.mysema.query.sql;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.support.Expressions;
import com.mysema.query.types.Expression;
import com.mysema.query.types.path.NumberPath;

public class WindowFunctionTest {

    private static String toString(Expression<?> e) {
        return new SQLSerializer(SQLTemplates.DEFAULT).handle(e).toString();
    }
    
    @Test
    public void Complex() {
        NumberPath<Long> path = Expressions.numberPath(Long.class, "path");
        NumberPath<Long> path2 = Expressions.numberPath(Long.class, "path2");
        Expression<?> wf = SQLExpressions.sum(path).over().partition(path2).orderBy(path);
        assertEquals("sum(path) over (partition by path2 order by path)", toString(wf));
    }
    
    @Test
    public void All() {
        NumberPath<Long> path = Expressions.numberPath(Long.class, "path");
        assertEquals("sum(path)", toString(SQLExpressions.sum(path)));
        assertEquals("count(path)", toString(SQLExpressions.count(path)));
        assertEquals("avg(path)", toString(SQLExpressions.avg(path)));
        assertEquals("min(path)", toString(SQLExpressions.min(path)));
        assertEquals("max(path)", toString(SQLExpressions.max(path)));
        assertEquals("lead(path)", toString(SQLExpressions.lead(path)));
        assertEquals("lag(path)", toString(SQLExpressions.lag(path)));
        assertEquals("rank()", toString(SQLExpressions.rank()));
        assertEquals("dense_rank()", toString(SQLExpressions.denseRank()));
        assertEquals("row_number()", toString(SQLExpressions.rowNumber()));
        assertEquals("first(path)", toString(SQLExpressions.first(path)));
        assertEquals("last(path)", toString(SQLExpressions.last(path)));
        
    }
    
}
