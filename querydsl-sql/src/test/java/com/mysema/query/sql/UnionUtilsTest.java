package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Operation;
import com.mysema.query.types.PathImpl;
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.TemplateExpression;
import com.mysema.query.types.query.ListSubQuery;

public class UnionUtilsTest {

    @Test
    public void test() {
        ListSubQuery query1 = new SQLSubQuery().list();
        ListSubQuery query2 = new SQLSubQuery().list();
        SQLTemplates templates = new H2Templates();
        Expression<?> operation = UnionUtils.combineUnion(new SubQueryExpression[]{query1, query2}, 
                new PathImpl(Object.class, "path"), templates, false);
        TemplateExpression<?> template = (TemplateExpression<?>)((Operation<?>)operation).getArg(0);
        assertEquals("(({0})\nunion\n({1}))", template.getTemplate().toString());
    }
    
}
